package com.razorquake.sih2k24.presentation.text_translation

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorquake.sih2k24.R
import com.razorquake.sih2k24.domain.SpeechLog
import com.razorquake.sih2k24.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TTViewModel @Inject constructor(
    private val appUseCases: AppUseCases
) : ViewModel() {
    private val _state = mutableStateOf(TTState())
    val state: State<TTState> = _state
    private lateinit var speechRecognizer: SpeechRecognizer
    fun onEvent(event: TTEvent) {
        when (event) {
            is TTEvent.UpdateQuery -> {
                _state.value = _state.value.copy(query = event.query).updateTextColors()
            }

            is TTEvent.UpdateIsRecording -> {
                _state.value = _state.value.copy(isRecording = event.isRecording)
            }

            is TTEvent.StartRecording -> {
                startRecording(event.context, event.onResult)
            }

            is TTEvent.RecordingError -> {
                _state.value = _state.value.copy(error = event.error)
                viewModelScope.launch {
                    delay(5000) // Show error for 5 seconds
                    _state.value = _state.value.copy(error = null)
                }
            }

            TTEvent.StopRecording -> {
                stopRecording()
            }

            is TTEvent.TranslateText -> {
                viewModelScope.launch {
                    textToSpeech(event.text)
                    appUseCases.insertSpeechLog(
                        SpeechLog(
                            text = event.text,
                            timestamp = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }

    private fun startRecording(context: Context, onResult: (String) -> Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                }
            }

            // Implement other RecognitionListener methods as needed
            override fun onReadyForSpeech(params: android.os.Bundle?) {
                Log.d("SpeechRecognition", "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d("SpeechRecognition", "Beginning of speech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                Log.d("SpeechRecognition", "RMS changed: $rmsdB")
                _state.value = _state.value.copy(rmsValues = _state.value.rmsValues + rmsdB)
            }

            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                Log.d("SpeechRecognition", "Speech ended")
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }
                Log.e("SpeechRecognition", "Error: $errorMessage")
                onEvent(TTEvent.RecordingError(errorMessage))
            }

            override fun onPartialResults(partialResults: android.os.Bundle?) {
                val matches =
                    partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                }
            }

            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        })

        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun stopRecording() {
        speechRecognizer.stopListening()
        _state.value = _state.value.copy(isRecording = false)
    }

    private suspend fun textToSpeech(text: String) {
        withContext(Dispatchers.Main) {
            text.forEachIndexed { index, char ->
                _state.value = _state.value.copy(image = when (char) {
                    '0' -> R.drawable.zero
                    '1' -> R.drawable.one
                    '2' -> R.drawable.two
                    '3' -> R.drawable.three
                    '4' -> R.drawable.four
                    '5' -> R.drawable.five
                    '6' -> R.drawable.six
                    '7' -> R.drawable.seven
                    '8' -> R.drawable.eight
                    '9' -> R.drawable.nine
                    'a' -> R.drawable.a
                    'b' -> R.drawable.b
                    'c' -> R.drawable.c
                    'd' -> R.drawable.d
                    'e' -> R.drawable.e
                    'f' -> R.drawable.f
                    'g' -> R.drawable.g
                    'h' -> R.drawable.h
                    'i' -> R.drawable.i
                    'j' -> R.drawable.j
                    'k' -> R.drawable.k
                    'l' -> R.drawable.l
                    'm' -> R.drawable.m
                    'n' -> R.drawable.n
                    'o' -> R.drawable.o
                    'p' -> R.drawable.p
                    'q' -> R.drawable.q
                    'r' -> R.drawable.r
                    's' -> R.drawable.s
                    't' -> R.drawable.t
                    'u' -> R.drawable.u
                    'v' -> R.drawable.v
                    'w' -> R.drawable.w
                    'x' -> R.drawable.x
                    'y' -> R.drawable.y
                    'z' -> R.drawable.z
                    'A' -> R.drawable.a
                    'B' -> R.drawable.b
                    'C' -> R.drawable.c
                    'D' -> R.drawable.d
                    'E' -> R.drawable.e
                    'F' -> R.drawable.f
                    'G' -> R.drawable.g
                    'H' -> R.drawable.h
                    'I' -> R.drawable.i
                    'J' -> R.drawable.j
                    'K' -> R.drawable.k
                    'L' -> R.drawable.l
                    'M' -> R.drawable.m
                    'N' -> R.drawable.n
                    'O' -> R.drawable.o
                    'P' -> R.drawable.p
                    'Q' -> R.drawable.q
                    'R' -> R.drawable.r
                    'S' -> R.drawable.s
                    'T' -> R.drawable.t
                    'U' -> R.drawable.u
                    'V' -> R.drawable.v
                    'W' -> R.drawable.w
                    'X' -> R.drawable.x
                    'Y' -> R.drawable.y
                    'Z' -> R.drawable.z
                    else -> R.drawable.space
                },
                    textColors = List(_state.value.textColors.size) { i ->
                        if (i <= index)
                            R.color.text_title
                        else
                            R.color.body

                    }
                )
                delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::speechRecognizer.isInitialized) {
            speechRecognizer.destroy()
        }
    }
}