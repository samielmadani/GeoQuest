package com.example.geoquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.decode.ImageSource
import com.example.geoquest.ui.navigation.GeoQuestNavGraph
import com.example.geoquest.ui.navigation.NavigationDestination

/**
 * Top level composable that represents screens for the application
 */
@Composable
fun GeoQuestApp(navController: NavHostController = rememberNavController()) {
    GeoQuestNavGraph(navController = navController)
}

object Nav: NavigationDestination {
    override val route = "failed_screen"
    override val titleRes = R.string.incorrect_geo
}

/**
 * App bar to display the title and custom top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuestTopBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    onSettingsClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    navigateToHomeScreen: () -> Unit

) {
    val imageResource: Painter = painterResource(id = R.drawable.logo)
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_icon) )
                }
            } else {
                Image(
                    painter = imageResource,
                    contentDescription = "My Image",
                    modifier = Modifier
                        .scale(0.6f) // Scale the image to 60% of its original size
                        .clickable {
                            navigateToHomeScreen()
                        }
                )
            }
        },
        actions = {
            if (onSettingsClick != null) {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings, // Use the settings icon
                        contentDescription = "Settings"
                    )
                }
            } else if (onShareClick != null) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Quest"
                    )
                }
            }
        }
    )
}