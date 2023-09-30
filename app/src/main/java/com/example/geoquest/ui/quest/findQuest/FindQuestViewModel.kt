package com.example.geoquest.ui.quest.findQuest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.QuestRepository
import com.example.geoquest.ui.quest.createQuest.QuestUiState
import com.example.geoquest.ui.quest.createQuest.toQuestUiState
import com.example.geoquest.ui.quest.viewQuest.ViewQuestDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random


/**
 * ViewModel to retrieve and update a task from the [QuestRepository]'s data source
 */
class FindQuestViewModel(
    savedStateHandle: SavedStateHandle,
    private val questRepository: QuestRepository
): ViewModel() {


    /**     * Holds the current task ui state
     */
    var questUiState by mutableStateOf(QuestUiState())
        private set


    private val questId: Int = checkNotNull(savedStateHandle[ViewQuestDestination.questIdArgument])


    init {
        viewModelScope.launch {
            questUiState = questRepository.getQuestStream(questId)
                .filterNotNull()
                .first()
                .toQuestUiState(true)
        }
    }

    fun randomBoolean(): Boolean {
        return Random.nextBoolean()
    }

}