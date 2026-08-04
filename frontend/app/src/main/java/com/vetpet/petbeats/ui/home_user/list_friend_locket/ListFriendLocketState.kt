package com.vetpet.petbeats.ui.home_user.list_friend_locket

import com.vetpet.petbeats.ui.home_user.list_friend_locket.adapter.FriendChild

data class ListFriendLocketState (
    val pendingFriend: List<FriendChild> = emptyList(),
    val myFriend: List<FriendChild> = emptyList(),
    val makeFriend: List<FriendChild> = emptyList(),


    val searchFriend: String = "",


    val isSearch: Boolean = false,
    val isShowSearch: Boolean = false,
)