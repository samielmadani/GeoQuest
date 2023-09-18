package com.example.geoquest.model

import kotlinx.coroutines.flow.Flow

/**
 * An offline repository so that users do not need to connect to the internet to retrieve data
 */
class OfflineQuestRepository(private val questDao: QuestDao): QuestRepository {
    override fun getAllQuestsStream(): Flow<List<Quest>> = questDao.getAllQuests()

    override fun getQuestStream(questId: Int): Flow<Quest> = questDao.getQuest(questId)

    override suspend fun addQuest(quest: Quest) = questDao.insert(quest)

    override suspend fun deleteQuest(quest: Quest) = questDao.delete(quest)

    override suspend fun updateQuest(quest: Quest) = questDao.update(quest)
}