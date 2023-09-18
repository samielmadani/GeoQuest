package com.example.geoquest.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object
 */

@Database(entities = [Quest::class], version = 1, exportSchema = false)
abstract class GeoQuestDatabase: RoomDatabase() {
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var Instance: GeoQuestDatabase? = null

        fun getDatabase(context: Context): GeoQuestDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GeoQuestDatabase::class.java, "quest_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}