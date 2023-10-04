package com.example.geoquest.ui.quest.findQuest

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.QuestRepository
import com.example.geoquest.model.getCurrentLocation
import com.example.geoquest.ui.quest.createQuest.QuestUiState
import com.example.geoquest.ui.quest.createQuest.toQuestUiState
import com.example.geoquest.ui.quest.viewQuest.ViewQuestDestination
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.pow
import kotlin.random.Random


/**
 * ViewModel to retrieve and update a task from the [QuestRepository]'s data source
 */
class FindQuestViewModel(
    savedStateHandle: SavedStateHandle,
    val sharedPreferences: SharedPreferences,
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

    interface TextExtractionCallback {
        fun onTextExtracted(text: String)
        fun onExtractionFailed(errorMessage: String)
    }



    fun extractTextFromImage(context: Context, imageUriString: String, callback: TextExtractionCallback) {
        val options = TextRecognizerOptions.Builder()
            // Customize recognition options as needed
            .build()
        val recognizer = TextRecognition.getClient(options)

        val imageUri = Uri.parse(imageUriString)

        val image = InputImage.fromFilePath(context, imageUri)

        recognizer.process(image)
            .addOnSuccessListener { texts ->
                // Process the recognized text
                val extractedText = StringBuilder()
                for (block in texts.textBlocks) {
                    for (line in block.lines) {
                        for (element in line.elements) {
                            extractedText.append(element.text).append(" ")
                        }
                    }
                }
                callback.onTextExtracted(extractedText.toString())
            }
            .addOnFailureListener { e ->
                // Handle text recognition failure
                callback.onExtractionFailed("Text extraction failed: ${e.message}")
            }
    }

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0 // Earth's radius in meters

        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat/2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon/2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        context: Context,
        callback: (Boolean) -> Unit
    ) {
        if (sharedPreferences.getBoolean("developerOptions", false)) {
            val lat2 = sharedPreferences.getString("latitude", "0.0")?.toDouble() ?: 0.0
            val lon2 = sharedPreferences.getString("longitude", "0.0")?.toDouble() ?: 0.0
            val within20Meters = areCoordinatesWithin20Meters(lat1, lon1, lat2, lon2)
            callback(within20Meters)
        } else {
            getCurrentLocation(
                context,
                { location ->
                    val within20Meters = areCoordinatesWithin20Meters(location.latitude, location.longitude, lat1, lon1)
                    callback(within20Meters)
                },
                {
                    // Handle the case where obtaining the location failed
                    callback(false) // Assuming you want to return `false` in case of failure
                }
            )
        }
    }

    fun areCoordinatesWithin20Meters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val distance = calculateDistance(lat1, lon1, lat2, lon2)
        return distance <= 20.0
    }




}