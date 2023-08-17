package com.rofiqoff.games.ui.detail

import app.cash.turbine.testIn
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.repository.GameRepositoryImpl
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import com.rofiqoff.games.data.implementation.sources.remote.response.GameDetailResponse
import com.rofiqoff.games.helper.MainDispatcherRule
import com.rofiqoff.games.utils.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.net.SocketTimeoutException

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var repository: GameRepository
    private lateinit var viewModel: DetailViewModel

    private val dummyResponse: GameDetailResponse
        get() = GameDetailResponse(
            "Test Name", "abc", "2023-08-17", "", "", 5.0
        )

    private fun initialize() {
        apiService = mock(ApiService::class.java)
        repository = GameRepositoryImpl(apiService)
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun givenResponseSuccess_whenFetchDetailGame_shouldReturnGameDetail() = runTest {
        initialize()

        // given
        `when`(apiService.getGameDetail("slug")).thenReturn(dummyResponse)

        val state = viewModel.stateDetail.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.fetchDetailGame("slug")

        // then
        val result = state.awaitItem()

        val expected = UiState.Success(dummyResponse.asGameDetail)

        assertEquals(expected, result)
    }

    @Test
    fun givenResponseError_whenFetchDetailGame_shouldReturnError() = runTest {
        initialize()

        // given
        `when`(apiService.getGameDetail("slug")).then { throw SocketTimeoutException("Time out") }

        viewModel = DetailViewModel(repository)

        val state = viewModel.stateDetail.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.fetchDetailGame("slug")

        // then
        val result = state.awaitItem()

        val expected = UiState.Error(Constants.GENERAL_ERROR_MESSAGE)

        assertEquals(expected, result)

        assertEquals(Constants.GENERAL_ERROR_MESSAGE, (result as UiState.Error).message)
    }

    @Test
    fun givenResponseSuccess_whenFetchDetailGame_shouldReturnSuccessAndShowCorrectReleaseDate() =
        runTest {
            initialize()

            // given
            `when`(apiService.getGameDetail("slug")).thenReturn(dummyResponse)

            viewModel = DetailViewModel(repository)

            val state = viewModel.stateDetail.testIn(backgroundScope)
            assert(state.awaitItem() == UiState.Loading)

            // when
            viewModel.fetchDetailGame("slug")

            // then
            val result = state.awaitItem()

            val expected = UiState.Success(dummyResponse.asGameDetail)
            assertEquals(expected, result)

            val expectedDate = "Thursday, 17 August 2023"
            assertEquals(expectedDate, (result as UiState.Success).data.released)
        }
}
