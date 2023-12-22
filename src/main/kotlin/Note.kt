data class Note(
    var nid: Int = 0,
    val title: String,
    val text: String,
    val privacy: Int = 0,
    val commentPrivacy: Int = 0,
    var comment: Comment
) {

}

class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)

object NotesService {
    var notes = mutableListOf<Note>()
    var notesId: Int = 0
    var comments = mutableListOf<Comment>()
    var commentsId: Int = 0
    var deletedComments = mutableListOf<Comment>()

    fun add(note: Note): Int {
        notes.add(note.copy(nid = ++notesId))
        return notes.last().nid
    }

    fun createComment(noteId: Int, comment: Comment): Int {
        for (note in notes) {
            if (note.nid == noteId) {
                comments.add(comment.copy(cid = ++commentsId, parentNoteId = note.nid))
                return comments.last().cid
            }
        }
        throw NoteNotFoundException("no note with such ID $noteId")
    }

    fun deleteNote(noteId: Int): Int {
        for (note in notes) {
            if (note.nid == noteId) {
                notes.remove(note)
                for (comment in comments) {
                    if (noteId == comment.parentNoteId) {
                        deletedComments.add(comment)
                        comments.remove(comment)
                    }
                }
                return 1
            }
        }
        throw NoteNotFoundException("no note with such ID $noteId")
    }

    fun deleteComment(commentId: Int): Int {
        for (comment in comments) {
            if (comment.cid == commentId) {
                deletedComments.add(comment)
                comments.remove(comment)
                return 1
            }
        }
        throw CommentNotFoundException("no comment with such ID $commentId")
    }

    fun editNote(newNote: Note): Int {
        for ((index, note) in notes.withIndex()) {
            if (note.nid == newNote.nid) {
                notes[index] = newNote.copy()
                return 1
            }
        }
        throw NoteNotFoundException("no note with such ID $newNote.nid")
    }

    fun editComment(newComment: Comment): Int {
        for ((index, comment) in comments.withIndex()) {
            if (comment.cid == newComment.cid) {
                comments[index] = newComment.copy()
                return 1
            }
        }
        throw CommentNotFoundException("no note with such ID $newComment.cid")
    }

    fun getNotesById(vararg notes_ids: Int): MutableList<Note> {
        var notesToDisplay = mutableListOf<Note>()
        for (note in notes) {
            if (note.nid in notes_ids) {
                notesToDisplay.add(note)
            }
        }
        if (notesToDisplay.isEmpty()) {
            throw NoteNotFoundException("no note with such ID $notes_ids")
        }
        return notesToDisplay
    }

    fun getNoteComments(noteId: Int): MutableList<Comment> {
        var commentsToDisplay = mutableListOf<Comment>()
        for (note in notes) {
            if (note.nid == noteId) {
                commentsToDisplay.add(note.comment)
            }
        }
        if (commentsToDisplay.isEmpty()) {
            throw NoteNotFoundException("no note with such ID $noteId")
        }
        return commentsToDisplay
    }

    fun restoreComment(commentId: Int): Int {
        for ((index, comment) in deletedComments.withIndex()) {
            if (comment.cid == commentId) {
                comments.add(deletedComments[index])
                deletedComments.removeAt(index)
                return 1
            }
        }
        throw CommentNotFoundException("no comment with such ID $commentId")
    }

}

