package com.example.geoquest.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.Nav
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.home.HomeScreen
import com.example.geoquest.ui.quest.createQuest.CameraScreen
import com.example.geoquest.ui.quest.createQuest.CameraScreenDestination
import com.example.geoquest.ui.quest.createQuest.CreateQuest
import com.example.geoquest.ui.quest.createQuest.CreateQuestDestination
import com.example.geoquest.ui.quest.createQuest.CreateQuestViewModel
import com.example.geoquest.ui.quest.findQuest.FailedScreen
import com.example.geoquest.ui.quest.findQuest.FailedScreenDestination
import com.example.geoquest.ui.quest.findQuest.FindQuestDestination
import com.example.geoquest.ui.quest.findQuest.FindQuestScreen
import com.example.geoquest.ui.quest.createQuest.LastCapturedPhotoViewModel
import com.example.geoquest.ui.quest.findQuest.SuccessScreen
import com.example.geoquest.ui.quest.findQuest.SuccessScreenDestination
import com.example.geoquest.ui.quest.viewQuest.ViewQuestDestination
import com.example.geoquest.ui.quest.viewQuest.ViewQuestScreen
import com.example.geoquest.ui.settings.SettingsDestination
import com.example.geoquest.ui.settings.SettingsScreen
import com.example.geoquest.ui.signup.SignUpDestination
import com.example.geoquest.ui.signup.SignUpScreen

/**
 * Provides the Navigation graph for the application
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuestNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val lastCapturedPhotoViewModel: LastCapturedPhotoViewModel = viewModel()
    val createQuestViewModel: CreateQuestViewModel = viewModel(factory = AppViewModelProvider.Factory)

    Scaffold {contentPadding ->
        Box(modifier = Modifier.padding(contentPadding))
        {
            NavHost(
                navController = navController,
                startDestination = SignUpDestination.route,
                modifier = modifier,
            ) {
                composable(route = SignUpDestination.route) {
                    SignUpScreen(
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
                    )
                }
                composable(route = HomeDestination.route) {
                    HomeScreen(
                        navigateToCreateQuest = { navController.navigate(CreateQuestDestination.route) },
                        navigateToViewQuest = { navController.navigate("${ViewQuestDestination.route}/${it}") },
                        navigateToSettings = { navController.navigate(SettingsDestination.route) },
                        createViewModel = createQuestViewModel,
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }

                    )
                }
                composable(route = CreateQuestDestination.route) {
                    CreateQuest(
                        coroutineScope = coroutineScope,
                        navigateBack = { navController.navigate(HomeDestination.route) },
                        navigateToCamera = { navController.navigate(CameraScreenDestination.route) },
                        viewModel = createQuestViewModel,
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
                    )
                }
                composable(route = ViewQuestDestination.routeWithArgs,
                    arguments = listOf(navArgument(ViewQuestDestination.questIdArgument) {
                        type = NavType.IntType
                    })
                ) {
                    ViewQuestScreen(
                        navigateUp = { navController.navigateUp() },
                        navigateToFindQuest = { navController.navigate("${FindQuestDestination.route}/${it}") },
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }


                    )
                }
                composable(route = FindQuestDestination.routeWithArgs,
                    arguments = listOf(navArgument(FindQuestDestination.questIdArgument) {
                        type = NavType.IntType
                    })
                ) {
                    FindQuestScreen(
                        navigateUp = { navController.navigateUp() },
                        navigateToCamera = { navController.navigate(CameraScreenDestination.route) },
                        lastCapturedPhotoViewModel = lastCapturedPhotoViewModel,
                        navigateToSuccessScreen = { navController.navigate(SuccessScreenDestination.route) },
                        navigateToFailedScreen = { navController.navigate(FailedScreenDestination.route) },
                        createViewModel = createQuestViewModel,
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }

                    )
                }
                composable(route = SettingsDestination.route) {
                    SettingsScreen(
                        navigateUp = { navController.navigate(HomeDestination.route) },
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
                    )
                }
                composable(route = CameraScreenDestination.route) {
                    CameraScreen(
                        navigateToCreateQuest = { navController.navigate(CreateQuestDestination.route) },
                        navigateUp = { navController.navigateUp() },
                        lastCapturedPhotoViewModel = lastCapturedPhotoViewModel,
                        createViewModel = createQuestViewModel,

                    )
                }
                composable(route = SuccessScreenDestination.route) {
                    SuccessScreen(
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }
                    )
                }
                composable(route = FailedScreenDestination.route) {
                    FailedScreen(
                        navigateUp = { navController.navigateUp() },
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) }

                    )
                }
                composable(route = Nav.route) {
                    GeoQuestTopBar(
                        navigateToHomeScreen = { navController.navigate(HomeDestination.route) },
                        canNavigateBack = true,
                        title = "",
                    )
                }
            }
        }
    }
}