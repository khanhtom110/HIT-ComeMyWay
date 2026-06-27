package com.example.petbeats.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class HistoryEntity (
    @PrimaryKey val keyword: String,
    val time: Long = System.currentTimeMillis() //sắp xếp lịch sử có thời gian gần nhất lên đầu
)