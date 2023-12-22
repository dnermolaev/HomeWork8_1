import NotesService.notes

data class Chat(
    var chatId: Int = 0,
    var read: Boolean = false,
    var message: Message,

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
    var messagesToDisplay = mutableListOf<Message>()

    fun createMessage(chatId: Int, msg: Message): Int {
        for (chat in chats) {
            if (chat.chatId == chatId) {
                messages.add(msg.copy(msgId = ++messagesId, parentChatId = chat.chatId))
                return messages.last().msgId
            }
        }
        throw ChatNotFoundException("no chat with such ID $chatId")
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

    fun createChat(userId: Int, chat: Chat): Int {
        chats.add(chat.copy(chatId = ++chatsId))
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
                if (!chat.message.read) chat.read = false
                break
            }
            return chat.read
        }
        return chats.filter(predicate).size
    }

    fun getChats() {
        for (chat in chats)
            println(chat.chatId)
    }

    fun getLastMessages(chatId: Int): Int {
        val predicate = fun(msg: Message): Boolean {
            return (messages.size - msg.msgId < 4)
        }

        for (chat in chats) {
            if (chat.chatId == chatId) {
                messages.filter(predicate)
            }
            return 1
        }
        throw ChatNotFoundException("no note with such ID $chatId")
    }

    fun getMessagesList(chatId: Int, cmpnID: Int): Int {
        for (chat in chats) {
            if (chat.chatId == chatId) {
                for (message in messages) {
                    if (message.companionId == cmpnID) {
                        message.read = true
                        messagesToDisplay.add(message)
                    }
                }
                return 1
            }
        }
        throw MessageNotFoundException("no message with such companionID $cmpnID")
    }

}