package com.example.petbeats.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)