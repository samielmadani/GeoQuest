package com.example.geoquest.ui.quest

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Log

/**
 * ViewModel
 */
class SettingsViewModel(private val sharedPreferences: SharedPreferences): ViewModel() {
    private val editor = sharedPreferences.edit()

    /**
     * Holds current sign up ui state
     */
    var settingsState by mutableStateOf(
        SettingsState(
            userName = getUserName() ?: "",
            developerOptions = getDeveloperOptions(),
            isEntryValid = validateInput(getUserName() ?: "")
        )
    )
        private set

    fun updateSettingsState(userName: String, developerOptions: Boolean) {
        settingsState =
            SettingsState(userName = userName, developerOptions = developerOptions, isEntryValid = validateInput(userName))
    }

    fun saveSettings(userName: String, developerOptions: Boolean) {
        saveUserName(userName)
        saveDeveloperOptions(developerOptions)
    }

    private fun validateInput(userName: String = settingsState.userName): Boolean {
        return userName.isNotBlank() && isValidText(userName) && userName.length <= 36
    }

    private fun saveUserName(userName: String) {
        Log.d("SignUpViewModel", "saveUserName: $userName")
        editor.putString("userName", userName).apply()
    }

    private fun getUserName(): String? {
        return sharedPreferences.getString("userName", null)
    }

    private fun saveDeveloperOptions(isSet: Boolean) {
        editor.putBoolean("developerOptions", isSet).apply()
    }

    private fun getDeveloperOptions(): Boolean {
        return sharedPreferences.getBoolean("developerOptions", false)
    }

    private fun isValidText(text: String): Boolean {
        return text.matches(Regex("(?=.*[a-zA-Z])[a-zA-Z0-9 ]+"))
    }
}

data class SettingsState(
    val userName: String = "",
    val developerOptions: Boolean = false,
    val isEntryValid: Boolean = false
)