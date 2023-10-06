package com.example.geoquest.model

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides add, update, delete, and retrieve of [Quest] from a given data source
 */
interface QuestRepository {
    /**
     * Retrieve all the quests from the given data source
     */
    fun getAllQuestsStream(): Flow<List<Quest>>

    /**
     * Retrieve a quest from the given data source that matches with the [questId]
     */
    fun getQuestStream(questId: Int): Flow<Quest>

    /**
     * Retrieve completed quests from the data source
     */
    fun getCompletedQuests(): Flow<List<Quest>>

    /**
     * Add quest from the data source
     */
    suspend fun addQuest(quest: Quest)

    /**
     * Delete quest from the data source
     */
    suspend fun deleteQuest(quest: Quest)

    /**
     * Delete all quests from the data source
     */
    suspend fun deleteAllQuests()

    /**
     * Update quest in the data source
     */
    suspend fun updateQuest(quest: Quest)
}