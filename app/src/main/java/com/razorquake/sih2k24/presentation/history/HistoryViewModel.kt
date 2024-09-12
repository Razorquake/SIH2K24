package com.razorquake.sih2k24.presentation.history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razorquake.sih2k24.domain.models.SpeechLog
import com.razorquake.sih2k24.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val appUseCases: AppUseCases
): ViewModel() {
    private val _state = mutableStateOf(HistoryState())
    val state: State<HistoryState> = _state
    fun onEvent(event: HistoryEvent){
        when(event){
            is HistoryEvent.DeleteSpeechLog -> {
                viewModelScope.launch {
                    val speechLog = appUseCases.getSpeechLog(event.text)
                    if(speechLog!=null)
                    deleteSpeechLog(speechLog)
                }
            }
        }
    }
    private fun getSpeechLogs(){
        appUseCases.getAllSpeechLog().onEach {
            _state.value = state.value.copy(speechLogs = it.asReversed())
        }.launchIn(viewModelScope)
    }
    init {
        getSpeechLogs()
    }
    private suspend fun deleteSpeechLog(speechLog: SpeechLog){
        appUseCases.deleteSpeechLog(speechLog)
    }
}