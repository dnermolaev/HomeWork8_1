interface Attachment {
    val type: String
    val attachment: AttachmentType
}

interface AttachmentType {
    val id: Int
    val owner_id: Int
    val title: String
    val description : String
}