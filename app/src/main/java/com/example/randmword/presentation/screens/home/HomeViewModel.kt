package com.example.randmword.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randmword.data.remote.ninja_api.createApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(HomeState(word = "Загрузка..."))
    val state: StateFlow<HomeState> = _state
    private val apiService = createApiService()

    init {
        getRandomWord()
    }

    private fun getRandomWord() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getRandomWord()
                _state.value = _state.value.copy(word = response.word)
            } catch (e: Exception) {
                _state.value = _state.value.copy(word = "Ошибка: ${e.message}")
            }
        }
    }
}
