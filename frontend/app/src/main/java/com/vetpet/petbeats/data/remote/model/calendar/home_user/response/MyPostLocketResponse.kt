package com.vetpet.petbeats.data.remote.model.calendar.home_user.response

data class MyPostLocketResponse (
    val content: CreatePostLocketResponse,
    val hasNext: Boolean,
    val lastPostId: Int,
)