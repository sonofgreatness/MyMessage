package project.mymessage.ui.contacts

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import project.mymessage.ui.chats.ChatsUI
import project.mymessage.ui.configs.SearchUI
import project.mymessage.ui.nav.Screen
import project.mymessage.ui.theme.BlueLight
import project.mymessage.ui.viewModels.ContactsViewModel


class ContactUI {


    companion object {


        @Composable
        fun ContactsScreen(
            context: Context,
            navController: NavController,
            viewModel: ContactsViewModel
        ) {
            val hasPermission by viewModel.hasPermission.collectAsState()
            val groupedContacts by viewModel.groupedContacts.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.checkAndRequestPermissions(context as Activity)
            }
            if (!hasPermission) {
                Text("Permission required to access contacts.")
                return
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                groupedContacts.forEach { (initial, contacts) ->
                    item { SectionHeader(title = initial) }

                    items(contacts) { contact ->
                        ContactItem(contact = contact) {
                            val phoneNumber = contact.phoneNumbers.firstOrNull() ?: "Unknown"
                            val fullName = contact.fullName ?: "Unknown"
                            navController.navigate(
                                Screen.ConversationScreen.withArgs(
                                    fullName,
                                    phoneNumber
                                )
                            )
                        }
                    }
                }
            }
        }


        @Composable
        fun SectionHeader(title: Char) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(color = Color.Gray, shape = RoundedCornerShape(4.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = title.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        @Composable
        fun ContactItem(contact: Contact, onClick: () -> Unit) {
            Card(
                shape = RectangleShape,
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onClick) // Click listener added
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = contact.fullName ?: "Unknown", fontWeight = FontWeight.Bold)
                    contact.phoneNumbers.forEach { phone ->
                        Text(text = phone, fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }
        }

        @Composable
        fun SelectRecipient(
            navController: NavController,
            viewModel: ContactsViewModel

        ) {
            val searchQuery = remember { mutableStateOf("") }
            val groupedContacts by viewModel.filteredContacts.collectAsState()
            var recipients = remember { mutableStateListOf<Contact>() }
            val isSearchVisible = remember { mutableStateOf(false) }
            val tempRecipient = remember { mutableStateOf("") }

            Column(Modifier.padding(16.dp)) {
                ChatsUI.TitleRow(title = "Select recipients", navController = navController)
                ChatsUI.GridOfChips(recipients)
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = {
                        searchQuery.value = it
                        if (searchQuery.value!=""){
                            updateContacts(contactsViewModel = viewModel, searchQuery)
                            isSearchVisible.value = true
                        }

                    },
                    placeholder = { Text("Search Contacts or enter number") },
                    singleLine = true,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (searchQuery.value.isNotBlank()) {
                            tempRecipient.value = searchQuery.value
                            isSearchVisible.value = true

                        }
                    })
                , trailingIcon = ({Icon(
                       imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                      modifier = Modifier.clickable {
                            searchQuery.value = ""
                            isSearchVisible.value = false
                      }
                )})
                )
                Row{
                    Button(onClick = {
                        navController.navigate(Screen.AddConversationScreen.withArgs(Gson().toJson(recipients.toList())))
                    }) {
                        Text("Done")
                    }
                }
                if (isSearchVisible.value)
                LazyColumn {
                    item { SearchUI.TitleText("Contacts" + " ( " + groupedContacts.values.sumOf { it.size } + " )") }

                    item {
                        androidx.compose.material.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, BlueLight)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                groupedContacts.values.flatten().forEachIndexed { index, contact ->
                                val phoneNumber = contact.phoneNumbers.firstOrNull() ?: "Unknown"
                                val fullName = contact.fullName ?: "Unknown"
                                Row {
                                    SearchUI.HighlightedText(fullText = fullName,
                                        phoneNumber = phoneNumber,
                                        query = searchQuery.value,
                                        onClick = {
                                            recipients.add(
                                                Contact(
                                                    listOf(phoneNumber),
                                                    fullName =  fullName)
                                            )

                                        }, index = index)}
                                }
                            }
                        }
                    }
                }
            }
        }
        fun updateContacts (
            contactsViewModel: ContactsViewModel,
            searchQuery: MutableState<String>){
            contactsViewModel.filterContacts(searchQuery.value)
        }
    }
}