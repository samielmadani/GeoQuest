package com.example.geoquest.ui.quest.findQuest

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi

object SuccessScreenDestination: NavigationDestination {
    override val route = "success_screen"
    override val titleRes = R.string.geo_found
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SuccessScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: SuccessViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    BackPressHandler(onBackPressed = {})

    Scaffold(
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = SuccessScreenDestination.titleRes),
                canNavigateBack = false,
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Center vertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_check_circle_24),
                    contentDescription = "Check",
                    modifier = Modifier.size(120.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You found it!",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navigateToHomeScreen() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = "Arrow",
                        tint = Color.White
                    )
                }
            }
        }

    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}
