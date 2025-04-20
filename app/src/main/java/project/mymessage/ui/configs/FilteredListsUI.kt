package project.mymessage.ui.configs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import project.mymessage.ui.configs.SearchUI.Companion.MessageItem
import project.mymessage.ui.nav.Screen
import project.mymessage.ui.viewModels.ContactsViewModel
import project.mymessage.ui.viewModels.ConversationViewModel

class
FilteredListsUI {


    companion object {
        @Composable
        fun FilteredMessagesScreen(
            navController: NavController,
            conversationViewModel: ConversationViewModel,
            search_term: String?
        ) {

            val messages by conversationViewModel.filteredMessages.observeAsState(emptyList())
            conversationViewModel.updateFilteredMessages(search_term ?: "")
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                Row(modifier = Modifier.padding(32.dp)) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() })

                    TitleTextForFilter("Messages (" + messages.size.toString() + ")")

                }
                LazyColumn {
                    this.itemsIndexed(messages) { index, message ->
                        MessageItem(message, navController, index,search_term)

                    }
                }
            }

        }

        @Composable
        fun FilteredContactScreen(
            navController: NavController,
            contactsViewModel: ContactsViewModel,
            search_term: String?
        ) {
            val groupedContacts by contactsViewModel.filteredContacts.collectAsState()
            contactsViewModel.filterContacts(search_term ?: "")
            Column (modifier = Modifier.background(MaterialTheme.colorScheme.background)){
                Row (modifier = Modifier.padding(32.dp)) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() })

                    TitleTextForFilter("Contacts (" + groupedContacts.values.sumOf { it.size } + ")")

                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    groupedContacts.forEach { (initial, contacts) ->

                        itemsIndexed(contacts) { index ,contact ->
                            val phoneNumber = contact.phoneNumbers.firstOrNull() ?: "Unknown"
                            val fullName = contact.fullName ?: "Unknown"
                            Row {
                                SearchUI.HighlightedText(fullText = fullName,
                                    phoneNumber = phoneNumber,
                                    query = search_term ?: "",
                                    onClick = {
                                        navController.navigate(
                                            Screen.ConversationScreen.withArgs(
                                                fullName,
                                                phoneNumber
                                            )
                                        )
                                    },
                                    index = index
                                )
                            }

                        }
                    }
                }
            }

        }



        @Composable
        fun FilteredConversationScreen(
            navController: NavController,
            conversationViewModel: ConversationViewModel,
            search_term: String?
        )
        {
            val conversations by conversationViewModel.filteredConversations.observeAsState(emptyList())
            conversationViewModel.updateFilteredConversations(search_term?:"")


            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                Row (modifier = Modifier.padding(32.dp)) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { navController.popBackStack() })

                    TitleTextForFilter("Conversations (" + conversations.size.toString() + ")")

                }
                LazyColumn {
                        itemsIndexed(conversations) {
                                index, conversationWithMessages ->
                            SearchUI.ConversationItemSearch(
                                conversationWithMessages,
                                navController,
                                index =index,
                                search_term
                            )
                        }

                    }
                }
            }
        @Composable
        fun TitleTextForFilter(titleText :String){
            Text(text = titleText,
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )

        }

    }
}
