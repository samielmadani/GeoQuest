package com.example.geoquest.ui.quest.findQuest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

object FailedScreenDestination: NavigationDestination {
    override val route = "failed_screen"
    override val titleRes = R.string.incorrect_geo
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FailedScreen(
    navigateUp: () -> Unit,
    viewModel: FailedViewModel = viewModel(factory = AppViewModelProvider.Factory),

    ) {
    Scaffold(
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = FailedScreenDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
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
                    painter = painterResource(id = R.drawable.baseline_do_not_disturb_on_24),
                    contentDescription = "Cross",
                    modifier = Modifier.size(120.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.not_it),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontSize = 16.sp
                )
            }
        }

    }
}
