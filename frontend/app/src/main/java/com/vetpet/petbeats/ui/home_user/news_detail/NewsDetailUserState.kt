package com.vetpet.petbeats.ui.home_user.news_detail

import com.google.ai.client.generativeai.type.Content

data class NewsDetailUserState (
    val id: Int = 0,
    val clinicId: Int = 0,
    val clinicName: String = "",
    val clinicAvatarUrl: String = "",
    val titleClinic: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val imageUrls: List<String> = emptyList()
)