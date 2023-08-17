package com.rofiqoff.games.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.model.Games
import com.rofiqoff.games.data.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GameRepository,
) : ViewModel() {

    private val _listGame = MutableStateFlow<UiState<Games>>(UiState.Loading)
    val listGame: StateFlow<UiState<Games>> = _listGame

    private val _searchGame = MutableStateFlow<UiState<Games>>(UiState.Loading)
    val searchGame: StateFlow<UiState<Games>> = _searchGame

    fun fetchListGame(page: Int) = viewModelScope.launch {
        repository.getAllGames(page)
            .collect { result ->
                when (result) {
                    is AppResponse.Success -> {
                        _listGame.update { UiState.Success(result.data) }
                    }

                    is AppResponse.Error -> {
                        _listGame.update { UiState.Error(result.message) }
                    }
                }
            }
    }

    fun searchGame(query: String) = viewModelScope.launch {
        repository.searchGame(query)
            .collect { result ->
                when (result) {
                    is AppResponse.Success -> {
                        _searchGame.update { UiState.Success(result.data) }
                    }

                    is AppResponse.Error -> {
                        _searchGame.update { UiState.Error(result.message) }
                    }
                }
            }
    }

}
