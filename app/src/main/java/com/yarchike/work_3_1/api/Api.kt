package com.yarchike.work_3_1.api

import com.google.gson.annotations.SerializedName
import com.yarchike.work_3_1.dto.PostModel
import retrofit2.Response
import retrofit2.http.*

data class AuthRequestParams(val username: String, val password: String)

data class Token(val token: String)

data class RegistrationRequestParams(val username: String, val password: String)
data class CreatePostRequest(val id: Long = 0, val content: String)
data class CreateRepostRequest(val id: Long = 0, val postResurse: String, val repostResurs:PostModel )


// тип поста автоматически определяется на базе sourceId и link
data class PostRequest(
    val id: Long = 0, // 0 - новый, !0 - редактируем существующий, если есть права
    val sourceId: Long? = null, // !null - если репостим
    val content: String? = null,
    val link: String? = null, // например, ссылка на Youtube
    val attachmentId: String? = null // id вложения, если есть
)


interface API {
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>
    @GET("api/v1/posts/recent")
    suspend fun getPosts(): Response<List<PostModel>>
    @POST("api/v1/posts/{id}/likes")
    suspend fun likedByMe(@Path("id") id: Long): Response<PostModel>
    @DELETE("api/v1/posts/{id}/likes")
    suspend fun cancelMyLike(@Path("id") id: Long): Response<PostModel>
    @POST("api/v1/posts")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest): Response<Void>
    @POST("api/v1/repost")
    suspend fun createRepost(@Body createRepostRequest: CreateRepostRequest): Response<Void>
    @POST("api/v1/posts/After")
    suspend fun getPostsAfter(@Body id:Long): Response<List<PostModel>>
    @POST("api/v1/posts/old")
    suspend fun getPostsOld(@Body id:Long): Response<List<PostModel>>

}