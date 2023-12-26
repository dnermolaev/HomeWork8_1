data class Chat(
    var chatId: Int = 0,
    var read: Boolean = false,
    var companionId: Int = 0,
) {
}

data class Message(
    var companionId: Int = 0,
    var read: Boolean = false,
    var msgId: Int = 0,
    var parentChatId: Int = 0
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

    fun getUnreadChatsCount(chat: Chat): Int {
        val predicate = fun(chat: Chat): Boolean {
            for (message in messages) {
                if (!message.read && message.parentChatId == chat.chatId) {
                    return false
                }
            }
            return true
        }
        return chats.filter(predicate).size
    }

    @JvmName("getChats1")
    fun getChats(): List<Chat> {
        return chats
    }

    fun getLastMessages(chatId: Int): List<Message> {
        var messagesWithId = mutableListOf<Message>()

        val predicate = fun(msg: Message): Boolean {
            if (msg.parentChatId == chatId) {
                messagesWithId.add(msg.copy())
            }
            return (messagesWithId.size - msg.msgId < 4)
        }
        for (chat in chats) {
            if (chat.chatId == chatId) {
                return messages.filter(predicate)
            }

        }
        throw ChatNotFoundException("no chat with such ID $chatId")
    }

    fun getMessagesList(chatId: Int, cmpnID: Int): List<Message> {
        val chat = chats.find { it.chatId == chatId }
            ?: throw ChatNotFoundException("Чат не найден")

        val messagesToDisplay = messages
            .filter { it.parentChatId == chatId }
            .takeLast(cmpnID)
            .ifEmpty { throw MessageNotFoundException("no message with such companionID $cmpnID") }
            .onEach { it.read = true }

        return messagesToDisplay
    }
}

