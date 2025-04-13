package project.mymessage.ui.configs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import project.mymessage.database.Entities.ConversationWithMessages
import project.mymessage.database.Entities.Message
import project.mymessage.database.Entities.SearchQuery
import project.mymessage.ui.nav.Screen
import project.mymessage.ui.theme.BlueLight
import project.mymessage.ui.viewModels.ContactsViewModel
import project.mymessage.ui.viewModels.ConversationViewModel
import project.mymessage.ui.viewModels.SearchViewModel
import project.mymessage.util.Constants.Companion.HIGHLIGHTCOLOR
import project.mymessage.util.Constants.Companion.MAX_CONTACTS_SEARCH
import project.mymessage.util.Constants.Companion.MAX_CONVERSATIONS_SEARCH
import project.mymessage.util.Constants.Companion.MAX_MESSAGES_SEARCH
import project.mymessage.util.Constants.Companion.iconColors
import project.mymessage.util.ConversationsManager
import project.mymessage.util.DatesManager

class SearchUI {

    companion object{
        @Composable
        fun SearchScreen(
            navController: NavController,
            contactsViewModel: ContactsViewModel,
            conversationViewModel: ConversationViewModel,
            searchViewModel: SearchViewModel
        ) {
            val searchQuery = remember { mutableStateOf("") }
            val searchResults by searchViewModel.searchQueries.observeAsState((emptyList()))
            val conversations by conversationViewModel.filteredConversations.observeAsState(emptyList())
            val messages by conversationViewModel.filteredMessages.observeAsState(emptyList())
            val groupedContacts by contactsViewModel.filteredContacts.collectAsState()
            var isSearchVisible by remember { mutableStateOf(true) }


            Column(modifier = Modifier.fillMaxSize()) {
                DraggableTopSheetSearch(navController, searchQuery, searchViewModel, contactsViewModel, conversationViewModel,{isSearchVisible = !isSearchVisible}) {

                    isSearchVisible = false
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isSearchVisible){
                    LazyColumn(
                        modifier =  Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        item {
                            Text(
                                text = "Recent Searches",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                        items(searchResults) { searchString ->
                            SearchesItem(searchString,searchQuery, searchViewModel,
                                contactsViewModel,conversationViewModel,{searchViewModel.deleteSearchQuery(searchString.term)} ) {
                                isSearchVisible = !isSearchVisible
                            }
                        }
                        item {
                            Text(
                                text = "Clear all searches",
                                style = MaterialTheme.typography.body2.copy(color = Color.Red),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { searchViewModel.clearAllSearchQueries() }
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier =  Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    item {

                        TitleText("Messages" + "("+ messages.size.toString()+")")
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, BlueLight)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                messages.take(MAX_MESSAGES_SEARCH).forEachIndexed { index, message ->
                                    MessageItem(message, navController,index,searchQuery.value)
                                }

                                if(messages.size > MAX_MESSAGES_SEARCH) {
                                    ViewMoreText {
                                        navController.navigate(
                                            Screen.MessagesScreen.withArgs(
                                                searchQuery.value
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item{Spacer(modifier = Modifier.height(8.dp))}
                    item{
                        TitleText("Contacts" + " ( " +groupedContacts.values.sumOf { it.size }+ " )")
                    }

                    val limitedContacts = groupedContacts.values.flatten().take(MAX_CONTACTS_SEARCH)
                    item{
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, BlueLight)
                        ){
                            Column(modifier = Modifier.padding(8.dp)) {
                                limitedContacts.forEachIndexed { index, contact ->
                                    val phoneNumber = contact.phoneNumbers.firstOrNull() ?: "Unknown"
                                    val fullName = contact.fullName ?: "Unknown"
                                        Row {
                                            HighlightedText(fullText = fullName,
                                                phoneNumber = phoneNumber,
                                                query = searchQuery.value,
                                                onClick = {navController.navigate(Screen.ConversationScreen.withArgs(fullName, phoneNumber))}
                                            ,index = index)
                                        }
                                }
                                if (groupedContacts.values.flatten().size > MAX_CONTACTS_SEARCH){
                                    ViewMoreText {
                                        navController.navigate(
                                            Screen.FilteredContactScreen.withArgs(
                                                searchQuery.value
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item{Spacer(modifier = Modifier.height(8.dp))}

                    item{
                        TitleText( "Conversations " + "( " + conversations.size.toString()+" )")
                    }


                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp, 2.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, BlueLight)
                        ){

                            Column(modifier = Modifier.padding(8.dp)) {
                                conversations.take(MAX_CONVERSATIONS_SEARCH).forEachIndexed{
                                    index ,conversationWithMessages ->
                                    ConversationItemSearch(conversationWithMessages, navController, index,searchQuery.value)
                                }
                                if(conversations.size > MAX_CONVERSATIONS_SEARCH)
                                {
                                        ViewMoreText{navController.navigate(Screen.FilteredConversationScreen.withArgs(searchQuery.value))}
                                }
                            }
                        }
                    }
                }
            }
        }

        @Composable
        fun TitleText(title : String){

            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyLarge.fontFamily
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

        }

        @Composable
        fun ViewMoreText( onClick :()-> Unit  ){
            Text(
                text = "View more",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,

                color = Color.Blue,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        @Composable
        fun MessageItem(message: Message, navController: NavController,index: Int,
                        query: String?, highlightColor: Color = HIGHLIGHTCOLOR

        ){

            val fullText = message.content
            val annotatedString = generateAnnotatedString(fullText, query!!, highlightColor)
            val iconTint = iconColors[index % iconColors.size]
            Column {
                Row {
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
                                        message.from_id,
                                        message.to_id!!
                                    )
                                )
                            }
                    )
                    Column {
                        Text(message.from_id,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                                fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyLarge.fontFamily

                        )
                        Row {
                         Column()
                         {
                                Text(annotatedString,
                                softWrap = true,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyLarge.fontFamily,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp, end = 24.dp)
                                        .clickable {
                                            navController.navigate(
                                                Screen.ConversationScreen.withArgs(
                                                    message.from_id,
                                                    message.to_id!!
                                                )
                                            )
                                        }
                                )
                           GrayDivider()
                            }
                        }
                    }

                }

            }

        }




        @Composable
        fun SearchesItem(search: SearchQuery,  searchQuery: MutableState<String>,searchViewModel: SearchViewModel,
                         contactsViewModel: ContactsViewModel,conversationViewModel: ConversationViewModel ,onDelete: () -> Unit,onSearchClickFalse: () -> Unit) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .clickable {
                            searchQuery.value = search.term
                            updateLists(
                                searchViewModel,
                                contactsViewModel,
                                conversationViewModel,
                                searchQuery
                            )
                            onSearchClickFalse()

                        }
                    ) {
                        Text(text = search.term, style = MaterialTheme.typography.body1)
                        Text(
                            text = DatesManager.convertTimestampToString(search.dateCreated),
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Delete")
                    }
                }
            }
        }

        @Composable
        fun HighlightedText(fullText: String,phoneNumber : String , query: String,
                            highlightColor: Color = HIGHLIGHTCOLOR,
                            index: Int,
                            onClick: () ->Unit) {

            val annotatedString = generateAnnotatedString(fullText, query, highlightColor)
            val annotatedStringNumber = generateAnnotatedString(fullText =phoneNumber,query, highlightColor)
            val iconTint = iconColors[index % iconColors.size]
            Row (modifier = Modifier.padding(12.dp)){

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    tint = White,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = iconTint,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                        .clickable {
                            onClick()
                        }
                )
                Column(Modifier.clickable {
                    onClick()
                }) {
                    Text(annotatedString,
                        style = TextStyle(
                            color = Black,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyLarge.fontFamily
                        )
                    )
                    Text(annotatedStringNumber,
                        style = TextStyle(
                            color = Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = androidx.compose.material3.MaterialTheme.typography.bodyLarge.fontFamily))

                    GrayDivider()
                }
            }
        }



        @Composable
        fun ConversationItemSearch(
            conversationWithMessages: ConversationWithMessages,
            navController: NavController,
            index : Int,
            query:String?,
            highlightColor: Color = HIGHLIGHTCOLOR

            ) {
            val conversation = conversationWithMessages.conversation
            val latestMessage = conversationWithMessages.messages.lastOrNull()?.content ?: "No messages"
            val latestMessageTime = DatesManager.getLatestMessageDateFromMessages(conversationWithMessages.messages)
            val latestUnreadMessages = ConversationsManager.getNumberOfUnreadMessages(conversationWithMessages.messages)
            val fullText = conversation.from
            val annotatedString = generateAnnotatedString(fullText, query!!, highlightColor)

            val iconTint = iconColors[index % iconColors.size]

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
                        contentDescription = "User Icon",
                        tint = White,
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
                        Text(text = annotatedString,
                            style = MaterialTheme.typography.h6,
                        color = Black)
                        Text(text = latestMessage,
                            style = MaterialTheme.typography.body2,
                            maxLines = 1,
                            color = Black,
                            overflow = TextOverflow.Ellipsis)

                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(text = latestMessageTime,
                            style = MaterialTheme.typography.caption)
                        Spacer(modifier = Modifier.height(24.dp))
                        if (latestUnreadMessages!=0)
                            Box(
                                modifier = Modifier
                                    .size(24.dp) // Adjust size as needed
                                    .background(BlueLight, shape = CircleShape),
                                contentAlignment = Alignment.Center
                                )
                            {
                                Text(
                                    text = latestUnreadMessages.toString(),
                                    style = MaterialTheme.typography.caption,
                                    color = White
                                )
                            }
                    }


                }
                GrayDivider()
            }
        }

        @Composable
        fun GrayDivider(){
            Divider(
                color = Color.Gray,
                thickness = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

        }
        @Composable
        fun DraggableTopSheetSearch(navController: NavController,
                                    searchQuery: MutableState<String>,
           searchViewModel: SearchViewModel,
                                    contactsViewModel: ContactsViewModel,
                                    conversationViewModel: ConversationViewModel,
                                    onSearchClick: () -> Unit,
                                    onSearchClickFalse:() -> Unit
      ) {
            var sheetHeightFraction by remember { mutableStateOf(0.2f) }
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current
            val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
            val coroutineScope = rememberCoroutineScope()
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
                                    sheetHeightFraction =
                                        if (sheetHeightFraction < 0.15f) 0.1f else 0.2f
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                val newHeightPx =
                                    (sheetHeightFraction * screenHeightPx + dragAmount)
                                        .coerceIn(0.1f * screenHeightPx, 0.2f * screenHeightPx)

                                sheetHeightFraction = newHeightPx / screenHeightPx
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sheetHeightFraction > 0.15f) {
                        Text(
                            text = "Search",
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
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { navController.popBackStack() }
                        )

                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = { searchQuery.value = it
                                  updateLists(searchViewModel,contactsViewModel, conversationViewModel,searchQuery)
                                   onSearchClickFalse()
                                updateLists(
                                    searchViewModel,
                                    contactsViewModel,
                                    conversationViewModel,
                                    searchQuery
                                )
                                            },
                            placeholder = { Text("Search...") },
                            singleLine = true,
                            shape = CircleShape,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        )

                        Icon(Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    onSearchClick()
                                }
                        )
                    }
                }
            }
        }
        private fun updateLists(searchViewModel: SearchViewModel,
                                contactsViewModel: ContactsViewModel,
                                conversationViewModel: ConversationViewModel,
                                searchQuery: MutableState<String>,
                                ){
            searchViewModel.addSearchQuery(searchQuery.value)
            searchViewModel.updateQueries()
            contactsViewModel.filterContacts(searchQuery.value)
            conversationViewModel.updateFilteredMessages(searchQuery.value)
            conversationViewModel.updateFilteredConversations(searchQuery.value)
        }

        private fun generateAnnotatedString(fullText: String, query: String, highlightColor: Color) : AnnotatedString{
            return  buildAnnotatedString {
                val startIndex = fullText.lowercase().indexOf(query.lowercase())
                if (startIndex != -1) {
                    append(fullText.substring(0, startIndex))
                    withStyle(style = SpanStyle(background = highlightColor)) {
                        append(fullText.substring(startIndex, startIndex + query.length))
                    }
                    append(fullText.substring(startIndex + query.length))
                } else {
                    append(fullText)
                }
            }
        }

    }


}