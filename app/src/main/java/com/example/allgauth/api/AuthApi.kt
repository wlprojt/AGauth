package com.example.allgauth.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header

interface AuthApi {
    @POST("/api/auth/google")
    suspend fun googleLogin(@Body body: GoogleLoginRequest): GoogleLoginResponse

    @GET("/api/auth/me")
    suspend fun me(@Header("Authorization") bearer: String): MeResponse
}
