package com.rofiqoff.games.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.repository.GameFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameFavoriteViewModel @Inject constructor(
    private val repository: GameFavoriteRepository,
) : ViewModel() {

    private val _listGame = MutableStateFlow<List<GameDetail>>(emptyList())
    val listGame: StateFlow<List<GameDetail>> = _listGame

    fun fetchListGame() = viewModelScope.launch {
        repository.fetchAllFavoriteGames()
            .collect { result -> _listGame.update { result } }
    }

}
