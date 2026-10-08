package com.vetpet.petbeats.ui.home_clinic.news

import com.vetpet.petbeats.ui.home_clinic.news.adapter.NewsChildClinic

data class NewsClinicState (
    val clinicId: Int = 0,
    val id: Int = 0,


    val isLoading: Boolean = false,

    val listNews: List<NewsChildClinic> = emptyList()
)