package com.example.randmword.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randmword.data.models.DictionaryResponseFull
import com.example.randmword.data.remote.dictionary_api.createDictionaryService
import com.example.randmword.data.remote.ninja_api.createWordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    private val apiServiceWord = createWordService()
    private val apiServiceDictionary = createDictionaryService()

    init {
        getRandomWord()
    }

    private fun getRandomWord() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiServiceWord.getRandomWord()
               /* withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(word = response.word)
                    viewModelScope.launch(Dispatchers.IO) {
                        getDefinition(response.word)
                    }
                }*/
                viewModelScope.launch(Dispatchers.IO) {
                    getDefinition(response.word)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(word = "Ошибка: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun getDefinition(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiServiceDictionary.getDefinition(word)
                val definition = getFirstDefinition(response)

                if(definition.equals("HTTP 404")){
                    getRandomWord()
                }else{
                    _state.value = _state.value.copy(
                        definition = definition,
                        word = word
                    )
                }
                Log.d("TAG", definition.toString())
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.localizedMessage?.let { Log.e("TAG", it) }
                }
            }
        }
    }

    private fun getFirstDefinition(response: DictionaryResponseFull): String? {
        val firstItem = response.firstOrNull() ?: return null
        val firstMeaning = firstItem.meanings.firstOrNull() ?: return null
        val firstDefinition = firstMeaning.definitions.firstOrNull() ?: return null
        Log.i("TAG", firstDefinition.definition)
        return firstDefinition.definition
    }
}
