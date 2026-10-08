package com.vetpet.petbeats.ui.home_clinic.news_post

data class NewsPostClinicState (
    val id: Int = 0,
    val imageClinic: String = "",
    val imageNews: String = "",
    val nameClinic: String = "",


    val title: String = "",
    val content: String = "",


    val isImageNews: Boolean = false,
    val isTitle: Boolean = false,
    val isContent: Boolean = false
)