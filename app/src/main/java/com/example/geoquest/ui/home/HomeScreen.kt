package com.example.geoquest.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.geoquest.GeoQuestTopBar
import com.example.geoquest.R
import com.example.geoquest.model.Quest
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.quest.createQuest.CreateQuestViewModel
import com.example.geoquest.ui.quest.findQuest.BackPressHandler
import com.example.geoquest.ui.quest.viewQuest.DifficultyStars
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMultiplePermissions() {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
        )
    )

    when {
        multiplePermissionsState.allPermissionsGranted -> {
            // All permissions are granted, proceed with the app's functionality
        }
        multiplePermissionsState.shouldShowRationale -> {
            // Show rationale to the user and provide a way to request permissions again
        }
        !multiplePermissionsState.permissionRequested -> {
            // Request permissions
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
        else -> {
            // Handle the case where permissions are denied
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navigateToCreateQuest: () -> Unit,
    navigateToViewQuest: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    createViewModel: CreateQuestViewModel,
    ) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedQuestId = remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    BackPressHandler(onBackPressed = {})

    // Listen for quests (test)
    discoverQuest(context, createViewModel)

    val multiplePermissionsState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.NEARBY_WIFI_DEVICES,
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
            )
        )
    } else {
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
            )
        )
    }

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            // Content to show when permission is not granted
            PermissionsDenied(modifier = modifier, scrollBehavior = scrollBehavior, multiplePermissionsState = multiplePermissionsState)
        },
        permissionNotAvailableContent = {
            // Content to show when permission is not available (e.g., policy restrictions)
            PermissionsDenied(modifier = modifier, scrollBehavior = scrollBehavior, multiplePermissionsState = multiplePermissionsState)
        }
    ) {
        // Content to show when permission is granted
        Scaffold(
            modifier = modifier,
            topBar = {
                GeoQuestTopBar(
                    title = stringResource(id = HomeDestination.titleRes),
                    canNavigateBack = false,
                    onSettingsClick = navigateToSettings
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
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.4f)
                        .padding(contentPadding)// Takes half of the screen height
                ) {
                    MapTarget(homeUiState.questList, viewModel, selectedQuestId)
                }
                HomeBody(
                    questList = homeUiState.questList,
                    navigateToViewQuest,
                    selectedQuestId,
                    modifier = modifier
                        .fillMaxSize()
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PermissionsDenied(modifier: Modifier, scrollBehavior: TopAppBarScrollBehavior, multiplePermissionsState: MultiplePermissionsState) {
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
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest()
            }) {
                Text("Request Permission")
            }
        }
    }
}

@Composable
fun HomeBody(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    selectedQuestId: MutableIntState,
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
                navigateToViewQuest,
                selectedQuestId
            )
        }
    }
}

@Composable
fun QuestList(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    selectedQuestId: MutableIntState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedQuestId.intValue) {
        if (selectedQuestId.intValue != -1) {
            val index = questList.indexOfFirst { quest -> quest.questId == selectedQuestId.intValue }
            listState.animateScrollToItem(index)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(items = questList, key = { it.questId }) { quest ->
            QuestCard(
                quest = quest,
                navigateToViewQuest,
                isSelected = quest.questId == selectedQuestId.intValue,
                selectedQuestId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QuestCard(
    quest: Quest,
    navigateToViewQuest: (Int) -> Unit,
    isSelected: Boolean = false,
    selectedQuestId: MutableIntState
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small)),
        onClick = {
            selectedQuestId.intValue = quest.questId
        },
        modifier = Modifier
            .background(
                if (isSelected) if (isSystemInDarkTheme()) Color.Black else Color.LightGray else Color.Transparent
            )
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .shadow(8.dp, shape = RoundedCornerShape(dimensionResource(id = R.dimen.padding_small))),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val painter: Painter = if (quest.questImageUri != null) {
                    rememberAsyncImagePainter(model = quest.questImageUri)
                } else {
                    painterResource(id = R.drawable.default_image)
                }

                Box(modifier = Modifier
                    .fillMaxHeight(0.7F)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = stringResource(id = R.string.default_image),
                        modifier = Modifier.size(130.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = quest.questTitle,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "By: " + quest.author,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    DifficultyStars(quest.questDifficulty)
                    Row {

                        Button(
                            onClick = { navigateToViewQuest(quest.questId) },
                            shape = MaterialTheme.shapes.small,
                        ) {
                            Text(text = stringResource(id = R.string.view_button))
                        }
                        Button(
                            onClick = { shareQuest(quest, context) },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(Color.Transparent),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Quest"
                            )
                        }
                    }

                }

            }
        }
    }
}


@Composable
fun MapTarget(questList: List<Quest>, viewModel: HomeViewModel, selectedQuestId: MutableIntState){
    // Extract the position from the state
    val positions = questList.map { LatLng(it.latitude, it.longitude) }
    val averageLat = positions.map { it.latitude }.average()
    val averageLng = positions.map { it.longitude }.average()

    val cameraPositionState: CameraPositionState
    if (positions.isEmpty()) {
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(viewModel.getLocation(), 4.5f)
        }
    } else if (selectedQuestId.intValue != -1) {
        val quest = questList.find { quest -> quest.questId == selectedQuestId.intValue }

        val position = if (quest !== null) {
            CameraPosition.fromLatLngZoom(LatLng(quest.latitude, quest.longitude), 4.5f)
        } else {
            CameraPosition.fromLatLngZoom(LatLng(averageLat, averageLng), 4.5f)
        }

        cameraPositionState = CameraPositionState(position = position)
    } else {
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(averageLat, averageLng), 4.5f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        for (quest in questList) {
            Marker(
                state = rememberMarkerState(position = LatLng(quest.latitude, quest.longitude)),
                title = quest.questTitle,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                onClick = {
                    selectedQuestId.intValue = quest.questId
                    true
                }
            )

        }
    }
}

