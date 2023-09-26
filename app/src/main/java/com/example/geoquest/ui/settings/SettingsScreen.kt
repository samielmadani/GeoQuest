package com.example.geoquest.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.home.HomeBody
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme

object SettingsDestination: NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateToHomeScreen: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    androidx.compose.material3.Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = true,
            )
        }
    ) { contentPadding ->
        Text(
            text = "TEST",
            fontSize = dimensionResource(id = R.dimen.signup_text_size).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    GeoQuestTheme {
        SettingsScreen(
            navigateToHomeScreen = {}
        )
    }
}