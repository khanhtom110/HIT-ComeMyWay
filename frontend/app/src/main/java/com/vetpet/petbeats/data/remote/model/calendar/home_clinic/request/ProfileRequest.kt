package com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request

import com.google.gson.annotations.SerializedName


data class ProfileRequest (
    val name: String,
    val address: String,
    val mapLink: String,
    val phone: String,
    val description: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    val openTime: String,
    val closeTime: String,
    val services: List<String>
)