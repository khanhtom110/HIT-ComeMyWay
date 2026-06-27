package com.example.petbeats.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petbeats.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    //Nếu chữ tồn tại rồi thì cập nhật thời gian chữ đó
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    // Lấy danh sách lịch sử
    // Chỉ lấy 10 từ khóa gần nhất để giao diện không bị quá dài
    @Query("SELECT * FROM User ORDER BY time DESC LIMIT 10")
    fun listHistory(): Flow<List<HistoryEntity>>
}