fun convertQuestToJson(quest: Quest): String {
    val gson = Gson()
    return gson.toJson(quest, Quest::class.java)
}

// Assuming you have a method to convert a JSON string to a Quest object
fun convertJsonToQuest(json: String): Quest {
    // Convert the JSON string to a Quest object
    // You can use libraries like Gson or Moshi for this
    val gson = Gson()
    return gson.fromJson(json, Quest::class.java)
}

fun discoverQuest(context: Context, viewModel: CreateQuestViewModel) {
    startDiscovery(context, viewModel)
}

private fun startAdvertising(quest: Quest, context: Context) {
    val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build()

    class ReceiveBytesPayloadListener : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            // This always gets the full data of the payload. Is null if it's not a BYTES payload.
            if (payload.type == Payload.Type.BYTES) {
                val receivedBytes = payload.asBytes()
                Log.i("SHARE SEND", "Received data")
                if (receivedBytes != null) {
                    val quest = convertJsonToQuest(String(receivedBytes))
                    Log.i("SHARE SEND", "QUEST: $quest")
                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
            Log.i("SHARE SEND", "Update requested??")
        }
    }

    val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                Log.i("SHARE SEND", "Connection initiated")
                Nearby.getConnectionsClient(context).acceptConnection(endpointId, ReceiveBytesPayloadListener())
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                Log.i("SHARE SEND", "Connection Result")
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        val bytesPayload = Payload.fromBytes(convertQuestToJson(quest).toByteArray())
                        Nearby.getConnectionsClient(context).sendPayload(endpointId, bytesPayload)
                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {}
                    ConnectionsStatusCodes.STATUS_ERROR -> {}
                    else -> {}
                }
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
                Log.i("SHARE SEND", "Disconnected")
            }
        }

    Nearby.getConnectionsClient(context)
        .startAdvertising(
            Build.ID, "com.example.geoquest", connectionLifecycleCallback, advertisingOptions
        )
        .addOnSuccessListener { Log.i("SHARE SEND", "startAdvertising OnSuccessListener") }
        .addOnFailureListener { error -> Log.e("SHARE SEND", "startAdvertising Error: $error") }
}

private fun startDiscovery(context: Context, viewModel: CreateQuestViewModel) {
    val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
    val id = Build.ID
    class ReceiveBytesPayloadListener : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            // This always gets the full data of the payload. Is null if it's not a BYTES payload.
            if (payload.type == Payload.Type.BYTES) {
                val receivedBytes = payload.asBytes()
                Log.i("SHARE RCV", "Received data")

                if (receivedBytes != null) {
                    val quest = convertJsonToQuest(String(receivedBytes))
                    Log.i("SHARE RCV", "QUEST: $quest")
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.createQuest(quest)
                    }
                }
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
            Log.i("SHARE RCV", "Update requested??")
        }
    }

    val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                Nearby.getConnectionsClient(context).acceptConnection(endpointId, ReceiveBytesPayloadListener())
                Log.i("SHARE RCV", "Connection initiated")
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {}
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {}
                    ConnectionsStatusCodes.STATUS_ERROR -> {}
                    else -> {}
                }

                Log.i("SHARE RCV", "Connection result")
            }

            override fun onDisconnected(endpointId: String) {
                // We've been disconnected from this endpoint. No more data can be
                // sent or received.
                Log.i("SHARE RCV", "Disconnected")
            }
        }

    val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                Log.i("SHARE RCV", "Endpoint found")

                // An endpoint was found. We request a connection to it.
                Nearby.getConnectionsClient(context)
                    .requestConnection(id, endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener { Log.i("SHARE RCV", "requestConnection OnSuccessListener") }
                    .addOnFailureListener { error -> Log.e("SHARE RCV", "requestConnection Error: $error") }
            }

            override fun onEndpointLost(endpointId: String) {
                // A previously discovered endpoint has gone away.
                Log.i("SHARE RCV", "Lost the endpoint")
            }
        }

    Nearby.getConnectionsClient(context)
        .startDiscovery("com.example.geoquest", endpointDiscoveryCallback, discoveryOptions)
        .addOnSuccessListener { Log.i("SHARE RCV", "startDiscovery OnSuccessListener") }
        .addOnFailureListener { error -> Log.e("SHARE RCV", "startDiscovery Error: $error") }
}

@OptIn(ExperimentalPermissionsApi::class)
fun shareQuest(quest: Quest, context: Context) {
    startAdvertising(quest, context)
}


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    GeoQuestTheme {
//        HomeScreen(
//            navigateToCreateQuest = {},
//            navigateToViewQuest = {},
//            navigateToSettings = {},
//
//        )
//    }
//}