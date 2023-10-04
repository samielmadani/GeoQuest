package com.example.geoquest.ui.quest.findQuest

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.home.HomeViewModel
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.quest.createQuest.CreateQuestViewModel
import com.example.geoquest.ui.quest.createQuest.LastCapturedPhotoViewModel
import com.example.geoquest.ui.quest.createQuest.toQuest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object FindQuestDestination: NavigationDestination {
    override val route = "findQuestScreen"
    override val titleRes = R.string.find_quest
    const val questIdArgument = "questId"
    val routeWithArgs = "$route/{$questIdArgument}"

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FindQuestScreen(
    navigateUp: () -> Unit,
    viewModel: FindQuestViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModel2: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToCamera: () -> Unit,
    lastCapturedPhotoViewModel: LastCapturedPhotoViewModel,
    navigateToSuccessScreen: () -> Unit,
    navigateToFailedScreen: () -> Unit,
    createViewModel: CreateQuestViewModel,

    ) {
    val lastCapturedPhoto: Bitmap? by lastCapturedPhotoViewModel.lastCapturedPhoto.observeAsState(null)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val hasPermission = cameraPermissionState.hasPermission
    val onRequestPermission = cameraPermissionState::launchPermissionRequest

    val quest = viewModel.questUiState.questDetails.toQuest()
    val context = LocalContext.current

    var geoText = "";
    var myImage = "";

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeoQuestTopBar(
                title = "Hunting for " + quest.questTitle,
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally // Center-align content horizontally
        ) {

            Box(
                modifier = Modifier.fillMaxHeight(0.4f) // Takes half of the screen height
            ) {
                MapTarget(
                    viewModel = viewModel,
                    viewModel2 = viewModel2
                )
            }

            // Description (40%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center // Center-align content within the Box
            ) {
                Text(
                    text = "Reach the marker on the map to find the quest, and take a photo of the GeoQuest when you find it!",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center, // Left-align text within the Box
                    modifier = Modifier.padding(horizontal = 16.dp) // Add horizontal padding for left alignment
                )
            }

            Row {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "GoeQuest:")

                    val painter: Painter = if (quest.questImageUri != null) {
                        rememberAsyncImagePainter(model = quest.questImageUri)
                    } else {
                        painterResource(id = R.drawable.default_image)
                    }




                    if (quest.questImageUri != null) {

                        viewModel.extractTextFromImage(context, quest.questImageUri!!, object :
                            FindQuestViewModel.TextExtractionCallback {
                            override fun onTextExtracted(text: String) {
                                geoText = text;
                            }

                            override fun onExtractionFailed(errorMessage: String) {

                            }
                        })

                    }

                    Image(
                        painter = painter,
                        contentDescription = stringResource(id = R.string.default_image),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )

                }

                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Your Image:")

                    val painter2: Painter = if (createViewModel.questUiState.questDetails.image != null) {
                        rememberAsyncImagePainter(model = createViewModel.questUiState.questDetails.image)
                    } else {
                        painterResource(id = R.drawable.default_image)
                    }

                    if (createViewModel.questUiState.questDetails.image != null) {

                        viewModel.extractTextFromImage(context, createViewModel.questUiState.questDetails.image!!, object :
                            FindQuestViewModel.TextExtractionCallback {
                            override fun onTextExtracted(text: String) {
                                myImage = text;
                            }

                            override fun onExtractionFailed(errorMessage: String) {

                            }
                        })

                    }

                    Image(
                        painter = painter2,
                        contentDescription = stringResource(id = R.string.default_image),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.image_size))
                    )
                }

            }



            Spacer(modifier = Modifier.weight(1f)) // Spacer to push button to the bottom

            if (createViewModel.questUiState.questDetails.image != null) {
                Button(
                    onClick = {
                        if (geoText == myImage) {
                            navigateToSuccessScreen()
                        } else {
                            navigateToFailedScreen()
                        }},
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(text = "Compare to GeoQuest")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_compare_arrows_24), // Replace with your icon resource
                        contentDescription = null, // Provide a content description if needed
                        modifier = Modifier
                            .size(24.dp) // Adjust the size as needed
                    )
                }
            }
            

            Button(
                onClick = {
                    if (hasPermission) {
                        navigateToCamera()
                    } else {
                        onRequestPermission()
                    }
                          },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(vertical = 16.dp) // Add horizontal padding for left alignment
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "I Found It!")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24), // Replace with your icon resource
                        contentDescription = null, // Provide a content description if needed
                        modifier = Modifier
                            .size(24.dp) // Adjust the size as needed
                    )
                }
            }
        }
    }
}



@Composable
fun MapTarget(
    viewModel: FindQuestViewModel,
    viewModel2: HomeViewModel
){
    // Extract the position from the state
    val lat_long = LatLng(viewModel.questUiState.questDetails.latitude, viewModel.questUiState.questDetails.longitude)

    val cameraPositionState: CameraPositionState
    if (viewModel.questUiState.questDetails.latitude == 0.0 && viewModel.questUiState.questDetails.longitude == 0.0) {
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(lat_long, 10f)
        }
    } else {
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(lat_long, 3f)
        }
    }


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
            Marker(
                state = MarkerState(position = lat_long),
                title = viewModel.questUiState.questDetails.questTitle,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            )
    }
}
