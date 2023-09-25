package com.example.geoquest.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.home.HomeScreen
import com.example.geoquest.ui.quest.CreateQuest
import com.example.geoquest.ui.quest.CreateQuestDestination
import com.example.geoquest.ui.quest.ViewQuest
import com.example.geoquest.ui.quest.ViewQuestDestination
import com.example.geoquest.ui.signup.SignUpDestination
import com.example.geoquest.ui.signup.SignUpScreen

/**
 * Provides the Navigation graph for the application
 */

@Composable
fun GeoQuestNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold {contentPadding ->
        Box(modifier = Modifier.padding(contentPadding))
        {
            NavHost(
                navController = navController,
                startDestination = SignUpDestination.route,
                modifier = modifier
            ) {
                composable(route = SignUpDestination.route) {
                    SignUpScreen(
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
                    )
                }
                composable(route = HomeDestination.route) {
                    HomeScreen(
                        navigateToCreateQuest = { navController.navigate(CreateQuestDestination.route) },
                        navigateToViewQuest = { navController.navigate(ViewQuestDestination.route) }
                    )
                }
                composable(route = CreateQuestDestination.route) {
                    CreateQuest(
                        coroutineScope = coroutineScope,
                        navigateBack = { navController.navigate(HomeDestination.route) })
                }
                composable(route = ViewQuestDestination.route) {
                    ViewQuest()
                }
            }
        }
    }
}