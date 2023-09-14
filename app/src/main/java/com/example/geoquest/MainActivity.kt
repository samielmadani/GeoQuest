package com.example.geoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.geoquest.ui.signup.SignUpScreen
import com.example.geoquest.ui.theme.GeoQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoQuestTheme {
                SignUpScreen()
            }
        }
    }
}