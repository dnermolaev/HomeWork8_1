data class Post(
    var id: Int = 0,
    val ownerId: Int = 0,
    val createdBy: Int = 0,
    val date: Int = 0,
    var text: String,
    val canPin: Boolean = true,
    val canDelete: Boolean = true,
    val isPinned: Boolean = false,
    val markedAsAds: Boolean = false,
    val original: Post? = null,
    var likes: Like = Like(),
    var comment: Comment? = null
) {
    data class Like(
        val count: Int = 0,
        val user_likes: Boolean = false,
        val can_like: Boolean = true,
        val can_publish: Boolean = false
    ) {
    }
}

class PostNotFoundException(message: String) : RuntimeException(message)

object WallService {
    var posts = emptyArray<Post>()
    var postId: Int = 0
    var comments = emptyArray<Comment>()

    fun createComment(postId: Int, comment: Comment): Comment {
            for (post in posts) {
                if (post.id == postId) {
                    comments += comment.copy()
                    return comments.last()
                }
            }
        throw PostNotFoundException ("no post with such ID $postId")
    }

    fun clearPosts() {
        posts = emptyArray()
        postId = 0
    }

    fun clearComments() {
        comments = emptyArray<Comment>()
        postId = 0
    }

    fun add(post: Post): Post {
        posts += post.copy(id = ++postId, likes = post.likes.copy())
        return posts.last()
    }

    fun update(newPost: Post): Boolean {
        for ((index, post) in posts.withIndex()) {
            if (post.id == newPost.id) {
                posts[index] = newPost.copy()
                println(posts[index])
                return true
            }
        }
        return false
    }
}


