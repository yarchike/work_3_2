package com.yarchike.work_3_1.dto

enum class AttachmentType {
    IMAGE, AUDIO, VIDEO
}

data class AttachmentModel(val id: String, val url: String, val type: AttachmentType)

enum class PostType {
    POST, REPOST
}

data class PostModel(
    val id: Int,
    val date: Long,
    val autor: String,
    val postResurse: String,
    var like: Int = 0,
    val comments: Int = 0,
    val share: Int = 0,
    var isLike: Boolean =false,
    val isComment: Boolean=false,
    val isShare: Boolean=false,
    val adress: String,
    val coordinates: Pair<Double, Double>,
    val type: PostTypes = PostTypes.POST,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewsPost: Long = 0
)
 {
    var likeActionPerforming = false
    fun updateLikes(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        like = updatedModel.like
        like = updatedModel.like
    }
}
