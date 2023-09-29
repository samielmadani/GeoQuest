package com.example.geoquest.ui.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.Quest
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navigateToCreateQuest: () -> Unit,
    navigateToViewQuest: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            // Content to show when permission is not granted
            PermissionsDenied(modifier = modifier, scrollBehavior = scrollBehavior, permissionState = permissionState)
        },
        permissionNotAvailableContent = {
            // Content to show when permission is not available (e.g., policy restrictions)
            PermissionsDenied(modifier = modifier, scrollBehavior = scrollBehavior, permissionState = permissionState)
        }
    ) {
        // Content to show when permission is granted
        Scaffold(
            modifier = modifier,
            topBar = {
                GeoQuestTopBar(
                    title = stringResource(id = HomeDestination.titleRes),
                    canNavigateBack = false,
                    onSettingsClick = navigateToSettings,
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = navigateToCreateQuest,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "")
                }
            },
        ) {contentPadding ->
            HomeBody(
                questList = homeUiState.questList,
                navigateToViewQuest,
                modifier = modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PermissionsDenied(modifier: Modifier, scrollBehavior: TopAppBarScrollBehavior, permissionState: PermissionState) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = HomeDestination.titleRes)) },
                modifier = modifier,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Location permissions are required to use this app.")
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Request Permission")
            }
        }
    }
}

@Composable
fun HomeBody(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (questList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_quest),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            QuestList(
                questList = questList,
                navigateToViewQuest
            )
        }
    }
}

@Composable
fun QuestList(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(items = questList, key = { it.questId }) {quest ->
            QuestCard(
                quest = quest,
                navigateToViewQuest
            )
        }
    }
}

@Composable
fun QuestCard(
    quest: Quest,
    navigateToViewQuest: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp),
                    tint = Color.Green
                )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = quest.questTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small)),
                )
                Button(
                    onClick = { navigateToViewQuest(quest.questId) },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = stringResource(id = R.string.view_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GeoQuestTheme {
        HomeScreen(
            navigateToCreateQuest = {},
            navigateToViewQuest = {},
            navigateToSettings = {}
        )
    }
}