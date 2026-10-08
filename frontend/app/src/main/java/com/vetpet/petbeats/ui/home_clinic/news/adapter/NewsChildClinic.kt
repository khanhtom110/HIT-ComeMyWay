package com.vetpet.petbeats.ui.home_clinic.news.adapter

data class NewsChildClinic (
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val title: String,
    val content: String,
    val imageUrls: String,
    val imageClinic: String,
    val status: String?,
    val approveBy: Int?,
    val approveAt: String?,
    val createAt: String?
)