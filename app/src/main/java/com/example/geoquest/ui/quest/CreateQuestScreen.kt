package com.example.geoquest.ui.quest

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.getCurrentLocation
import com.example.geoquest.model.openMap
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.home.HomeDestination
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CreateQuestDestination: NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuest(
    coroutineScope: CoroutineScope,
    navigateBack: () -> Unit,
    viewModel: CreateQuestViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) {contentPadding ->
        CreateQuestBody(
            questUiState = viewModel.questUiState,
            onQuestValueChange = viewModel::updateUiState,
            onCreateClick = {
                    coroutineScope.launch {
                        viewModel.saveQuest()
                        navigateBack()
                    }
            },
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            viewModel = viewModel
        )
    }
}

@Composable
fun CreateQuestBody(
    questUiState: QuestUiState,
    onQuestValueChange: (QuestDetails) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        QuestInputForm(
            questDetails = questUiState.questDetails,
            onValueChange = onQuestValueChange,
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel
        )
        Button(
            onClick = onCreateClick,
            enabled = questUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.create_quest_button)
            )
            Text(stringResource(id = R.string.create_quest_button))
        }
    }
}

@Composable
fun QuestInputForm(
    questDetails: QuestDetails,
    onValueChange: (QuestDetails) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestViewModel
) {
    val context = LocalContext.current


    getCurrentLocation(context, {
        onValueChange(questDetails.copy(latitude = it.latitude, longitude = it.longitude))
    }, {
        Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
    })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = questDetails.questTitle,
            onValueChange = { onValueChange(questDetails.copy(questTitle = it )) },
            label = { Text(stringResource(id = R.string.quest_title)) },
            singleLine = true,
            )
        OutlinedTextField(
            value = questDetails.questDescription,
            onValueChange = { onValueChange(questDetails.copy(questDescription = it )) },
            label = { Text(stringResource(id = R.string.quest_description)) },
        )
        Button(onClick = {
            openMap(context, viewModel.questUiState.questDetails.latitude, viewModel.questUiState.questDetails.longitude, viewModel.questUiState.questDetails.questTitle)
        }) {
            Text("Open in Map")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    GeoQuestTheme {
        CreateQuest(rememberCoroutineScope(), navigateBack = {  })
    }
}