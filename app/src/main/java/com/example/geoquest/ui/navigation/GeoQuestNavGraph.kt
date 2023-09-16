package com.example.geoquest.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.home.HomeScreen
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
                    HomeScreen()
                }
            }
        }
    }
}