package com.vetpet.petbeats.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vetpet.petbeats.data.local.dao.HistoryDao
import com.vetpet.petbeats.data.local.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}