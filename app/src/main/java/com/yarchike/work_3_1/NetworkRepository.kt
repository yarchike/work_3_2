package com.yarchike.work_3_1

import android.graphics.Bitmap
import com.yarchike.work_3_1.api.*
import com.yarchike.work_3_1.dto.AttachmentModel
import com.yarchike.work_3_1.dto.PostModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

class NetworkRepository(private val api: API): Repository {
    private var token: String? = null
    override suspend fun authenticate(login: String, password: String): Response<Token> {
        token = api.authenticate(AuthRequestParams(username = login, password = password)).body()?.token
     return api.authenticate(AuthRequestParams(username = login, password = password))
    }

    override suspend fun register(login: String, password: String): Response<Token> =
        api.register(RegistrationRequestParams(username = login, password = password))

    override suspend fun getPosts(): Response<List<PostModel>> =
        api.getPosts()

    override suspend fun likedByMe(id: Long): Response<PostModel> =
        api.likedByMe(id)

    override suspend fun cancelMyLike(id: Long): Response<PostModel> =
        api.cancelMyLike(id)

    override suspend fun createPost(content: String, attachmentModel:AttachmentModel?): Response<Void> =
        api.createPost(CreatePostRequest(postResurse = content, attachment = attachmentModel))

    override suspend fun createRepost(content: String, contentRepost: PostModel): Response<Void> =
        api.createRepost(CreateRepostRequest(postResurse = content, repostResurs = contentRepost))

    override suspend fun getPostsAfter(id: Long): Response<List<PostModel>> =
        api.getPostsAfter(id)

    override suspend fun getPostsOld(id: Long): Response<List<PostModel>> =
        api.getPostsOld(id)

    override suspend fun upload(bitmap: Bitmap): Response<AttachmentModel> {
        // Создаем поток байтов
        val bos = ByteArrayOutputStream()
        // Помещаем Bitmap в качестве JPEG в этот поток
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val reqFIle =
            // Создаем тип медиа и передаем массив байтов с потока
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), bos.toByteArray())
        val body =
        // Создаем multipart объект, где указываем поле, в котором
            // содержатся посылаемые данные, имя файла и медиафайл
            MultipartBody.Part.createFormData("file", "image.jpg", reqFIle)
        return api.uploadImage(body)
    }

    override suspend fun registerPushToken(token: String): Response<User> = api.registerPushToken(this.token!!, PushRequestParams(token))



}