package com.example.geoquest.ui.quest.createQuest

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.getCurrentLocation
import com.example.geoquest.model.openMap
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CreateQuestDestination: NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest_title
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateQuest(
    coroutineScope: CoroutineScope,
    navigateBack: () -> Unit,
    navigateToCamera: () -> Unit,
    lastCapturedPhotoViewModel: LastCapturedPhotoViewModel,
    viewModel: CreateQuestViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    Scaffold(
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = CreateQuestDestination.titleRes),
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
            hasPermission = cameraPermissionState.hasPermission,
            onRequestPermission = cameraPermissionState::launchPermissionRequest,
            navigateToCamera = navigateToCamera,
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            viewModel = viewModel,
            lastCapturedPhotoViewModel = lastCapturedPhotoViewModel
        )
    }
}

@Composable
fun CreateQuestBody(
    questUiState: QuestUiState,
    onQuestValueChange: (QuestDetails) -> Unit,
    onCreateClick: () -> Unit,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navigateToCamera: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateQuestViewModel,
    lastCapturedPhotoViewModel: LastCapturedPhotoViewModel
) {
    val lastCapturedPhoto:Bitmap? by lastCapturedPhotoViewModel.lastCapturedPhoto.observeAsState(null)


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lastCapturedPhoto != null) {
            lastCapturedPhoto?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = stringResource(id = R.string.default_image),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                )
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = stringResource(id = R.string.default_image),
                modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
            )
        }
        Button(onClick = {
            if (hasPermission) {
                navigateToCamera()
            } else {
                onRequestPermission()
            }
        }) {
            Text(text = stringResource(id = R.string.take_photo))
        }
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
        if (viewModel.isDeveloperOptionsSet()) {
            onValueChange(questDetails.copy(latitude = viewModel.getLocation().first.toDouble(), longitude = viewModel.getLocation().second.toDouble()))
        } else {
            onValueChange(questDetails.copy(latitude = it.latitude, longitude = it.longitude))
        }
    }, {
        Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
    })

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 280.dp)

        ) {
            OutlinedTextField(
                value = questDetails.questTitle,
                onValueChange = { onValueChange(questDetails.copy(questTitle = it)) },
                label = { Text(stringResource(id = R.string.quest_title)) },
                singleLine = true,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 280.dp)

        ) {
            OutlinedTextField(
                value = questDetails.questDescription,
                onValueChange = { onValueChange(questDetails.copy(questDescription = it )) },
                label = { Text(stringResource(id = R.string.quest_description)) },
                minLines = 3,
                maxLines = 4
            )
        }
        DifficultySetter(viewModel.selectedDifficulty, viewModel)

        Button(onClick = {
            openMap(context, viewModel.questUiState.questDetails.latitude, viewModel.questUiState.questDetails.longitude, viewModel.questUiState.questDetails.questTitle)
        }) {
            Text("Open in Map")
        }
    }


}


@Composable
fun DifficultySetter(
    selectedDifficulty: Int,
    viewModel: CreateQuestViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Select Difficulty:")
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..5) {
                // Step 3: Add click listeners to stars
                Star(selected = i <= selectedDifficulty, onClick = { viewModel.onDifficultySelected(i) })
            }
        }
    }
}
@Composable
fun Star(
    selected: Boolean,
    onClick: () -> Unit
) {
    Icon(
        painter = painterResource(id = if (selected) R.drawable.baseline_star_24 else R.drawable.baseline_star_border_24),
        contentDescription = "Star",
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(24.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CreateScreenPreview() {
    GeoQuestTheme {
        CreateQuest(
            rememberCoroutineScope(),
            navigateBack = {},
            navigateToCamera = {},
            lastCapturedPhotoViewModel = viewModel()
        )
    }
}