package com.razorquake.sih2k24.text_translation

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TTViewModel: ViewModel() {
    private val _state = mutableStateOf(TTState())
    val state: State<TTState> = _state
    private lateinit var speechRecognizer: SpeechRecognizer
    fun onEvent(event: TTEvent) {
        when (event) {
            is TTEvent.UpdateQuery -> {
                _state.value = _state.value.copy(query = event.query)
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
        }
    }
    private fun startRecording(context: Context, onResult: (String) -> Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
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
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
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

    override fun onCleared() {
        super.onCleared()
        if(::speechRecognizer.isInitialized) {
            speechRecognizer.destroy()
        }
    }
}