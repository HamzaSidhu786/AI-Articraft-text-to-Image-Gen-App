package com.hamza.aiarticraft.ui.main

import androidx.lifecycle.ViewModel
import com.hamza.aiarticraft.data.PromptData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PromptViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PromptData())
    val uiState: StateFlow<PromptData> = _uiState.asStateFlow()

    fun setPrompt(prompt: String) {
        _uiState.update { currentState ->
            currentState.copy(
                prompt = prompt
            )
        }
    }

    fun setBase64Code(base64: String) {
        _uiState.update { currentState ->
            currentState.copy(
                base64Code = base64
            )
        }
    }

}