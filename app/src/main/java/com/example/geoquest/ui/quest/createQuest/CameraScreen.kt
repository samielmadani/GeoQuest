package com.example.geoquest.ui.quest.createQuest


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.geoquest.R
import com.example.geoquest.ui.AppViewModelProvider
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor

object CameraScreenDestination: NavigationDestination {
    override val route = "camera_screen"
    override val titleRes = R.string.camera
}
@Composable
fun CameraScreen(
    navigateToCreateQuest: () -> Unit,
    viewModel: CameraViewModel = viewModel(factory = AppViewModelProvider.Factory),
    createViewModel: CreateQuestViewModel,
    lastCapturedPhotoViewModel: LastCapturedPhotoViewModel,
    navigateUp: () -> Unit
) {
    CameraContent(
        onPhotoCaptured = {
            createViewModel.updateUiState(createViewModel.questUiState.questDetails.copy(image = it.toString()))
        },
        lastCapturedPhoto = createViewModel.questUiState.questDetails.image,
        navigateUp = navigateUp
    )
}


@Composable
fun CameraContent(
    onPhotoCaptured: (Uri?) -> Unit,
    lastCapturedPhoto: String? = null,
    navigateUp: () -> Unit
) {

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column {
                if (lastCapturedPhoto != null) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            navigateUp()
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.End),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = "Arrow",
                            tint = Color.Black
                        )
                    }
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        capturePhoto(context, cameraController, onPhotoCaptured)
                    }
                ) {
                    Text(text = stringResource(id = R.string.take_photo))
                }

            }

        }
    ) {contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifeCycleOwner)
                    }
                }
            )

            if (lastCapturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(alignment = BottomStart),
                    uri = lastCapturedPhoto
                )
            }
        }
    }
}

@Composable
fun DisplayImage(uri: String?) {
    if (uri == null) {
        Image(
            painter = painterResource(id = R.drawable.default_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    Image(
        painter = rememberAsyncImagePainter(model = uri),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    uri: String??
) {
    Card(
        modifier = modifier
            .size(dimensionResource(id = R.dimen.card_size))
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        elevation = dimensionResource(id = R.dimen.padding_small),
        shape = MaterialTheme.shapes.large
    ) {
        DisplayImage(uri)
    }
}

fun ImageProxy.toFile(context: Context, fileName: String): File {
    val file = File(context.externalCacheDir, fileName)
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    try {
        FileOutputStream(file).use {
            it.write(bytes)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Uri?) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object: ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val filename = "${System.currentTimeMillis()}.jpg"
            val file = image.toFile(context, filename)
            image.close()

            // Convert the saved file to a Uri and send back to the Composable.
            onPhotoCaptured(Uri.fromFile(file))
        }

        override fun onError(exception: ImageCaptureException) {
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            Log.e("CameraContent", "Error Capturing Image", exception)
        }
    })
}

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    GeoQuestTheme {
        CameraScreen(
            navigateToCreateQuest = {},
            lastCapturedPhotoViewModel = viewModel(),
            navigateUp = {},
            createViewModel = viewModel(factory = AppViewModelProvider.Factory)
        )
    }
}