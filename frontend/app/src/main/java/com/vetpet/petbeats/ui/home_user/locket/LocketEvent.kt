package com.vetpet.petbeats.ui.home_user.locket

sealed class LocketEvent {
    object NavigationListFriendLocket: LocketEvent()
    object NavigationMeLocket: LocketEvent()
    object NavigationEverybodyLocket: LocketEvent()
}