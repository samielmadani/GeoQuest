package com.example.geoquest.ui.quest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.navigation.NavigationDestination

object ViewQuestDestination: NavigationDestination {
    override val route = "viewQuest"
    override val titleRes = R.string.view_quest
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewQuest(
    navigateUp: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
        Box(modifier = Modifier.padding(contentPadding))
    }
}
