package com.example.geoquest.ui.quest

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.geoquest.model.QuestRepository

class SuccessViewModel(
    savedStateHandle: SavedStateHandle,
    private val questRepository: QuestRepository
): ViewModel() {

}