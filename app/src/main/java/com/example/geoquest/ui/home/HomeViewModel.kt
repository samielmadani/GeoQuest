package com.example.geoquest.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.Quest
import com.example.geoquest.model.QuestRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all the quests in the Room database
 */
class HomeViewModel(private val sharedPreferences: SharedPreferences, private val questRepository: QuestRepository): ViewModel() {
    /**
     * Holds home ui state. The list of items are retrieved from [QuestRepository] and mapped
     * to [HomeUiState]
     */
    var homeUiState: StateFlow<HomeUiState> =
        questRepository.getAllQuestsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun getLocation(): LatLng {
        return LatLng((sharedPreferences.getString("latitude", "0.0") ?: "0.0").toDouble(), (sharedPreferences.getString("longitude", "0.0") ?: "0.0").toDouble())
    }
}

data class HomeUiState(
    val questList: List<Quest> = listOf(),
)