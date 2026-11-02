package com.vetpet.petbeats.data.remote.dto.home_user.response

data class MyPostLocketResponse (
    val content: List<com.vetpet.petbeats.data.remote.dto.home_user.response.CreatePostLocketResponse>,
    val hasNext: Boolean,
    val lastPostId: Int,
)