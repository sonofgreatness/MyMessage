package project.mymessage.util

class Enums {

    enum class MessageType(val value: Int) {
        Incoming(1),
        Outgoing(2);
    }
    enum class MessageStatus {
        SENT, DELIVERED, DRAFT, ERROR
    }
    enum class ThemeMode{
        DAY , NIGHT, NOTSET
    }
}