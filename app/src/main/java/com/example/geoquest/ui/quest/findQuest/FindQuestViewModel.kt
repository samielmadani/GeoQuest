package com.example.geoquest.ui.quest.findQuest

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.QuestRepository
import com.example.geoquest.model.getCurrentLocation
import com.example.geoquest.ui.quest.createQuest.QuestDetails
import com.example.geoquest.ui.quest.createQuest.QuestUiState
import com.example.geoquest.ui.quest.createQuest.toQuest
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
import java.util.Locale
import kotlin.math.pow
import kotlin.random.Random


/**
 * ViewModel to retrieve and update a task from the [QuestRepository]'s data source
 */
class FindQuestViewModel(
    savedStateHandle: SavedStateHandle,
    private val sharedPreferences: SharedPreferences,
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

    /**
     * Updates the quest in the data source
     */
    suspend fun updateQuest() {
        questRepository.updateQuest(questUiState.questDetails.toQuest())
    }

    /**
     * Updates the [questUiState] with the value provided in the argument.
     */
    fun updateUiState(questDetails: QuestDetails) {
        questUiState =
            QuestUiState(questDetails = questDetails, isEntryValid = true)
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

    private fun calculateDistanceMath(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
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

    private fun levenshteinDistance(str1: String, str2: String): Int {
        val len1 = str1.length
        val len2 = str2.length
        val dp = Array(len1 + 1) { IntArray(len2 + 1) }

        // Initialize the first column
        for (i in 0..len1) {
            dp[i][0] = i
        }

        // Initialize the first row
        for (j in 0..len2) {
            dp[0][j] = j
        }

        for (i in 1..len1) {
            for (j in 1..len2) {
                val cost = if (str1[i - 1] == str2[j - 1]) 0 else 1
                dp[i][j] = minOf(dp[i - 1][j] + 1, // Deletion
                    dp[i][j - 1] + 1, // Insertion
                    dp[i - 1][j - 1] + cost) // Substitution
            }
        }

        return dp[len1][len2]
    }

    private fun stripSpecialCharacters(input: String): String {
        val lowerCase = input.lowercase()
        return lowerCase.replace("[^a-z0-9]".toRegex(), "")
    }

    fun textIsSimilar(input1: String, input2: String, minimum_accuracy: Double): Boolean {
        val text1 = stripSpecialCharacters(input1)
        val text2 = stripSpecialCharacters(input2)

        val distance = levenshteinDistance(text1, text2)
        val maxLen = maxOf(text1.length, text2.length)

        val similarity =  ((1 - distance.toDouble() / maxLen.toDouble()) * 100)
        Log.w("SIMILARITY", similarity.toString())
        return similarity >= minimum_accuracy;
    }

    fun areCoordinatesWithin20Meters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        val distance = calculateDistanceMath(lat1, lon1, lat2, lon2)
        return distance <= 20.0
    }




}