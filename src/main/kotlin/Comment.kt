data class Comment (
    var cid: Int = 0,
    val fromId: Int = 0,
    val date: Int = 0,
    var text: String,
    var parentNoteId: Int = 0
){
}

