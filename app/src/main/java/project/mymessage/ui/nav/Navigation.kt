package project.mymessage.ui.nav


    import project.mymessage.ui.chats.ChatsUI.Companion.ConversationScreen
    import project.mymessage.ui.chats.ChatsUI.Companion.MainScreen
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material.BottomNavigation
    import androidx.compose.material.BottomNavigationItem
    import androidx.compose.material.Scaffold
    import androidx.compose.material.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.platform.LocalContext
    import androidx.navigation.NavController
    import androidx.navigation.NavType
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.navArgument
    import androidx.navigation.compose.rememberNavController
    import project.mymessage.ui.chats.ChatsUI
    import project.mymessage.ui.chats.ChatsUI.Companion.UnreadMessages
    import project.mymessage.ui.configs.FilteredListsUI
    import project.mymessage.ui.configs.SearchUI.Companion.SearchScreen
    import project.mymessage.ui.contacts.ContactUI
    import project.mymessage.ui.viewModels.ContactsViewModel
    import project.mymessage.ui.viewModels.ConversationViewModel

    import project.mymessage.ui.viewModels.SearchViewModel


@Composable
fun Navigation(conversationViewModel: ConversationViewModel,
               contactsViewModel: ContactsViewModel,
               searchViewModel: SearchViewModel
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
                 conversationViewModel =conversationViewModel )
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
                icon = {}, // Add an icon if needed
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

