package com.vetpet.petbeats.ui.home_user.chatbot

import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.chatbotresponse.RecommendClinic

sealed class ChatbotEvent {
    data class UserMessage(val message: String) : ChatbotEvent()
    data class BotMessage(val message: String) : ChatbotEvent()


    data class BotSuggest(val message: String, val clinic: List<RecommendClinic>) : ChatbotEvent()


    //Trạng thái chờ
    object BotTyping: ChatbotEvent()


}