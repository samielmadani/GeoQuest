package com.example.geoquest.ui.quest.findQuest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
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




}