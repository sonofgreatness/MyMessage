package project.mymessage.util.operationalstates

sealed class SendMessageState(status : String?) {

    object  Loading  : SendMessageState(null)
    object  Success : SendMessageState("Message sent")
    data class Error(val exceptionMessage: String? = null) : SendMessageState(status = exceptionMessage)
}