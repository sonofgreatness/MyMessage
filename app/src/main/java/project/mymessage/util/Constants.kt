package project.mymessage.util

import androidx.compose.ui.graphics.Color
import project.mymessage.ui.theme.*

class Constants {
    companion object {
        const val LOCAL_DATABASE_NAME="MyMessageDatabase"
        const val MYMESSAGE_TAG="MyMessageLogger"
        const val GITHUB_API = "https://api.github.com/"
        const val GITHUB_REPO_URL = "repos/sonofgreatness/MyMessage/releases/latest"
        const val MAX_MESSAGES_SEARCH=8
        const val MAX_CONTACTS_SEARCH=3
        const val MAX_CONVERSATIONS_SEARCH=2
     val iconColors=listOf(
            Grey300,
            RedLight,
            BlueLight,
            GreenLight,
              Pink40
        )
        val HIGHLIGHTCOLOR=Color.Yellow

    }
}