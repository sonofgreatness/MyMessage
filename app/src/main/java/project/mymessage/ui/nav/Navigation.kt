package project.mymessage.ui.nav


    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material.BottomNavigation
    import androidx.compose.material.BottomNavigationItem
    import androidx.compose.material.Scaffold
    import androidx.compose.material.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.mutableStateListOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.navigation.NavController
    import androidx.navigation.NavType
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.navArgument
    import androidx.navigation.compose.rememberNavController
    import com.google.gson.Gson
    import project.mymessage.ui.chats.ChatsUI
    import project.mymessage.ui.chats.ChatsUI.Companion.ConversationScreen
    import project.mymessage.ui.chats.ChatsUI.Companion.MainScreen
    import project.mymessage.ui.chats.ChatsUI.Companion.UnreadMessages
    import project.mymessage.ui.configs.AboutUI
    import project.mymessage.ui.configs.FilteredListsUI
    import project.mymessage.ui.configs.SearchUI.Companion.SearchScreen
    import project.mymessage.ui.contacts.Contact
    import project.mymessage.ui.contacts.ContactUI
    import project.mymessage.ui.viewModels.AboutViewModel
    import project.mymessage.ui.viewModels.ContactsViewModel
    import project.mymessage.ui.viewModels.ConversationViewModel
    import project.mymessage.ui.viewModels.SearchViewModel


@Composable
fun Navigation(conversationViewModel: ConversationViewModel,
               contactsViewModel: ContactsViewModel,
               searchViewModel: SearchViewModel,
               aboutViewModel: AboutViewModel
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                composable(route = Screen.MainScreen.route) {
                    MainScreen(navController = navController,conversationViewModel)
                }
                composable(route = Screen.UnreadMessageScreen.route) {
                    UnreadMessages(navController = navController,conversationViewModel)
                }
                composable(route = Screen.SearchScreen.route) {
                    SearchScreen(navController = navController,contactsViewModel, conversationViewModel,searchViewModel)
                }


                composable(
                    route = Screen.ConversationScreen.route + "/{name}/{phone}",
                    arguments = listOf(
                        navArgument("name") {
                            type = NavType.StringType
                            defaultValue = "Simphiwe"
                            nullable = true
                        },
                        navArgument("phone") {
                            type = NavType.StringType
                            defaultValue = "0000000000"
                            nullable = true
                        },
                    )
                ) { entry ->

                 ConversationScreen(name = entry.arguments?.getString("name"),
                     phone = entry.arguments?.getString("phone"),
                 conversationViewModel =conversationViewModel, navController = navController )
                }
                composable(route = Screen.ContactsScreen.route) {
                    ContactUI.ContactsScreen(context = LocalContext.current, navController = navController,contactsViewModel)
                }
                composable(route =Screen.MessagesScreen.route+"/{search_term}",
                    arguments = listOf(
                        navArgument("search_term") {
                            type = NavType.StringType
                            nullable = true})
                    ){
                    entry->
                    FilteredListsUI.FilteredMessagesScreen(navController = navController,
                        conversationViewModel, search_term = entry.arguments?.getString("search_term"))
                }
                composable(route = Screen.FilteredContactScreen.route+"/{search_term}",
                    arguments = listOf(
                        navArgument("search_term") {
                            type = NavType.StringType
                            nullable = true})

                ){
                    entry->
                    FilteredListsUI.FilteredContactScreen(
                        navController = navController,
                        contactsViewModel,
                        search_term = entry.arguments?.getString("search_term")
                    )
                }

                composable(route = Screen.FilteredConversationScreen.route+"/{search_term}",
                    arguments = listOf(
                        navArgument("search_term") {
                            type = NavType.StringType
                            nullable = true})


                ){
                        entry->
                    FilteredListsUI.FilteredConversationScreen(
                        navController ,
                        conversationViewModel,
                        search_term = entry.arguments?.getString("search_term")
                    )
                }
                composable(route = Screen.AboutScreen.route){
                    AboutUI.AboutScreen(
                        viewModel =aboutViewModel)
                }
                composable(route =  Screen.AddConversationScreen.route+
                    "?recipients={recipients}",
                    arguments = listOf(
                        navArgument("recipients") {
                            type = NavType.StringType
                            defaultValue = ""
                            nullable = true}


                )
                )
                {
                    entry ->
                    val recipientJson = entry.arguments?.getString("recipients")
                    val recipients  = remember {val initialList = recipientJson?.let{
                        Gson().fromJson(it,
                            Array<Contact>::class.java)?.toList()
                    } ?: emptyList()
                        mutableStateListOf<Contact>().apply {
                            addAll(initialList)
                        }
                    }
                    ChatsUI.NewConversationScreen(
                        navController ,
                        conversationViewModel,
                        contactsViewModel,
                        recipients)
                }
                composable(route = Screen.SelectRecipientScreen.route){
                    ContactUI.SelectRecipient(
                        navController = navController,
                        viewModel = contactsViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screen.MainScreen, Screen.ContactsScreen) // Define tabs
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                label = { Text(screen.title!!) },
                icon = {},
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

