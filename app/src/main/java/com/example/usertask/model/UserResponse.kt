package com.example.usertask.model

data class UserResponse(
    val data: List<User>
)

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

