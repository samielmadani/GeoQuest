package com.example.geoquest.model

import android.content.Context
import android.content.SharedPreferences

/**
 * App container for dependency injection
 */
interface AppContainer {
    val questRepository: QuestRepository
    val sharedPreferences: SharedPreferences
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

    /**
     * Implementation for [SharedPreferences]
     */
    override val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("geoquest_prefs", Context.MODE_PRIVATE)
    }
}
