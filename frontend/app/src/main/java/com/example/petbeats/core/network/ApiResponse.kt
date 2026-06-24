package com.example.petbeats.core.network

data class ApiResponse<T> (
    val statusCode: Int,
    val message: String,
    val data: T?,
    val timestamp: String
)