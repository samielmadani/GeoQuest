package com.example.geoquest.ui.quest

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.geoquest.model.Quest
import com.example.geoquest.model.QuestRepository
import com.example.geoquest.model.getCurrentLocation
import kotlinx.coroutines.launch

fun getTestData(): List<Quest> {
    return listOf(
        Quest(
            questTitle = "Eiffel Tower",
            questDescription = "Explore the romantic city of Paris and embark on a quest to discover the iconic Eiffel Tower. It stands tall in the heart of the city, offering breathtaking views from its iron structure. To find it, head to the Champ de Mars park, where you'll see the tower's graceful silhouette against the Parisian skyline.",
            questDifficulty = 2,
            questImageUri = null,
            latitude = 48.8584,
            longitude = 2.2945
        ),
        Quest(
            questTitle = "Great Wall of China",
            questDescription = "Embark on an adventure to explore the vastness of the Great Wall of China. This ancient marvel winds through rugged landscapes, and your quest begins at one of its most picturesque sections. Look for a location near Badaling, where you can see this awe-inspiring structure snaking along the mountains.",
            questDifficulty = 3,
            questImageUri = null,
            latitude = 40.4319,
            longitude = 116.5704
        ),
        Quest(
            questTitle = "Statue of Liberty",
            questDescription = "Visit the symbol of freedom in New York City, the Statue of Liberty. To start your quest, head to Liberty Island in New York Harbor. Look for Lady Liberty holding her torch high, welcoming visitors to the United States. The statue's green hue is an iconic sight against the city skyline.",
            questDifficulty = 2,
            questImageUri = null,
            latitude = 40.6892,
            longitude = -74.0445
        ),
        Quest(
            questTitle = "Pyramids of Giza",
            questDescription = "Embark on a journey to unravel the mysteries of ancient Egypt at the Pyramids of Giza. These majestic structures rise from the desert sands near Cairo. Your quest begins at the Great Pyramid of Khufu, the largest of the three pyramids. Search for this iconic wonder amid the Egyptian desert.",
            questDifficulty = 3,
            questImageUri = null,
            latitude = 29.9792,
            longitude = 31.1342
        ),
        Quest(
            questTitle = "Taj Mahal",
            questDescription = "Witness the unparalleled beauty of the Taj Mahal in Agra, India. Your quest takes you to the banks of the Yamuna River, where you'll find this exquisite white marble mausoleum. Look for its stunning architecture and pristine gardens, which make it one of the world's most renowned monuments of love.",
            questDifficulty = 2,
            questImageUri = null,
            latitude = 27.1751,
            longitude = 78.0421
        ),
        Quest(
            questTitle = "Machu Picchu",
            questDescription = "Embark on a challenging trek to the historical site of Machu Picchu in Peru. Your quest will lead you to the mist-shrouded ancient city nestled high in the Andes Mountains. To start, journey to Aguas Calientes and then hike up the Inca Trail to uncover the secrets of this UNESCO World Heritage Site.",
            questDifficulty = 4,
            questImageUri = null,
            latitude = -13.1631,
            longitude = -72.5450
        ),
        Quest(
            questTitle = "Sydney Opera House",
            questDescription = "Experience the architectural marvel of the Sydney Opera House in Australia. Begin your quest in the vibrant city of Sydney, near Circular Quay. Look for the distinctive white shells that make up this world-renowned performing arts venue, a masterpiece of modern design along the Sydney Harbour.",
            questDifficulty = 1,
            questImageUri = null,
            latitude = -33.8568,
            longitude = 151.2153
        )
    )
}

/**
 * ViewModel
 */
class SettingsViewModel(private val sharedPreferences: SharedPreferences, private val questRepository: QuestRepository): ViewModel() {
    private val editor = sharedPreferences.edit()

    /**
     * Holds current sign up ui state
     */
    var settingsState by mutableStateOf(
        SettingsState(
            userName = getUserName() ?: "",
            developerOptions = getDeveloperOptions(),
            latitude = getLocation().first,
            longitude = getLocation().second,
            isEntryValid = validateInput(getUserName() ?: "")
        )
    )
        private set

    fun updateSettingsState(
        userName: String = settingsState.userName,
        developerOptions: Boolean = settingsState.developerOptions,
        latitude: String = settingsState.latitude,
        longitude: String = settingsState.longitude
    ) {
        settingsState =
            SettingsState(
                userName = userName,
                developerOptions = developerOptions,
                latitude = latitude,
                longitude = longitude,
                isEntryValid = validateInput(userName))
    }

    fun saveSettings() {
        saveUserName(settingsState.userName)
        saveDeveloperOptions(settingsState.developerOptions)
        saveLocation(settingsState.latitude, settingsState.longitude)
    }

    fun insertTestData() {
        viewModelScope.launch {
            val testData = getTestData()
            for (quest in testData) {
                questRepository.addQuest(quest)
            }
        }
    }

    fun clearData() {
        viewModelScope.launch {
            questRepository.deleteAllQuests()
        }
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

    private fun saveLocation(latitude: String, longitude: String) {
        editor.putString("latitude", latitude).apply()
        editor.putString("longitude", longitude).apply()
    }

    private fun getLocation(): Pair<String, String> {
        return Pair(sharedPreferences.getString("latitude", "0.0") ?: "0.0", sharedPreferences.getString("longitude", "0.0") ?: "0.0")
    }

    private fun isValidText(text: String): Boolean {
        return text.matches(Regex("(?=.*[a-zA-Z])[a-zA-Z0-9 ]+"))
    }
}

data class SettingsState(
    val userName: String = "",
    val developerOptions: Boolean = false,
    val latitude: String = "0.0",
    val longitude: String = "0.0",
    val isEntryValid: Boolean = false
)