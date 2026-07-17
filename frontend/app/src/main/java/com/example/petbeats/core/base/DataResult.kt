package com.example.petbeats.core.base

import com.example.petbeats.data.repository.ErrorTarget

sealed class DataResult<out T> {
    data class Success<out T>(val data: T, val message: String? = null): DataResult<T>()
    data class Error(val message: String, val code: Int? = null, val target: ErrorTarget = ErrorTarget.GENERAL): DataResult<Nothing>()
}