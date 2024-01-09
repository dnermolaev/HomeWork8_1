data class Chat(
    var chatId: Int = 0,
    var companionId: Int = 0,
    val messages: MutableList<Message> = mutableListOf()
)  {
}

data class Message(
    var read: Boolean = false,
    var msgId: Int = 0,
    var parentChatId: Int = 0,
    var text: String
) {
}

class ChatNotFoundException(message: String) : RuntimeException(message)
class MessageNotFoundException(message: String) : RuntimeException(message)

object chatService {

    var messages = mutableListOf<Message>()
    var messagesId: Int = 0
    var chats = mutableListOf<Chat>()
    var chatsId: Int = 0


    fun createMessage(msg: Message, userId: Int): Int {

        for (chat in chats) {
            if (chat.companionId == userId) {
                messages.add(msg.copy(msgId = ++messagesId, parentChatId = chat.chatId))
                return messages.last().msgId
            } else {
                var cId = createChat(Chat(companionId = userId))
                messages.add(msg.copy(msgId = ++messagesId, parentChatId = cId))
            }
        }
        return messages.last().msgId
    }

    fun editMessage(msg: Message, messaggeId: Int): Int {
        for ((index, message) in messages.withIndex()) {
            if (msg.msgId == message.msgId) {
                messages[index] = msg.copy()
                return 1
            }
        }
        throw MessageNotFoundException("no message with such ID $messaggeId")
    }

    fun deleteMessage(messageId: Int): Int {
        for (message in messages) {
            if (message.msgId == messageId) {
                messages.remove(message)
                return 1
            }
        }
        throw MessageNotFoundException("no message with such ID $messageId")
    }

    private fun createChat(chat: Chat): Int {
        chats.add(chat.copy(companionId = chat.companionId, chatId = ++chatsId))
        return chats.last().chatId
    }

    fun deleteChat(chatId: Int): Int {
        for (chat in chats) {
            if (chat.chatId == chatId) {
                chats.remove(chat)
                for (message in messages) {
                    if (chatId == message.parentChatId) {
                        messages.remove(message)
                    }
                }
                return 1
            }
        }
        throw ChatNotFoundException("no note with such ID $chatId")
    }

    fun getUnreadChatsCount() = chats.count { it -> it.messages.any{!it.read} }

    @JvmName("getChats1")
    fun getChats(): List<Chat> {
        return chats
    }

    fun getLastMessages(): List<String> = chats.map { it.messages.lastOrNull()?.text ?: "no messages" }

    fun getMessagesList(cmpnID: Int, count: Int): List<Message> {
        val chat = chats.asReversed()
            .asSequence()
            .find { it.companionId == cmpnID  }
            ?: throw ChatNotFoundException("Чат не найден")

                return chat.messages
                .take(count)
                .onEach { it.read = true }
    }
}

