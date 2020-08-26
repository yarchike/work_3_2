package com.yarchike.work_3_1.dto

import com.yarchike.work_3_1.BASE_URL

enum class AttachmentType {
    IMAGE
}

data class AttachmentModel(val id: String, val mediaType: AttachmentType) {
    val url
        get() = "$BASE_URL/api/v1/static/$id"
}


data class PostModel(
    val id: Int,
    val date: Long,
    val autor: String,
    var postResurse: String? = null,
    var like: Int = 0,
    val comments: Int = 0,
    val share: Int = 0,
    var isLike: Boolean = false,
    val isComment: Boolean = false,
    var isShare: Boolean = false,
    val adress: String,
    val coordinates: Pair<Double, Double>,
    val type: PostTypes = PostTypes.POST,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewsPost: Long = 0,
    val repostResurs: PostModel? = null,
    val attachment: AttachmentModel?

) {
    var likeActionPerforming = false
    var repostActionPerforming = false
    fun updatePost(updatedModel: PostModel) {
        if (id != updatedModel.id) throw IllegalAccessException("Ids are different")
        like = updatedModel.like
        isLike = updatedModel.isLike
        postResurse = updatedModel.postResurse
        isShare = updatedModel.isShare
        isShare = updatedModel.isShare
    }
}
