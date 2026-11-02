package com.vetpet.petbeats.data.remote.dto.home_user.response

data class AddFriendResponse (
    val friendshipId: Int,
    val friendId: Int,
    val username: String,
    val avatar: String,
)