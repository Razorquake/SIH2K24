package com.razorquake.sih2k24.presentation.text_translation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.razorquake.sih2k24.R
import com.razorquake.sih2k24.presentation.text_translation.components.FrequencyChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextTranslatorScreen(
    onBackClick: () -> Unit,
    onTranslationComplete: (String) -> Unit,
    state: TTState,
    onEvent: (TTEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // Permission request
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onEvent(TTEvent.UpdateIsRecording(true))
            // Permission granted, start recording
            onEvent(TTEvent.StartRecording(context) { recognizedText ->
                onEvent(TTEvent.UpdateQuery(recognizedText))
                onEvent(TTEvent.UpdateIsRecording(false))
                onTranslationComplete(recognizedText)
            })
        } else {
            // Handle permission denied
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word Translation") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = state.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            if(state.query.isNotEmpty()){
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    state.query.forEachIndexed { index, char ->
                        Text(
                            text = char.toString(),
                            color = colorResource(id = state.textColors[index]),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            // Recording indicator
            if (state.isRecording) {
                FrequencyChart(state.rmsValues)
            } else if (state.error!=null){
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    ),
                    value = state.query,
                    onValueChange = { onEvent(TTEvent.UpdateQuery(it)) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Enter text here") },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(onClick = { onEvent(TTEvent.UpdateQuery("")) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear text")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            keyboardController?.hide()
                            if (state.query.isNotBlank()) {
                                onTranslationComplete(state.query)
                            }
                        }
                    )
                )

                IconButton(
                    onClick = {
                        if (state.query.isNotBlank()) {
                            onEvent(TTEvent.TranslateText(state.query))
                        } else {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                if(state.isRecording){
                                    onEvent(TTEvent.StopRecording)
                                } else {
                                    onEvent(TTEvent.UpdateIsRecording(true))
                                    onEvent(TTEvent.StartRecording(context) { recognizedText ->
                                        onEvent(TTEvent.UpdateQuery(recognizedText))
                                        onEvent(TTEvent.UpdateIsRecording(false))
                                        onTranslationComplete(recognizedText)
                                    })
                                }
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(if (state.query.isBlank()) R.drawable.baseline_mic_24 else R.drawable.baseline_send_24),
                        contentDescription = if (state.query.isBlank()) "Record audio" else "Send",
                        tint = if (state.isRecording) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextTranslatorScreenPreview() {
    TextTranslatorScreen(
        onBackClick = {},
        onTranslationComplete = {},
        state = TTState(query = "H"),
        onEvent = {}
    )
}