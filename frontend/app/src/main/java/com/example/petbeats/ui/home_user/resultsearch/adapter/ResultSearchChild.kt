package com.example.petbeats.ui.home_user.resultsearch.adapter

data class ResultSearchChild (
    val id: Int,
    val roomName: String,
    val image: String,
    val isOperating: Boolean,
    val distance: Double, //khoảng cách
    val rating: Double,
    val address: String,
    val closeTime: String,
    val openTime: String,
)