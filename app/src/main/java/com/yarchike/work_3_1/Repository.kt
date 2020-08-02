package com.yarchike.work_3_1

import com.yarchike.work_3_1.Repository.authenticate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.yarchike.work_3_1.api.API
import com.yarchike.work_3_1.api.AuthRequestParams
import com.yarchike.work_3_1.api.RegistrationRequestParams


object Repository {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://93.179.85.126:5050/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val api: API by lazy {
        retrofit.create(API::class.java)
    }

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
}
