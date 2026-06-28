package com.example.petbeats.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.petbeats.data.local.dao.HistoryDao
import com.example.petbeats.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}