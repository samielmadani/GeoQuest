package com.example.geoquest.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the [Quest] Database
 */

@Dao
interface QuestDao {
    @Query("SELECT * from quest ORDER BY questTitle ASC")
    fun getAllQuests(): Flow<List<Quest>>

    @Query("SELECT * from quest WHERE questId = :questId")
    fun getQuest(questId: Int): Flow<Quest>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(quest: Quest)

    @Update
    suspend fun update(quest: Quest)

    @Delete
    suspend fun delete(quest: Quest)
}