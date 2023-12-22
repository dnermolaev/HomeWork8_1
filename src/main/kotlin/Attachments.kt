class AudioAttachment(override val type: String = "Audio", override val attachment: Audio) : Attachment {

}

class VideoAttachment(override val type: String = "Video", override val attachment: Video) : Attachment {
}

class PhotoAttachment(override val type: String = "Photo", override val attachment: Photo) : Attachment {
}

class Audio(
    override val id: Int,
    override val owner_id: Int,
    override val title: String,
    override val description: String
) : AttachmentType {

}

class Video(
    override val id: Int,
    override val owner_id: Int,
    override val title: String,
    override val description: String
) : AttachmentType {

}

class Photo(
    override val id: Int,
    override val owner_id: Int,
    override val title: String,
    override val description: String
) : AttachmentType {

}

class File(
    override val id: Int,
    override val owner_id: Int,
    override val title: String,
    override val description: String
) : AttachmentType{

}

class Link(
    override val id: Int,
    override val owner_id: Int,
    override val title: String,
    override val description: String
) : AttachmentType{

}

