package com.example.geoquest.ui.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.geoquest.model.openMap
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination

object FindQuestDestination: NavigationDestination {
    override val route = "findQuestScreen"
    override val titleRes = R.string.find_quest
    const val questIdArgument = "questId"
    val routeWithArgs = "$route/{$questIdArgument}"

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindQuestScreen(
    navigateUp: () -> Unit,
    viewModel: FindQuestViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = viewModel.questUiState.questDetails.questTitle,
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
        }
    }
}
