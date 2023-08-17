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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: GameRepository,
) : ViewModel() {

    private val _listGame = MutableStateFlow<UiState<Games>>(UiState.Idle)
    val listGame: StateFlow<UiState<Games>> = _listGame

    fun fetchListGame(page: Int) = viewModelScope.launch {
        repository.getAllGames(page)
            .onStart { _listGame.update { UiState.Loading } }
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

}
