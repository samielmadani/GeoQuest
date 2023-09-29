package com.example.geoquest.ui.quest

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LastCapturedPhotoViewModel: ViewModel() {
    private val _lastCapturedPhoto = MutableLiveData<Bitmap?>(null)
    val lastCapturedPhoto: LiveData<Bitmap?> = _lastCapturedPhoto

    fun setLastCapturedPhoto(photo: Bitmap?) {
        _lastCapturedPhoto.value = photo
    }
}