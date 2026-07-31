package com.vetpet.petbeats.ui.home_user.chatbot

import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild

sealed class ChatbotEvent {
    data class UserMessage(val message: String) : ChatbotEvent()
    data class BotMessage(val message: String) : ChatbotEvent()


    data class BotSuggest(val message: String, val clinic: List<BookChild>) : ChatbotEvent()


    //Trạng thái chờ
    object BotTyping: ChatbotEvent()
}