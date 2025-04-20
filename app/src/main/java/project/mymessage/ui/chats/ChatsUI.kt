package project.mymessage.ui.chats

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import project.mymessage.R
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.database.Entities.Message
import project.mymessage.ui.configs.SearchUI
import project.mymessage.ui.contacts.Contact
import project.mymessage.ui.nav.Screen
import project.mymessage.ui.theme.BlueLight
import project.mymessage.ui.theme.TopBottomLeftRoundedShape
import project.mymessage.ui.theme.body1
import project.mymessage.ui.theme.body2
import project.mymessage.ui.theme.caption
import project.mymessage.ui.theme.h6
import project.mymessage.ui.theme.topAndBottomLeftRounded
import project.mymessage.ui.viewModels.ContactsViewModel
import project.mymessage.ui.viewModels.ConversationViewModel
import project.mymessage.util.Constants
import project.mymessage.util.ConversationsManager
import project.mymessage.util.DatesManager
import project.mymessage.util.Enums
import project.mymessage.util.DatesManager as DatesManager1

class ChatsUI {

    companion object {

        @Composable
        fun MainScreen(navController: NavController, viewModel: ConversationViewModel) {

            val backgroundColor = MaterialTheme.colorScheme.background
            val surface = MaterialTheme.colorScheme.surface

            val conversations by viewModel.readAllConversations.observeAsState(emptyList())
            val totalUnreadMessages by viewModel.totalUnreadMessages.observeAsState(0)
            viewModel.updateConversations()

            LaunchedEffect(viewModel.updateConversations()) {
                println( "Conversations changed: ${conversations.size}")
            }

                Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
                    Column(modifier = Modifier.fillMaxSize()
                       ) {
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
                                        .padding(8.dp, 2.dp)

                                    ,
                                    elevation = CardDefaults.cardElevation(16.dp),
                                    shape = RoundedCornerShape(36.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = surface)

                                ) {
                                    Column(modifier = Modifier.padding(8.dp)
                                        ) {
                                        conversations.forEachIndexed { index, conversationWithMessages ->
                                            ConversationItem2(conversationWithMessages, navController, index)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = {navController.navigate(Screen.AddConversationScreen.route)},
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        contentColor = White
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "New Conversation")
                    }
                }



        }
        @Composable
        fun DraggableTopSheet(totalUnreadMessages:Int, navController: NavController) {

            val surface = MaterialTheme.colorScheme.surface
            val onSurface = MaterialTheme.colorScheme.onSurface

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
                    .background(surface)
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
                                val newHeightPx =
                                    (sheetHeightFraction * screenHeightPx + dragAmount)
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
                            style = MaterialTheme.typography.h6.copy(color = onSurface),
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
                            ,
                            colors =  ButtonDefaults. buttonColors(
                                backgroundColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background
                            )
                        ) {
                            Text("View")
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 6.dp, start = 16.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (sheetHeightFraction > 0.1f) {

                            Spacer(modifier = Modifier.width(16.dp))
                        } else {
                            Text(
                                text = "Messages",
                                style = MaterialTheme.typography.body1.copy(color = onSurface)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search,
                                contentDescription = "Search",
                                tint = onSurface,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        navController.navigate(Screen.SearchScreen.route)
                                    })
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(Icons.Default.MoreVert, contentDescription = "More",
                                tint = onSurface,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        navController.navigate(Screen.AboutScreen.route)
                                    })
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
                    .padding(8.dp)

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
                            .background(
                                color = iconTint,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                            .clickable {
                                navController.navigate(
                                    Screen.ConversationScreen.withArgs(
                                        conversation.from,
                                        conversation.to
                                    )
                                )
                            }
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate(
                                    Screen.ConversationScreen.withArgs(
                                        conversation.from,
                                        conversation.to
                                    )
                                )
                            }
                    ) {
                        Text(text = conversation.from,
                            style = MaterialTheme.typography.h6.copy(color =MaterialTheme.colorScheme.onSurface))
                        Text(text = latestMessage,
                            style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colorScheme.onSurface),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                            )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = latestMessageTime,
                            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colorScheme.onSurface))
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
                                    style = MaterialTheme.typography.caption.copy(color =MaterialTheme.colorScheme.onSurface),
                                    color = White
                                )
                            }

                    }
                }
                SearchUI.GrayDivider()
            }
        }
        @Composable
        fun ConversationScreen(conversationViewModel: ConversationViewModel,
                               name: String?, phone: String?,
                               navController: NavController) {
            LaunchedEffect(name) {
                conversationViewModel.getConversationsWithMessagesFrom(name!!)
            }



            val conversationWithMessages  by conversationViewModel.currentConversation.observeAsState(emptyList())
            val conversations = conversationWithMessages.firstOrNull()?.messages ?: emptyList()
            val messageInput = remember { mutableStateOf("") }


            var drawerVisible by remember { mutableStateOf(false) }
            val configuration = LocalConfiguration.current

            val screenWidth = configuration.screenWidthDp.dp

            val context = LocalContext.current
            val drawerWidthDp = screenWidth * 0.9f
            val to_id = phone!!.toString().trim()
            val from_id = name!!.toString().trim()


            val currenContact : List<Contact> = listOf(
                Contact(
                    fullName = to_id ,
                    phoneNumbers = listOf(to_id)

                )
            )

            // Convert Dp to Float for Animatable
            val density = LocalDensity.current
            val drawerWidthPx = with(density) { drawerWidthDp.toPx() }

            val offsetX = remember { Animatable(drawerWidthPx) }


            LaunchedEffect(drawerVisible) {
                offsetX.animateTo(
                    targetValue = if (drawerVisible) 0f else drawerWidthPx,
                    animationSpec = tween(durationMillis = 300)
                )
            }

            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Top

            ) {

                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                 Row(Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.SpaceBetween,
                     verticalAlignment = Alignment.CenterVertically)
                 {
                     Icon(Icons.Default.ArrowBack,
                         contentDescription = "Back",
                         tint = MaterialTheme.colorScheme.onBackground,
                         modifier = Modifier
                             .size(36.dp)
                             .padding(9.dp)
                             .clickable {
                                 navController.navigate(Screen.MainScreen.route)
                             }


                     )
                     Column {
                         Text(name, style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colorScheme.onSurface))
                         Text(phone, style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colorScheme.onSurface))
                     }
                     Icon(
                         imageVector = Icons.Default.MoreVert,
                         contentDescription = "More",
                         tint = MaterialTheme.colorScheme.onSurface,
                         modifier = Modifier
                             .size(36.dp)
                             .padding(9.dp)
                             .clickable {
                                 drawerVisible = true
                             })
                 }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    if (conversations.isEmpty()) {
                        Text("No messages available"
                        ,style = MaterialTheme.typography.bodyMedium.copy(color =MaterialTheme.colorScheme.onSurface)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        BottomRow(contacts = currenContact,
                            messageInput = messageInput,
                            conversationViewModel = conversationViewModel)

                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f),
                        ) {
                            item{
                                Column {
                                    conversations.forEach{ conversation->
                                        ConversationItem(conversation)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        BottomRow(contacts = currenContact,
                            messageInput = messageInput,
                            conversationViewModel = conversationViewModel
                            )
                    }
                }
            }
            if (drawerVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                        .clickable {
                            drawerVisible = false
                        }
                        .topAndBottomLeftRounded(
                            topLeftRadius = 16.dp,
                            bottomLeftRadius = 16.dp
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .offset(x = with(density) { offsetX.value.toDp() }) // Convert Float back to Dp
                            .width(drawerWidthDp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd) // Drawer slides in from the right
                            .background(
                                MaterialTheme.colorScheme.background,
                                shape = TopBottomLeftRoundedShape(
                                    topLeft = CornerSize(16.dp),
                                    bottomLeft = CornerSize(16.dp)
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(9.dp)
                                        .clickable {
                                            deleteMessages(
                                                conversationViewModel,
                                                to_id,
                                                from_id,
                                                context
                                            )
                                        }
                                )
                                Text(
                                    "Delete messages", modifier = Modifier
                                        .padding(9.dp)
                                        .clickable {
                                            deleteMessages(
                                                conversationViewModel,
                                                to_id,
                                                from_id,
                                                context
                                            )
                                        },

                                    style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                                )

                            }
                            Spacer(Modifier.weight(1f))
                            SimIconRow(conversationViewModel)
                        }
                    }
                }
                }


            }
        }



       private fun deleteMessages(conversationViewModel: ConversationViewModel,
                           to_id:String,from_id:String,  context: Context
        ) {
           conversationViewModel.deleteMessagesFromConversation(to_id)
           conversationViewModel.getConversationsWithMessagesFrom(from_id)
           Toast.makeText(context, "messages deleted", Toast.LENGTH_SHORT).show()

       }


        @Composable
        fun ConversationItem(message: Message, index: Int = 2) {
           if (message.messageType == Enums.MessageType.Outgoing.value)
               OutgoingMessage(message,index)
            else
                IncomingMessage(message)

        }




        @Composable
        fun OutgoingMessage(message: Message, index: Int){
            val iconTint = Constants.iconColors[index % Constants.iconColors.size]
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Person,
                    tint  = White,
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .background(
                            color = iconTint,
                            shape = CircleShape
                        )
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = message.content,
                                style = MaterialTheme.typography.body1)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = DatesManager.convertTimestampToString(message.dateCreated),
                            style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

        }



        @Composable
        fun IncomingMessage(message: Message){
           Row( modifier = Modifier
               .fillMaxWidth().padding(16.dp),
               horizontalArrangement = Arrangement.End,
               verticalAlignment = Alignment.Bottom)
            {
                Text(
                    text = DatesManager.convertTimestampToString(message.dateCreated),
                    modifier = Modifier.padding(top = 4.dp,
                    end =4.dp )
                    , style = MaterialTheme.typography.caption.
                    copy(color = MaterialTheme.colorScheme.onSurface)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = message.content,
                            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colorScheme.onSurface))
                    }
                }

            }
        }


        @Composable
        fun SimIconRow(conversationViewModel: ConversationViewModel) {
            val simDisplayName by conversationViewModel.currentSimDisplayName.observeAsState("")
            val simIcon by conversationViewModel.currentSimIcon.observeAsState(null)
            val currentSimSlot = conversationViewModel.getCurrentSimSlot()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    val newSimSlot = if (currentSimSlot == 0) 1 else 0 // Toggle between 0 and 1
                    conversationViewModel.selectSimCard(newSimSlot)
                }
            ) {
                if (simIcon != null) {
                    val painter: Painter = remember { BitmapPainter(simIcon!!.toBitmap().asImageBitmap()) }
                    Image(
                        painter = painter,
                        contentDescription = "Sim",
                        modifier = Modifier
                            .size(36.dp)
                            .padding(9.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_sim_card_24), // Replace with your placeholder drawable resource
                        contentDescription = "Sim Placeholder",
                        modifier = Modifier
                            .size(36.dp)
                            .padding(9.dp),
                        tint = Color.Gray
                    )
                }
                Text(simDisplayName, modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }
        @Composable
        fun UnreadMessages(navController: NavController, viewModel: ConversationViewModel)
        {


            val conversations by viewModel.readAllConversations.observeAsState(emptyList())
            val totalUnreadMessages by viewModel.totalUnreadMessages.observeAsState(0)

            Column(modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)) {
                DraggableTopSheetUnreadMessages(totalUnreadMessages, navController)
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
                                .padding(8.dp, 2.dp)

                            ,
                            elevation = CardDefaults.cardElevation(16.dp),
                            shape = RoundedCornerShape(36.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface)

                        ) {
                            Column(modifier = Modifier.padding(8.dp)
                            ) {
                                conversations.forEachIndexed { index, conversationWithMessages ->
                                    ConversationItem2(conversationWithMessages, navController, index)
                                }
                            }

                    }
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
                    .background(MaterialTheme.colorScheme.surface)
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
                                val newHeightPx =
                                    (sheetHeightFraction * screenHeightPx + dragAmount)
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
                        Text(
                            text = unreadText,
                            style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colorScheme.onSurface),
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
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { navController.navigate(Screen.MainScreen.route) }
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.MoreVert,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "More", modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }




        @Composable
        fun NewConversationScreen(
            navController: NavController,
            conversationViewModel: ConversationViewModel,
            contactsViewModel: ContactsViewModel,
            recipients:SnapshotStateList<Contact>)
        {
            val recipientInput = remember { mutableStateOf("") }
            val messageInput = remember { mutableStateOf("") }

            val showDialog = remember { mutableStateOf(false) }
            val tempRecipient = remember { mutableStateOf("") }

            Column(Modifier
                .padding(16.dp)
                .fillMaxHeight()){
              TitleRowPopsBackToConversation(title = "New Conversation", navController = navController, conversationViewModel)
                GridOfChips(recipients)
                TextField(
                    value = recipientInput.value,
                    onValueChange = { recipientInput.value = it
                        if (recipientInput.value.isNotBlank()) {
                            tempRecipient.value = recipientInput.value
                            showDialog.value = true
                        }},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Add recipient") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Add,
                            tint = White,
                            contentDescription = "Add recipients",
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = DarkGray, shape = CircleShape)
                                .clickable {
                                    navController.navigate(Screen.SelectRecipientScreen.route)
                                }
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (recipientInput.value.isNotBlank()) {
                            tempRecipient.value = recipientInput.value
                            showDialog.value = true
                        }
                    })
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (showDialog.value) {
                    Row{
                        Icon(
                            Icons.Default.Add,
                            tint = White,
                            contentDescription = "Add recipient",
                            modifier = Modifier
                                .size(32.dp)
                                .background(color = DarkGray, shape = CircleShape)
                                .clickable {
                                    recipients.add(
                                        Contact(
                                            listOf(tempRecipient.value),
                                            fullName = null
                                        )
                                    )
                                    recipientInput.value = ""
                                    showDialog.value = false
                                })
                        Text(text = tempRecipient.value
                        ,modifier = Modifier.padding(start = 8.dp)
                        ) }
                }
                Spacer(modifier = Modifier.weight(1f))

                BottomRow(messageInput = messageInput, contacts = recipients, conversationViewModel)

            }

        }

        @Composable
        fun GridOfChips(recipients: SnapshotStateList<Contact>) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recipients) { recipient ->
                    Chip(
                        recipient = recipient.phoneNumbers[0],
                        onRemove = { recipients.remove(recipient) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        @Composable
        fun BottomRow(messageInput: MutableState<String>, contacts: List<Contact>,
                      conversationViewModel: ConversationViewModel) {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) { padding ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.Bottom)
                        .padding(padding)
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    TextField(
                        value = messageInput.value,
                        onValueChange = { messageInput.value = it },

                        modifier = Modifier
                            .width(screenWidth * 0.7f)
                            .padding(end = 8.dp),
                        placeholder = { Text(" Write message ...")},
                        colors= TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onBackground,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor   = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)

                        )
                    )
                    IconButton(
                        onClick = {
                            if (contacts.isNotEmpty()) {
                                sendMessageToContacts(
                                    message = messageInput.value,
                                    contacts = contacts,
                                    conversationViewModel = conversationViewModel
                                )
                                messageInput.value = ""
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("No recipients selected")
                                }
                            }
                        },
                        modifier = Modifier.width(screenWidth * 0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

       private  fun sendMessageToContacts(contacts : List<Contact>, message:String,
                                  conversationViewModel: ConversationViewModel){
        conversationViewModel.sendMessageToContacts(contacts, message)
        }

        @Composable fun TitleRow(title: String, navController: NavController)
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(36.dp)
                        .padding(9.dp)
                        .clickable {
                            navController.popBackStack() }
                )
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
        @Composable fun TitleRowPopsBackToConversation(title: String, navController: NavController, conversationViewModel: ConversationViewModel)
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(36.dp)
                        .padding(9.dp)
                        .clickable {
                            conversationViewModel.updateConversations()
                            navController.popBackStack() }
                )
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
        @Composable
        fun Chip(recipient: String, onRemove: () -> Unit) {
            Row(
                modifier = Modifier
                    .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(recipient, modifier = Modifier.padding(end = 4.dp))
                Icon(

                    Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Red,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onRemove() }
                )
            }
        }
    }


}
