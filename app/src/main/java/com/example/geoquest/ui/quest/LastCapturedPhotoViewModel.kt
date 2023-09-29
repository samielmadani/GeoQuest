package com.example.geoquest.ui.quest

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LastCapturedPhotoViewModel: ViewModel() {
    private val _lastCapturedPhoto = MutableStateFlow<Bitmap?>(null)
    val lastCapturedPhoto = _lastCapturedPhoto.asStateFlow()

    fun setLastCapturedPhoto(photo: Bitmap?) {
        _lastCapturedPhoto.value = photo
    }
}