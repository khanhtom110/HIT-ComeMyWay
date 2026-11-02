package com.vetpet.petbeats.data.remote.dto.home_user.response.chatbotresponse

data class ChatResponse (
    val aiResponse: String?,
    val suggestedServiceName: String?,
    val recommendedClinics: List<com.vetpet.petbeats.data.remote.dto.home_user.response.chatbotresponse.RecommendClinic>?,
)