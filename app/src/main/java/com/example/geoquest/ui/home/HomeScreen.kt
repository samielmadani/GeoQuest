package com.example.geoquest.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToCreateQuest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false
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
            modifier = modifier
                .padding(contentPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier
) {

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GeoQuestTheme {
        HomeScreen(
            navigateToCreateQuest = {}
        )
    }
}