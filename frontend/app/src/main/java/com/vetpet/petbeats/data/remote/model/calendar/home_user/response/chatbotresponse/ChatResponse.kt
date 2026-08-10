package com.vetpet.petbeats.data.remote.model.calendar.home_user.response.chatbotresponse

data class ChatResponse (
    val aiResponse: String?,
    val suggestedServiceName: String?,
    val recommendedClinics: List<RecommendClinic>?,
)