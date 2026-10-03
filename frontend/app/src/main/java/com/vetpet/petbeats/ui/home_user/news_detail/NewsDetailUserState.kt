package com.vetpet.petbeats.ui.home_user.news_detail

import com.google.ai.client.generativeai.type.Content

data class NewsDetailUserState (
    val imageClinic: String = "",
    val nameClinic: String = "",
    val title: String = "",
    val imageNews: String = "",
    val content: String = ""
)