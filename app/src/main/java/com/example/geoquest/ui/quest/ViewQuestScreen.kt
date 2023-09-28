package com.example.geoquest.ui.quest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.Quest
import com.example.geoquest.model.openMap
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color

object ViewQuestDestination: NavigationDestination {
    override val route = "viewQuestScreen"
    override val titleRes = R.string.view_quest
    const val questIdArgument = "questId"
    val routeWithArgs = "$route/{$questIdArgument}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewQuestScreen(
    navigateUp: () -> Unit,
    navigateToFindQuest: (Int) -> Unit,
    viewModel: ViewQuestViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = ViewQuestDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) {contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Text(
                text = viewModel.questUiState.questDetails.questTitle,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = viewModel.questUiState.questDetails.questDescription,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DifficultyStars(viewModel.questUiState.questDetails.questDifficulty)

            Button(onClick = {
                openMap(context, viewModel.questUiState.questDetails.latitude, viewModel.questUiState.questDetails.longitude, viewModel.questUiState.questDetails.questTitle)
            }) {
                Text("Open in Map")
            }

            Button(
                onClick = { navigateToFindQuest(viewModel.questId) },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(id = R.string.find_button))
            }

        }
    }
}

@Composable
fun DifficultyStars(difficultyLevel: Int) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "Difficulty:",
            fontSize = 16.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )
        repeat(difficultyLevel) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}
