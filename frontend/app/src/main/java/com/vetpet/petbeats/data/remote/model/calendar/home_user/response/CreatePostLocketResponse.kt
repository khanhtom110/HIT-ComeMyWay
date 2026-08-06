package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class CreatePostLocketResponse (
    val id: Int,
    val userId: Int,
    val username: String,
    val userAvatar: String,
    val imageUrl: String,
    val caption: String,
    val createdAt: String,
)