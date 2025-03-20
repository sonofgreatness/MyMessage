package project.mymessage.ui.contacts

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.mymessage.ui.nav.Screen
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

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                groupedContacts.forEach { (initial, contacts) ->
                    item { SectionHeader(title = initial) }

                    items(contacts) { contact ->
                        ContactItem(contact = contact) {
                            val phoneNumber = contact.phoneNumbers.firstOrNull() ?: "Unknown"
                            val fullName = contact.fullName ?: "Unknown"
                            navController.navigate(Screen.ConversationScreen.withArgs(fullName, phoneNumber))
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
    }
}