package com.yarchike.work_3_1

import android.app.Application
import android.content.Context
import com.yarchike.work_3_1.api.API
import com.yarchike.work_3_1.api.interceptor.InjectAuthTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {

        lateinit var repository: Repository
            private set
    }

    override fun onCreate() {
        super.onCreate()

        val httpLoggerInterceptor = HttpLoggingInterceptor()
        // Указываем, что хотим логировать тело запроса.
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor {
                getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
                    AUTHENTICATED_SHARED_KEY, null
                )
            })
            .addInterceptor(httpLoggerInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://server-martynov.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //создаем API на основе нового retrofit-клиента
        val api = retrofit.create(API::class.java)

        repository = NetworkRepository(api)
    }
}