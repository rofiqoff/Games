package com.rofiqoff.games.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rofiqoff.games.configuration.network.AppResponse
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.model.GameDetail
import com.rofiqoff.games.data.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: GameRepository,
) : ViewModel() {

    private val _stateDetail = MutableStateFlow<UiState<GameDetail>>(UiState.Loading)
    val stateDetail: StateFlow<UiState<GameDetail>> = _stateDetail

    fun fetchDetailGame(slug: String) = viewModelScope.launch {
        repository.getGameDetail(slug)
            .collect { result ->
                when (result) {
                    is AppResponse.Success -> {
                        _stateDetail.update { UiState.Success(result.data) }
                    }

                    is AppResponse.Error -> {
                        _stateDetail.update { UiState.Error(result.message) }
                    }
                }
            }
    }
}
