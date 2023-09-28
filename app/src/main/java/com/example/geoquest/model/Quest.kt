package com.example.geoquest.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class representing the Quest table
 */

@Entity(tableName = "quest")
data class Quest (
    @PrimaryKey(autoGenerate = true)
    val questId: Int = 0,
    val questTitle: String,
    val questDescription: String,
    val questDifficulty: Int,
    var questImageUri: String?,
    val latitude: Double,
    val longitude: Double
)