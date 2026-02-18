package com.example.allgauth

data class UserResponse(
    val user: User
)

data class User(
    val name: String,
    val email: String,
    val image: String?
)


data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: User
)
