package com.vetpet.petbeats.ui.home_clinic.news_post

sealed class NewsPostClinicEvent {
    object NavigationNewsClinic: NewsPostClinicEvent()

    data class NavigationNewsSuccessClinic(val id: Int): NewsPostClinicEvent()
}