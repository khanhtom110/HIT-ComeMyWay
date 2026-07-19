package com.vetpet.petbeats.ui.auth.home

sealed class HomeEvent {
    object NavigationLogin: HomeEvent()
    object NavigationRegister: HomeEvent()
}