package com.yarchike.work_3_1.Post

data class Post(
    val id: Int,
    val date: Long,
    val autor: String,
    val postResurse: String,
    var like: Int = 0,
    val comments: Int = 0,
    val share: Int = 0,
    var isLike: Boolean,
    val isComment: Boolean,
    val isShare: Boolean,
    val adress: String,
    val coordinates: Pair<Double, Double>,
    val type: PostTypes,
    val url: String? = null,
    val dateRepost: Long? = null,
    val autorRepost: String? = null,
    var hidePost: Boolean = false,
    var viewsPost: Long = 0,
    val autorId: Long = -1,
    val postIsLike: ArrayList<Long> = ArrayList()

)