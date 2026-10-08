package com.vetpet.petbeats.ui.home_user.news.adapter

data class NewsChild (
    val id: Int,
    val clinic: Int,
    val clinicName: String,
    val clinicAvatarUrl: String?,
    val titleClinic: String,
    val excerpt: String?,
    val imageUrl: String?,
)