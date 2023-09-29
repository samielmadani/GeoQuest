package com.example.geoquest.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.getCurrentLocation
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
    navigateToHomeScreen: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

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
                OutlinedTextField(
                    value = viewModel.settingsState.userName,
                    onValueChange = { viewModel.updateSettingsState(userName = it) },
                    label = { Text(stringResource(id = R.string.player_name)) },
                    singleLine = true,
                )

                DeveloperOptionsToggle(
                    isChecked = viewModel.settingsState.developerOptions,
                    onCheckedChange = { viewModel.updateSettingsState(developerOptions = it) }
                )

                if (viewModel.settingsState.developerOptions) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {viewModel.insertTestData()}, colors = ButtonDefaults.buttonColors(Color.Red)) {
                            Text(
                                text = "Populate Database"
                            )
                        }
                        Button(onClick = {viewModel.clearData()}, colors = ButtonDefaults.buttonColors(Color.Red)) {
                            Text(
                                text = "Clear Database"
                            )
                        }
                    }

                    LatLongInput(viewModel.settingsState.latitude, { viewModel.updateSettingsState(
                        latitude = it,
                    ) }, viewModel.settingsState.longitude, {
                        viewModel.updateSettingsState(
                            longitude = it
                        )
                    })
                    Button(onClick = {
                        getCurrentLocation(context, {
                            viewModel.updateSettingsState(
                                latitude = it.latitude.toString(),
                                longitude = it.longitude.toString()
                            )
                        }, {
                            Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
                        })
                    }) {
                        Text(
                            text = "Set as current location"
                        )
                    }
                }

                Button(onClick = {
                    viewModel.saveSettings()
                    navigateToHomeScreen()
                }) {
                    Text(
                        text = "Save"
                    )
                }
            }
    }
}

@Composable
fun DeveloperOptionsToggle(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Developer Options")
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun LatLongInput(
    latitude: String,
    onLatitudeChange: (String) -> Unit,
    longitude: String,
    onLongitudeChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = latitude,
            onValueChange = onLatitudeChange,
            label = { Text("Latitude") },
            modifier = Modifier.weight(1f),  // This will make the text fields share the available space equally
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { /* Handle next action */ })
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = onLongitudeChange,
            label = { Text("Longitude") },
            modifier = Modifier.weight(1f),  // This will make the text fields share the available space equally
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { /* Handle done action */ })
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    GeoQuestTheme {
        SettingsScreen(
            navigateUp = {},
            navigateToHomeScreen = {}
        )
    }
}