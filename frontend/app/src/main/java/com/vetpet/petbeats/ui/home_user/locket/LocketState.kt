package com.vetpet.petbeats.ui.home_user.locket

data class LocketState (
    val message: String = "",
    val linkImage: String = "",


    val isDown: Boolean = false,
    val isFlash: Boolean = false,
    val isSendSuccess: Boolean = false,
    val isLoading: Boolean = false,
)