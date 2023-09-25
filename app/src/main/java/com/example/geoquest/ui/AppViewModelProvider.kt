package com.example.geoquest.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.geoquest.GeoQuestApplication
import com.example.geoquest.ui.home.HomeViewModel
import com.example.geoquest.ui.quest.CreateQuestViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // initializer for HomeViewModel
        initializer {
            HomeViewModel(geoQuestApplication().container.questRepository)
        }
        // initializer for CreateQuestViewModel
        initializer {
            CreateQuestViewModel(geoQuestApplication().container.questRepository)
        }
    }
}

fun CreationExtras.geoQuestApplication(): GeoQuestApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GeoQuestApplication)