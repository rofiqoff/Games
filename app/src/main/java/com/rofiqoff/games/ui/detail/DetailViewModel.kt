package com.rofiqoff.games.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.repository.GameFavoriteRepository
import com.rofiqoff.games.data.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: GameRepository,
    private val favoriteRepository: GameFavoriteRepository,
) : ViewModel() {

    private val _stateDetail = MutableStateFlow<UiState<GameDetail>>(UiState.Loading)
    val stateDetail: StateFlow<UiState<GameDetail>> = _stateDetail

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private var dataDetail = GameDetail()

    private fun setDataDetail(data: GameDetail) {
        this.dataDetail = data
    }

    fun checkFavoriteState() {
        if (isFavorite.value) {
            deleteFavoriteGameById(dataDetail.id)
        } else {
            insertFavoriteGame()
        }
    }

    fun fetchDetailGame(slug: String) = viewModelScope.launch {
        repository.getGameDetail(slug)
            .onStart { _stateDetail.update { UiState.Loading } }
            .collect { result ->
                when (result) {
                    is AppResponse.Success -> {
                        val data = result.data
                        setDataDetail(data)
                        checkFavoriteGameById(data.id)

                        _stateDetail.update { UiState.Success(data) }
                    }

                    is AppResponse.Error -> {
                        _stateDetail.update { UiState.Error(result.message) }
                    }
                }
            }
    }

    private fun insertFavoriteGame() = viewModelScope.launch {
        favoriteRepository.insertFavoriteGame(dataDetail)
            .collect { result -> _isFavorite.update { result } }
    }

    private fun deleteFavoriteGameById(id: Long) = viewModelScope.launch {
        favoriteRepository.deleteFavoriteGameById(id)
            .collect { result -> _isFavorite.update { !result } }
    }

    private fun checkFavoriteGameById(id: Long) = viewModelScope.launch {
        favoriteRepository.checkFavoriteGameById(id)
            .collect { result -> _isFavorite.update { result } }
    }
}
