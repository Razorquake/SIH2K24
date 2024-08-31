package com.razorquake.sih2k24

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import java.util.*
import android.util.Log
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ISLInterpreterApp() {
    var speechText by remember { mutableStateOf("") }
    var interpretedSignLanguage by remember { mutableStateOf("") }
    var isSpeechRecognitionActive by remember { mutableStateOf(false) }
    var isSignLanguageDetectionActive by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val speechRecognizerIntent = remember { Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }}

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val micPermissionGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
            val cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
            if (micPermissionGranted && cameraPermissionGranted) {
                // Permissions granted, you can now use the microphone and camera
            } else {
                // Handle permission denial
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        ))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ISL Interpreter") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Camera Preview
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                CameraPreview(
                    onSignLanguageDetected = { detectedSign ->
                        interpretedSignLanguage = detectedSign
                    },
                    isActive = isSignLanguageDetectionActive
                )
            }

            // Interpreted Sign Language Text Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Interpreted Sign Language:",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = interpretedSignLanguage.ifEmpty { "No sign language detected yet" },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Speech Recognition Text Display
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Recognized Speech:",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = speechText.ifEmpty { "No speech recognized yet" },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        isSignLanguageDetectionActive = !isSignLanguageDetectionActive
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSignLanguageDetectionActive) Color.Red else Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Text(if (isSignLanguageDetectionActive) "Stop Sign Detection" else "Start Sign Detection")
                }
                Button(
                    onClick = {
                        isSpeechRecognitionActive = !isSpeechRecognitionActive
                        if (isSpeechRecognitionActive) {
                            speechRecognizer.startListening(speechRecognizerIntent)
                        } else {
                            speechRecognizer.stopListening()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSpeechRecognitionActive) Color.Red else Color.Green,
                        contentColor = Color.White
                    )
                ) {
                    Text(if (isSpeechRecognitionActive) "Stop Speech Recognition" else "Start Speech Recognition")
                }
            }
        }
    }

    DisposableEffect(Unit) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("SpeechRecognition", "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognition", "Speech started")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Optionally handle audio level changes
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Optionally handle sound buffers
            }

            override fun onEndOfSpeech() {
                Log.d("SpeechRecognition", "Speech ended")
                isSpeechRecognitionActive = false
            }

            override fun onError(error: Int) {
                Log.e("SpeechRecognition", "Error occurred: $error")
                isSpeechRecognitionActive = false
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    speechText = matches[0]
                }
                isSpeechRecognitionActive = false
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partialMatches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!partialMatches.isNullOrEmpty()) {
                    speechText = partialMatches[0]
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Handle other events if needed
            }
        })
        onDispose {
            speechRecognizer.destroy()
        }
    }
}

@Composable
fun CameraPreview(
    onSignLanguageDetected: (String) -> Unit,
    isActive: Boolean
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(executor, { imageProxy ->
                            if (isActive) {
                                // Perform sign language detection here
                                // This is a placeholder for the actual detection logic
                                val detectedSign = "Detected Sign Placeholder"
                                onSignLanguageDetected(detectedSign)
                            }
                            imageProxy.close()
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}