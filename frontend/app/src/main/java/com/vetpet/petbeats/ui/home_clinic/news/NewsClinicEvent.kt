package com.vetpet.petbeats.ui.home_clinic.news


sealed class NewsClinicEvent {
    data class NavigationClinicPost(val id: Int): NewsClinicEvent()
}