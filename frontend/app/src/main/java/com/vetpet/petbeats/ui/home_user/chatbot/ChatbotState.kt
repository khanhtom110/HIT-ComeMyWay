package com.vetpet.petbeats.ui.home_user.chatbot

import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild

data class ChatbotState (
    val chatUser: String = "",
    val isChatUser: Boolean = false,
    val isLogo: Boolean = false,
)