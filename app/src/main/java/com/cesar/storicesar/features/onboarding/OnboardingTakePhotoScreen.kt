package com.cesar.storicesar.features.onboarding

import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cesar.storicesar.R
import com.cesar.storicesar.util.CAMERAX_IMAGE
import com.cesar.storicesar.util.PHOTO_NAME
import com.cesar.storicesar.util.TYPE_JPEG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OnboardingTakePhotoScreen(
    registerState: StateFlow<TakePhotoState>,
    onImageSaved: (Uri) -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    Column {
        registerState.collectAsState().value.also { state ->
            if (state is TakePhotoState.Error) {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.error_on_register),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (state is TakePhotoState.Success) {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.register_success),
                    Toast.LENGTH_SHORT
                ).show()
                onRegisterSuccess()
            }

        }
        PhotoCapture(onImageSaved)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoCapture(onImageSaved: (Uri) -> Unit) {

    val permissions = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (permissions.status.isGranted) {
        CameraPreview(onImageSaved)
    } else {
        Column {
            val textToShow = if (permissions.status.shouldShowRationale) {
                R.string.camera_permission_grant
            } else {
                R.string.camera_permission_required
            }
            Text(
                stringResource(id = textToShow),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { permissions.launchPermissionRequest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.request_permissions))
            }
        }
    }
}

@Composable
fun CameraPreview(onImageSaved: (Uri) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {

                val mainExecutor = ContextCompat.getMainExecutor(context)


                val name = SimpleDateFormat(PHOTO_NAME, Locale.getDefault()).format(Date())
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, TYPE_JPEG)
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, CAMERAX_IMAGE)
                    }
                }

                val outputOptions = ImageCapture.OutputFileOptions.Builder(
                    context.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()


                cameraController.takePicture(
                    outputOptions,
                    mainExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            outputFileResults.savedUri?.let { onImageSaved(it) }
                            Toast.makeText(
                                context,
                                context.getString(R.string.photo_saved),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_on_photo_save),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })
            },
                content = {
                    Text(text = stringResource(id = R.string.take_photo_id))
                })

        }
    ) { paddingValues ->
        AndroidView(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues), factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setBackgroundColor(Color.BLACK)
                scaleType = PreviewView.ScaleType.FILL_START
            }.also {
                it.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        })
    }
}


