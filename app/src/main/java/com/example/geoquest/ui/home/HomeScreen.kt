package com.example.geoquest.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.geoquest.R
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen() {

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GeoQuestTheme {
        HomeScreen()
    }
}