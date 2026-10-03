package com.vetpet.petbeats.ui.home_user.news

sealed class NewsUserEvent {
    data class NavigaitonNewsUser(val id: Int): NewsUserEvent()
}