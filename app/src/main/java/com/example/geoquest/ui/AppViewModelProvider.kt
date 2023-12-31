package com.example.geoquest.ui

import com.example.geoquest.ui.quest.createQuest.CameraViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquest.GeoQuestApplication
import com.example.geoquest.model.SavePhotoToGallery
import com.example.geoquest.ui.home.HomeViewModel
import com.example.geoquest.ui.quest.createQuest.CreateQuestViewModel
import com.example.geoquest.ui.settings.SettingsViewModel
import com.example.geoquest.ui.quest.SignUpViewModel
import com.example.geoquest.ui.quest.viewQuest.ViewQuestViewModel
import com.example.geoquest.ui.quest.findQuest.FindQuestViewModel
import com.example.geoquest.ui.quest.findQuest.SuccessViewModel
import com.example.geoquest.ui.quest.findQuest.FailedViewModel



object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(geoQuestApplication().container.sharedPreferences, geoQuestApplication().container.questRepository)
        }
        initializer {
            CreateQuestViewModel(
                geoQuestApplication().container.sharedPreferences,
                geoQuestApplication().container.questRepository
            )
        }
        initializer {
            ViewQuestViewModel(this.createSavedStateHandle(), geoQuestApplication().container.questRepository)
        }
        initializer {
            FindQuestViewModel(this.createSavedStateHandle(), geoQuestApplication().container.sharedPreferences, geoQuestApplication().container.questRepository)
        }
        initializer {
            SignUpViewModel(geoQuestApplication().container.sharedPreferences)
        }
        initializer {
            SettingsViewModel(geoQuestApplication().container.sharedPreferences, geoQuestApplication().container.questRepository)
        }
        initializer {
            CameraViewModel(SavePhotoToGallery(geoQuestApplication()))
        }
        initializer {
            SuccessViewModel(this.createSavedStateHandle(), geoQuestApplication().container.questRepository)
        }
        initializer {
            FailedViewModel(this.createSavedStateHandle(), geoQuestApplication().container.questRepository)
        }
    }
}

fun CreationExtras.geoQuestApplication(): GeoQuestApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GeoQuestApplication)