package com.vetpet.petbeats.ui.home_user.news

import com.vetpet.petbeats.ui.home_user.news.adapter.NewsChild

data class NewsUserState (
    val lastPostId: Int? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,

    val listNews: List<NewsChild> = emptyList()
)