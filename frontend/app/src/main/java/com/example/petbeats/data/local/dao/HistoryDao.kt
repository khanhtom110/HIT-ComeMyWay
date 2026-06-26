package com.example.petbeats.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.petbeats.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistory(history: HistoryEntity)


}