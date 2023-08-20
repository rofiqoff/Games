package com.rofiqoff.games.ui.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rofiqoff.games.data.domain.model.GameResult
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

    private val _dataGames = MutableStateFlow<PagingData<GameResult>?>(null)
    val dataGames: StateFlow<PagingData<GameResult>?> = _dataGames

    fun fetchListGame() = viewModelScope.launch {
        repository.getAllGames().cachedIn(viewModelScope)
            .collect { result ->
                _dataGames.update { result }
            }
    }

    fun searchGame(query: String) = viewModelScope.launch {
        repository.searchGame(query).cachedIn(viewModelScope)
            .collect { result ->
                _dataGames.update { result }
            }
    }

}
