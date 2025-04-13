package project.mymessage.ui.nav

sealed class Screen(val route: String, val title: String?) {
    object MainScreen : Screen("main_screen", "Conversations")
    object ContactsScreen : Screen("contacts_screen", "Contacts")
    object FilteredContactScreen : Screen("filtered_contact_screen", "FilteredContacts")
    object FilteredConversationScreen : Screen("filtered_conversation_screen", "FilteredConversations")
    object ConversationScreen : Screen("conversation_screen", "Chat")
    object UnreadMessageScreen : Screen("unread_messages_screen", "UnreadMessages")
    object SearchScreen : Screen("search_screen", "Search")
    object MessagesScreen : Screen("messages_screen", "messages")
    object AboutScreen : Screen("about_screen", "about")
    object AddConversationScreen : Screen("add_conversation_screen", "New Conversation")
    object SelectRecipientScreen : Screen("select_recipient_screen", "Select Recipient")

    fun withArgs(vararg args: String): String {
        return if (args.isNotEmpty()) {
            buildString {
                append(route)
                args.forEachIndexed { index, arg ->
                    if (index == 0 && route == AddConversationScreen.route) {
                        append("?recipients=$arg")
                    } else {
                        append("/$arg")
                    }
                }
            }
        } else {
            route
        }
    }
}
