package project.mymessage.ui.viewModels

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.Manifest
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import project.mymessage.ui.contacts.Contact
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val application: Application
) : ViewModel() {

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    private val _groupedContacts = MutableStateFlow<Map<Char, List<Contact>>>(emptyMap())
    val groupedContacts: StateFlow<Map<Char, List<Contact>>> = _groupedContacts.asStateFlow()

    private val _filteredContacts = MutableStateFlow<Map<Char, List<Contact>>>(emptyMap())
    val filteredContacts: StateFlow<Map<Char, List<Contact>>> = _filteredContacts

    fun checkAndRequestPermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            _hasPermission.value = true
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_CONTACTS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                1
            )
        }
    }



    private fun loadContacts() {
        viewModelScope.launch {
            getGroupedContacts(application).let{
                _groupedContacts.value = it
                _filteredContacts.value = it
            }
        }
    }

    private fun getGroupedContacts(context: Context): Map<Char, List<Contact>> {
        val contactsMap = mutableMapOf<Char, MutableList<Contact>>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val rawName = if (nameIndex != -1) it.getString(nameIndex) else "Unknown"
                val rawNumber = if (numberIndex != -1) it.getString(numberIndex) else ""

                val normalizedNumber = rawNumber.replace("\\s".toRegex(), "")
                val firstChar = rawName.firstOrNull()?.uppercaseChar() ?: '&'

                // Assign '&' if name starts with a number or special character
                val groupChar = if (firstChar.isLetter()) firstChar else '&'

                // Add to group
                contactsMap.getOrPut(groupChar) { mutableListOf() }
                    .add(Contact(phoneNumbers = listOf(normalizedNumber), fullName = rawName))
            }
        }
        return contactsMap.toSortedMap() // Ensure alphabetical sorting
    }


     fun filterContacts( query:String) {
         val lowerCaseQuery = query.lowercase()
         _filteredContacts.value = if (lowerCaseQuery.isBlank()) {
             _groupedContacts.value
         } else {
             _groupedContacts.value.mapValues { (_, contacts) ->
                 contacts.filter { contact ->
                     contact.fullName?.lowercase()?.contains(lowerCaseQuery) == true ||
                             contact.phoneNumbers.any { it.lowercase().contains(lowerCaseQuery) }
                 }
             }.filterValues { it.isNotEmpty() }
         }
    }

    fun updatePermissionStatus(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) {
            loadContacts()
        }
    }
}
