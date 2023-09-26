package com.example.geoquest

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoQuestTopBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    onSettingsClick: (() -> Unit)? = null
) {
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
            }
        }
    )
}