package com.yarchike.work_3_1


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.yarchike.work_3_1.api.API
import com.yarchike.work_3_1.api.AuthRequestParams
import com.yarchike.work_3_1.api.CreatePostRequest
import com.yarchike.work_3_1.api.RegistrationRequestParams
import com.yarchike.work_3_1.api.interceptor.InjectAuthTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object Repository {

    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl("http://93.179.85.126:5050/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://93.179.85.126:5050/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        api = retrofit.create(API::class.java)
    }

    private var api: API =
        retrofit.create(API::class.java)


    suspend fun authenticate(login: String, password: String) = api.authenticate(
        AuthRequestParams(login, password)
    )

    suspend fun register(login: String, password: String) =
        api.register(
            RegistrationRequestParams(
                login,
                password
            )
        )
    suspend fun getPosts() = api.getPosts()
    suspend fun likedByMe(id: Long) = api.likedByMe(id)

    suspend fun cancelMyLike(id: Long) = api.cancelMyLike(id)
    suspend fun createPost(content: String) = api.createPost(CreatePostRequest(content = content))
}
