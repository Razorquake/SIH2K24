package com.razorquake.sih2k24.presentation.sign_translation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.razorquake.sih2k24.data.TfLiteISLClassifier
import com.razorquake.sih2k24.domain.Classification


@Composable
fun SignTranslatorScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    // Bind the controller to the lifecycle owner
    DisposableEffect(lifecycleOwner) {
        controller.bindToLifecycle(lifecycleOwner)
        onDispose {
            controller.unbind()
        }
    }

    val classifier = remember { TfLiteISLClassifier(context) }

    var classifications by remember { mutableStateOf(emptyList<Classification>()) }

    val analyzer = remember {
        ISLImageAnalyzer(classifier) { results ->
            classifications = results
        }
    }

    LaunchedEffect(analyzer) {
        controller.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            analyzer
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasCameraPermission) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Translation Results:",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            classifications.forEach { classification ->
                Text(
                    text = classification.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            Text("Camera permission is required for this feature")
            Button(
                onClick = { launcher.launch(Manifest.permission.CAMERA) }
            ) {
                Text("Request permission")
            }
        }
    }
}