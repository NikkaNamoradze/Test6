package com.example.test6.network

import com.example.test6.model.login.LoginResponse
import com.example.test6.model.login.LoginSend
import com.example.test6.model.registration.RegistrationResponse
import com.example.test6.model.registration.RegistrationSend
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object RetrofitClient {

    private const val BASE_URL = "https://reqres.in/api/"

    private val retrofitBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofitBuilder.create(AuthService::class.java)
    }

}

interface AuthService {

    @POST("login")
    suspend fun logIn(@Body body: LoginSend): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body body: RegistrationSend): Response<RegistrationResponse>
}