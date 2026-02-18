package com.example.allgauth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/google")
    suspend fun googleLogin(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @GET("api/auth/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserResponse>
}
