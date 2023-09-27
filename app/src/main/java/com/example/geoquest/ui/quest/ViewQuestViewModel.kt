package com.example.geoquest.ui.quest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.QuestRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a task from the [QuestRepository]'s data source
 */
class ViewQuestViewModel(
    //savedStateHandle: SavedStateHandle,
    private val questRepository: QuestRepository
): ViewModel() {

    /**     * Holds the current task ui state
     */
    var questUiState by mutableStateOf(QuestUiState())
        private set

    //private val questId: Int = checkNotNull(savedStateHandle[ViewQuestDestination.route])

    init {
        viewModelScope.launch {
            questUiState = questRepository.getQuestStream(3)
                .filterNotNull()
                .first()
                .toQuestUiState(true)
        }
    }
    /**
     * Update the task in the [TasksRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(questUiState.questDetails)) {
            questRepository.updateQuest(questUiState.questDetails.toQuest())
        }
    }

    /**
     * Updates the [taskUiState] with the value provided in the argument.
     */
    fun updateUiState(questDetails: QuestDetails) {
        questUiState =
            QuestUiState(questDetails = questDetails, isEntryValid = validateInput(questDetails))
    }

    private fun validateInput(uiState: QuestDetails = questUiState.questDetails): Boolean {
        return with(uiState) {
            questTitle.isNotBlank() && isValidText(questTitle) &&
                    (isValidText(questDescription) || questDescription.isBlank()) &&
                    questTitle.length <= 36 && questDescription.length <= 256
        }
    }

    private fun isValidText(text: String): Boolean {
        return text.matches(Regex("(?=.*[a-zA-Z])[a-zA-Z0-9 ]+"))
    }

}