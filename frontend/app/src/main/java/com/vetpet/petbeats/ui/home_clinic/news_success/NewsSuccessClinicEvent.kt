package com.vetpet.petbeats.ui.home_clinic.news_success

import com.vetpet.petbeats.ui.home_clinic.news_post.NewsPostClinicEvent

sealed class NewsSuccessClinicEvent {
    object NavigationNewsPost: NewsSuccessClinicEvent()
    object NavigationNews: NewsSuccessClinicEvent()
}