package com.yarchike.work_3_1

import com.yarchike.work_3_1.api.*
import com.yarchike.work_3_1.dto.PostModel
import retrofit2.Response

class NetworkRepository(private val api: API): Repository {

    override suspend fun authenticate(login: String, password: String): Response<Token> =
        api.authenticate(AuthRequestParams(username = login, password = password))

    override suspend fun register(login: String, password: String): Response<Token> =
        api.register(RegistrationRequestParams(username = login, password = password))

    override suspend fun getPosts(): Response<List<PostModel>> =
        api.getPosts()

    override suspend fun likedByMe(id: Long): Response<PostModel> =
        api.likedByMe(id)

    override suspend fun cancelMyLike(id: Long): Response<PostModel> =
        api.cancelMyLike(id)

    override suspend fun createPost(content: String): Response<Void> =
        api.createPost(CreatePostRequest(content = content))

    override suspend fun createRepost(content: String, contentRepost: PostModel): Response<Void> =
        api.createRepost(CreateRepostRequest(postResurse = content, repostResurs = contentRepost))

    override suspend fun getPostsAfter(id: Long): Response<List<PostModel>> =
        api.getPostsAfter(id)



}