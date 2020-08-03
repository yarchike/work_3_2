package com.yarchike.work_3_1.Post

data class PostRequestDto(
    val id: Long,
    val autor: String? = null,
    val postResurse: String? = null,
    var like: Int = 0,
    var isLike: Boolean = false
)