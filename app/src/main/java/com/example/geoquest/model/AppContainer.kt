package com.example.geoquest.model

import android.content.Context

/**
 * App container for dependency injection
 */
interface AppContainer {
    val questRepository: QuestRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineQuestRepository]
 */
class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [QuestRepository]
     */
    override val questRepository: QuestRepository by lazy {
        OfflineQuestRepository(GeoQuestDatabase.getDatabase(context).questDao())
    }
}