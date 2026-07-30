package com.vetpet.petbeats.ui.auth.changepassword_clinic

sealed class ChangePasswordEvent {
    object NavigationChangeSuccess: ChangePasswordEvent()
}