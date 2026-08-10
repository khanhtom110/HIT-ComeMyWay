package com.vetpet.petbeats.ui.home_user.list_friend_locket

sealed class ListFriendLocketEvent {
    object NavigationLocket: ListFriendLocketEvent()

    data class CopyLink(val link: String): ListFriendLocketEvent()
}