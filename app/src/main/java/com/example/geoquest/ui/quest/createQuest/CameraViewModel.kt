package com.example.geoquest.ui.quest.createQuest

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.SavePhotoToGallery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val savePhotoToGallery: SavePhotoToGallery
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    val capturedImageUri = MutableStateFlow<Uri?>(null)

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGallery.savePhotoToGallery(bitmap)
            updateCapturedPhotoState(bitmap)
        }
    }

    fun storePhoto(uri: Uri?) {
        Log.d("CameraViewModel", "storePhoto: $uri")
        capturedImageUri.value = uri
    }

    private fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

}

data class CameraState(
    val capturedImage: Bitmap? = null,
)
