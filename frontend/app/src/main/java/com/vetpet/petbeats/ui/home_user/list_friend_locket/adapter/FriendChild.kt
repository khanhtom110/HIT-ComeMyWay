package com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter

data class FriendChild (
    val friendshipId: Int,
    val friendId: Int,
    val username: String,
    val avatar: String?,

    val isMakeFriend: Boolean = false,
)