package com.example.geoquest.ui.quest.viewQuest

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.home.shareQuest
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.quest.createQuest.toQuest

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
    val quest = viewModel.questUiState.questDetails.toQuest()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = ViewQuestDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp,
                onShareClick = { }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp), // Add padding to center-align content
            horizontalAlignment = Alignment.CenterHorizontally // Center-align content horizontally
        ) {
            val painter: Painter = if (quest.questImageUri != null) {
                rememberAsyncImagePainter(model = quest.questImageUri)
            } else {
                painterResource(id = R.drawable.default_image)
            }

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.default_image),
                modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
            )
            Text(
                text = quest.questTitle,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),

            )
            Text(
                text = "By: " + quest.author,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            DifficultyStars(quest.questDifficulty)
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = stringResource(id = R.string.quest_description),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(id = R.dimen.padding_large).value.sp,
                )
            )
            Text(
                text = quest.questDescription,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Pushes the "Find" button to the bottom

            Button(
                onClick = { navigateToFindQuest(quest.questId) },
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = stringResource(id = R.string.begin_hunt))
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
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )
        repeat(difficultyLevel) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) Color.Yellow else Color.DarkGray,
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}
