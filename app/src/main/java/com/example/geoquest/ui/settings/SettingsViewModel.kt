package com.example.geoquest.ui.settings

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geoquest.R
import com.example.geoquest.model.Quest
import com.example.geoquest.model.QuestRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun getUri(image: Int, context: Context): String {
    val uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.packageName)
        .appendPath(image.toString())
        .build()
    return uri.toString()
}
fun getTestData(context: Context): List<Quest> {
    return listOf(
//        Quest(
//            questTitle = "Eiffel Tower",
//            questDescription = "Explore the romantic city of Paris and embark on a quest to discover the iconic Eiffel Tower. It stands tall in the heart of the city, offering breathtaking views from its iron structure. To find it, head to the Champ de Mars park, where you'll see the tower's graceful silhouette against the Parisian skyline.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = 48.8584,
//            longitude = 2.2945,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Great Wall of China",
//            questDescription = "Embark on an adventure to explore the vastness of the Great Wall of China. This ancient marvel winds through rugged landscapes, and your quest begins at one of its most picturesque sections. Look for a location near Badaling, where you can see this awe-inspiring structure snaking along the mountains.",
//            questDifficulty = 3,
//            questImageUri = null,
//            latitude = 40.4319,
//            longitude = 116.5704,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Statue of Liberty",
//            questDescription = "Visit the symbol of freedom in New York City, the Statue of Liberty. To start your quest, head to Liberty Island in New York Harbor. Look for Lady Liberty holding her torch high, welcoming visitors to the United States. The statue's green hue is an iconic sight against the city skyline.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = 40.6892,
//            longitude = -74.0445,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Pyramids of Giza",
//            questDescription = "Embark on a journey to unravel the mysteries of ancient Egypt at the Pyramids of Giza. These majestic structures rise from the desert sands near Cairo. Your quest begins at the Great Pyramid of Khufu, the largest of the three pyramids. Search for this iconic wonder amid the Egyptian desert.",
//            questDifficulty = 3,
//            questImageUri = null,
//            latitude = 29.9792,
//            longitude = 31.1342,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Taj Mahal",
//            questDescription = "Witness the unparalleled beauty of the Taj Mahal in Agra, India. Your quest takes you to the banks of the Yamuna River, where you'll find this exquisite white marble mausoleum. Look for its stunning architecture and pristine gardens, which make it one of the world's most renowned monuments of love.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = 27.1751,
//            longitude = 78.0421,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Machu Picchu",
//            questDescription = "Embark on a challenging trek to the historical site of Machu Picchu in Peru. Your quest will lead you to the mist-shrouded ancient city nestled high in the Andes Mountains. To start, journey to Aguas Calientes and then hike up the Inca Trail to uncover the secrets of this UNESCO World Heritage Site.",
//            questDifficulty = 4,
//            questImageUri = null,
//            latitude = -13.1631,
//            longitude = -72.5450,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Sydney Opera House",
//            questDescription = "Experience the architectural marvel of the Sydney Opera House in Australia. Begin your quest in the vibrant city of Sydney, near Circular Quay. Look for the distinctive white shells that make up this world-renowned performing arts venue, a masterpiece of modern design along the Sydney Harbour.",
//            questDifficulty = 1,
//            questImageUri = null,
//            latitude = -33.8568,
//            longitude = 151.2153,
//            author = "GeoQuest"
//        )
//        Quest(
//            questTitle = "Queenstown",
//            questDescription = "Explore the adventure capital of the world, Queenstown! Start your quest in the heart of this picturesque town on Lake Wakatipu's shores. Experience thrilling activities like bungee jumping, jet boating, and hiking in the surrounding Remarkables mountain range.",
//            questDifficulty = 3,
//            questImageUri = null,
//            latitude = -45.0312,
//            longitude = 168.6626,
//            isCompleted = false,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Tongariro National Park",
//            questDescription = "Embark on a volcanic adventure in Tongariro National Park. Your quest begins at Whakapapa Village, where you can explore the otherworldly landscapes of this UNESCO World Heritage Site. Hike the Tongariro Alpine Crossing for stunning views of the park's active volcanoes.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = -39.2900,
//            longitude = 175.5672,
//            isCompleted = false,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Abel Tasman National Park",
//            questDescription = "Discover the pristine beaches and lush forests of Abel Tasman National Park. Start your quest at the park's visitor center in Marahau. Explore the coastal tracks, kayak in the clear waters, and spot native wildlife in this natural paradise.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = -40.9324,
//            longitude = 173.0089,
//            isCompleted = false,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Rotorua's Geothermal Wonders",
//            questDescription = "Experience the geothermal wonders of Rotorua. Your quest takes you to Te Puia, where you can witness geysers, mud pools, and cultural performances. Explore the unique geothermal landscape and learn about Māori culture in this captivating destination.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = -38.1499,
//            longitude = 176.2498,
//            isCompleted = false,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Fiordland National Park",
//            questDescription = "Embark on an awe-inspiring journey to Fiordland National Park. Begin your quest in the town of Te Anau and explore the rugged wilderness of this World Heritage Area. Discover the enchanting fjords, dense rainforests, and serene lakes that define this natural wonder.",
//            questDifficulty = 3,
//            questImageUri = null,
//            latitude = -45.4145,
//            longitude = 167.7180,
//            isCompleted = true,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Lake Taupo",
//            questDescription = "Discover the beauty of Lake Taupo, New Zealand's largest lake. Your quest starts in the town of Taupo, where you can take part in water sports, fishing, or simply relax by the lake's tranquil shores. Don't miss the powerful Huka Falls nearby.",
//            questDifficulty = 2,
//            questImageUri = null,
//            latitude = -38.6857,
//            longitude = 176.0702,
//            isCompleted = false,
//            author = "GeoQuest"
//        ),
//        Quest(
//            questTitle = "Aoraki / Mount Cook National Park",
//            questDescription = "Embark on a high-altitude adventure in Aoraki / Mount Cook National Park. Your quest begins at the village of Mount Cook, where you can hike in the shadow of New Zealand's highest peak, Aoraki (Mount Cook). Explore alpine landscapes, glaciers, and stargaze in this pristine wilderness.",
//            questDifficulty = 3,
//            questImageUri = null,
//            latitude = -43.7342,
//            longitude = 170.0977,
//            isCompleted = false,
//            author = "GeoQuest"
//        )
        Quest(
            questTitle = "The Last Kiwi Stand",
            questDescription = "Find the last remaining kiwi bird statue, a reminder of the world before. Hint: It's not made of chocolate anymore.",
            questDifficulty = 3,
            questImageUri = getUri(R.drawable.kiwi, context),
            latitude = -40.9006,
            longitude = 174.8860,
            author = "PostApocExplorer",
            isCompleted = false
        ),

        Quest(
            questTitle = "Wellington's Wasteland Watchtower",
            questDescription = "Climb the remains of the once-majestic Wellington Sky Tower to find a humorous sign about the end of WiFi.",
            questDifficulty = 4,
            questImageUri = getUri(R.drawable.sky_tower, context),
            latitude = -41.2865,
            longitude = 174.7762,
            author = "EndOfDaysTraveler",
            isCompleted = false
        ),

        Quest(
            questTitle = "Rotorua's Radiant Springs",
            questDescription = "Discover Rotorua's hot springs, now glowing a bit more than usual. Beware of two-headed ducks!",
            questDifficulty = 2,
            questImageUri = getUri(R.drawable.ducks, context),
            latitude = -38.1368,
            longitude = 176.2497,
            author = "NuclearNomad",
            isCompleted = false
        ),
        Quest(
            questTitle = "Dunedin's Dystopian Drive",
            questDescription = "Drive through the streets of Dunedin, but watch out for the road signs that have taken on a humorous twist!",
            questDifficulty = 1,
            questImageUri = getUri(R.drawable.dunedin, context),
            latitude = -45.8788,
            longitude = 170.5020,
            author = "ApocalypseAdventurer",
            isCompleted = true
        )
    )
}

/**
 * ViewModel
 */
class SettingsViewModel(
    private val sharedPreferences: SharedPreferences,
    private val questRepository: QuestRepository
): ViewModel() {

    var isLoading by mutableStateOf(false)

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

    fun insertTestData(context: Context) {
        viewModelScope.launch {
            isLoading = true
            val testData = getTestData(context)
            for (quest in testData) {
                questRepository.addQuest(quest)
            }
            delay(10000)
            isLoading = false
        }
    }

    fun clearData() {
        viewModelScope.launch {
            isLoading = true
            questRepository.deleteAllQuests()
            delay(10000)
            isLoading = false
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