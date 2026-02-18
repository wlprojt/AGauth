package com.example.allgauth.api

data class GoogleLoginRequest(val idToken: String, val platform: String = "android")

data class GoogleLoginResponse(
    val success: Boolean,
    val token: String?,      // ✅ add this in backend response
    val user: UserDto?
)

data class UserDto(
    val _id: String,
    val name: String,
    val email: String,
    val image: String?
)

data class MeResponse(val user: UserDto)
