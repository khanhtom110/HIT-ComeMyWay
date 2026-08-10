package com.vetpet.petbeats.ui.home_user.chatbot.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vetpet.petbeats.ui.home_user.chatbot.ChatbotEvent

class ChatbotDiffcallback: DiffUtil.ItemCallback<ChatbotEvent>() {
    override fun areItemsTheSame(old: ChatbotEvent, new: ChatbotEvent): Boolean {
        return old == new
    }

    override fun areContentsTheSame(old: ChatbotEvent, new: ChatbotEvent): Boolean {
        return old == new
    }

}