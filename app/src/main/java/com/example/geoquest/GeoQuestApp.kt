package com.example.geoquest

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.geoquest.ui.navigation.GeoQuestNavGraph

/**
 * Top level composable that represents screens for the application
 */
@Composable
fun GeoQuestApp(navController: NavHostController = rememberNavController()) {
    GeoQuestNavGraph(navController = navController)
}

/**
 * App bar to display the title and custom top bar
 */
@Composable
fun GeoQuestTopBar(
    title: String,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
) {

}