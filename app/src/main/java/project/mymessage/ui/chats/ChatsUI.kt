package project.mymessage.ui.chats

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.database.Entities.Message
import project.mymessage.ui.configs.SearchUI
import project.mymessage.ui.nav.Screen
import project.mymessage.ui.theme.BlueLight
import project.mymessage.ui.viewModels.ConversationViewModel
import project.mymessage.util.Constants
import project.mymessage.util.ConversationsManager
import project.mymessage.util.DatesManager as DatesManager1

class ChatsUI {

    companion object {
        @Composable
        fun MainScreen(navController: NavController, viewModel: ConversationViewModel) {
            val conversations by viewModel.readAllConversations.observeAsState(emptyList())
            val totalUnreadMessages by viewModel.totalUnreadMessages.observeAsState(0)
            viewModel.updateConversations()

            Column(modifier = Modifier.fillMaxSize()) {
                DraggableTopSheet(totalUnreadMessages, navController)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.weight(1f))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, BlueLight)
                        ){
                            Column(modifier = Modifier.padding(8.dp)) {
                                conversations.forEachIndexed{
                                        index ,conversationWithMessages ->
                                    ConversationItem2(conversationWithMessages, navController, index)
                                }
                            }
                        }
                    }
                }
            }

        }
        @Composable
        fun DraggableTopSheet(totalUnreadMessages:Int, navController: NavController) {
            var sheetHeightFraction by remember { mutableStateOf(0.4f) }
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            val coroutineScope = rememberCoroutineScope()
            val unreadText : String = if(totalUnreadMessages ==0)
                "No Unread Messages"
            else
                "$totalUnreadMessages   Unread Messages"

            val animatedHeight by animateFloatAsState(
                targetValue = sheetHeightFraction * screenHeightPx,
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { animatedHeight.toDp() })
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    sheetHeightFraction = if (sheetHeightFraction < 0.15f) {
                                        0.1f
                                    } else {
                                        0.5f
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                val newHeightPx = (sheetHeightFraction * screenHeightPx + dragAmount)
                                    .coerceIn(0.1f * screenHeightPx, 0.4f * screenHeightPx)

                                sheetHeightFraction = newHeightPx / screenHeightPx
                            }
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sheetHeightFraction > 0.15f) {
                        // Only show this content when expanded
                        Text(
                            text = unreadText,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(16.dp)
                        )

                        // Centered Button with rounded corners
                        Button(
                            onClick = {
                                navController.navigate(Screen.UnreadMessageScreen.route)
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .padding(6.dp)
                                .height(48.dp)
                                .width(150.dp)
                        ) {
                            Text("View")
                        }
                    }

                    // Row: "Messages" on the left, Icons on the right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top =16.dp , bottom = 6.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (sheetHeightFraction > 0.1f) {

                            Spacer(modifier = Modifier.width(16.dp))
                        } else {
                            Text(
                                text = "Messages",
                                style = MaterialTheme.typography.body1
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp).clickable {
                                    navController.navigate(Screen.SearchScreen.route)
                                })
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(Icons.Default.MoreVert, contentDescription = "More",
                                modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }

        @Composable
        fun ConversationItem2(
            conversationWithMessages: ConversationWithMessages,
            navController: NavController,
            index : Int

        ) {
            val conversation = conversationWithMessages.conversation
            val latestMessage = conversationWithMessages.messages.lastOrNull()?.content ?: "No messages"
            val latestMessageTime =DatesManager1.getLatestMessageDateFromMessages(conversationWithMessages.messages)
             val latestUnreadMessages = ConversationsManager.getNumberOfUnreadMessages(conversationWithMessages.messages)
            val iconTint = Constants.iconColors[index % Constants.iconColors.size]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        tint  = White,
                        contentDescription = "User Icon",
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = iconTint,
                            shape = CircleShape)
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(Screen.ConversationScreen.withArgs(conversation.from, conversation.to))
                            }
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(Screen.ConversationScreen.withArgs(conversation.from, conversation.to))
                            }
                    ) {
                        Text(text = conversation.from, style = MaterialTheme.typography.h6)
                        Text(text = latestMessage,
                            style = MaterialTheme.typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                            )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = latestMessageTime, style = MaterialTheme.typography.caption)
                        Spacer(modifier = Modifier.height(24.dp))
                        if (latestUnreadMessages!=0)
                            Box(
                                modifier = Modifier
                                    .size(24.dp) // Adjust size as needed
                                    .background(BlueLight, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = latestUnreadMessages.toString(),
                                    style = MaterialTheme.typography.caption,
                                    color = White
                                )
                            }

                    }
                }
                SearchUI.GrayDivider()
            }
        }



        @Composable
        fun ConversationScreen(conversationViewModel: ConversationViewModel,name: String?, phone: String?) {
            LaunchedEffect(name) {
                conversationViewModel.getConversationsWithMessagesFrom(name!!)
            }

            val conversationWithMessages  by conversationViewModel.currentConversation.observeAsState(emptyList())
            val conversations = conversationWithMessages.firstOrNull()?.messages ?: emptyList()


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // First Box for the Greeting Message
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("$name", style = MaterialTheme.typography.h2)
                    Text("$phone", style = MaterialTheme.typography.h6)
                }

                Spacer(modifier = Modifier.height(16.dp))


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    if (conversations.isEmpty()) {
                        Text("No messages available")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(conversations) { conversation ->
                                ConversationItem(conversation)
                            }
                        }
                    }
                }
            }
        }


        @Composable
        fun ConversationItem(message: Message) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = message.content, style = MaterialTheme.typography.h6)
                    Text(text = "On: ${message.dateCreated}")
                    Text(text = "is_read: ${message.isRead}")
                    Text(text = "type: ${message.messageType}")
                    Text(text = "from - to: ${message.from_id}  - ${message.status}")

                }
            }
        }


        @Composable
        fun UnreadMessages(navController: NavController, viewModel: ConversationViewModel)
        {


            val conversations by viewModel.readAllConversations.observeAsState(emptyList())
            val totalUnreadMessages by viewModel.totalUnreadMessages.observeAsState(0)

            Column(modifier = Modifier.fillMaxSize()) {
                DraggableTopSheetUnreadMessages(totalUnreadMessages, navController)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.weight(1f))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    itemsIndexed(conversations) { index, conversationWithMessages ->
                        ConversationItem2(conversationWithMessages, navController, index)
                    }
                }
            }
        }

        @Composable
        fun DraggableTopSheetUnreadMessages(totalUnreadMessages:Int, navController: NavController) {
            var sheetHeightFraction by remember { mutableStateOf(0.5f) }
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            val coroutineScope = rememberCoroutineScope()
            val unreadText : String = if(totalUnreadMessages ==0)
                "No Unread Messages"
            else
                "$totalUnreadMessages   Unread Messages"

            val animatedHeight by animateFloatAsState(
                targetValue = sheetHeightFraction * screenHeightPx,
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { animatedHeight.toDp() })
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    sheetHeightFraction = if (sheetHeightFraction < 0.15f) {
                                        0.1f
                                    } else {
                                        0.5f
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                val newHeightPx = (sheetHeightFraction * screenHeightPx + dragAmount)
                                    .coerceIn(0.1f * screenHeightPx, 0.5f * screenHeightPx)

                                sheetHeightFraction = newHeightPx / screenHeightPx
                            }
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sheetHeightFraction > 0.15f) {
                        // Only show this content when expanded
                        Text(
                            text = unreadText,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(16.dp)
                        )

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                        .size(32.dp)
                                .clickable {navController.navigate(Screen.MainScreen.route)}
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.MoreVert, contentDescription = "More", modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }
    }
}
