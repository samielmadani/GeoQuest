package com.example.geoquest.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.quest.SettingsViewModel
import com.example.geoquest.ui.theme.GeoQuestTheme

object SettingsDestination: NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Update Username",
                    fontSize = dimensionResource(id = R.dimen.signup_text_size).value.sp,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = viewModel.settingsState.userName,
                    onValueChange = { viewModel.updateSettingsState(it) },
                    label = { Text(stringResource(id = R.string.player_name)) },
                    singleLine = true,
                )
                Button(onClick = {
                    viewModel.saveUserName(viewModel.settingsState.userName)
                }) {
                    Text(
                        text = "Save"
                    )
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    GeoQuestTheme {
        SettingsScreen(
            navigateUp = {}
        )
    }
}