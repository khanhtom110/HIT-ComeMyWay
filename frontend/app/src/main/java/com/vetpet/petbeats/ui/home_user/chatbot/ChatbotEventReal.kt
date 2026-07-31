package com.vetpet.petbeats.ui.home_user.chatbot

sealed class ChatbotEventReal {
    data class NavigaitonInformation(val id: Int): ChatbotEventReal()
}