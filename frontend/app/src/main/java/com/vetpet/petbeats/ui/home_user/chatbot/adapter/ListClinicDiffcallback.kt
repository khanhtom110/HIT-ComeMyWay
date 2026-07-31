package com.vetpet.petbeats.ui.home_user.chatbot.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vetpet.petbeats.data.remote.model.calendar.home_user.response.chatbotresponse.RecommendClinic

class ListClinicDiffcallback: DiffUtil.ItemCallback<RecommendClinic>() {
    override fun areItemsTheSame(old: RecommendClinic, new: RecommendClinic): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: RecommendClinic, new: RecommendClinic): Boolean {
        return old == new
    }
}