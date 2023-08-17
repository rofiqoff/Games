package com.rofiqoff.games.ui.home

import app.cash.turbine.testIn
import com.rofiqoff.games.data.domain.helper.UiState
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.repository.GameRepositoryImpl
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService
import com.rofiqoff.games.data.implementation.sources.remote.response.GameResultResponse
import com.rofiqoff.games.data.implementation.sources.remote.response.GamesResponse
import com.rofiqoff.games.helper.MainDispatcherRule
import com.rofiqoff.games.utils.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.net.SocketTimeoutException

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var repository: GameRepository
    private lateinit var viewModel: HomeViewModel

    private val dummyResultResponse: GameResultResponse
        get() = GameResultResponse(
            1, "abc", "Test Name", "2023-08-17", "", 5.0
        )

    private val dummyResponse: GamesResponse
        get() = GamesResponse(
            7, "", "", listOf(dummyResultResponse)
        )

    private val dummyEmptyResponse: GamesResponse
        get() = GamesResponse(
            0, "", "", emptyList()
        )

    private fun initialize() {
        apiService = mock(ApiService::class.java)
        repository = GameRepositoryImpl(apiService)
        viewModel = HomeViewModel(repository)
    }

    // get all games
    @Test
    fun givenResponseSuccess_whenFetchListGame_shouldReturnListGame() = runTest {
        initialize()

        // given
        `when`(apiService.getAllGames(1)).thenReturn(dummyResponse)

        val state = viewModel.listGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.fetchListGame(1)

        // then
        val result = state.awaitItem()

        val expected = UiState.Success(dummyResponse.asGames)

        assertEquals(expected, result)

        assertTrue((result as UiState.Success).data.results.size == 1)
        assertEquals("Test Name", result.data.results[0].name)
    }

    @Test
    fun givenResponseSuccess_whenFetchListGame_shouldReturnListGameAndShowCorrectReleaseDate() =
        runTest {
            initialize()

            // given
            `when`(apiService.getAllGames(1)).thenReturn(dummyResponse)
            viewModel = HomeViewModel(repository)

            val state = viewModel.listGame.testIn(backgroundScope)
            assert(state.awaitItem() == UiState.Loading)

            // when
            viewModel.fetchListGame(1)

            // then
            val result = state.awaitItem()

            val expected = UiState.Success(dummyResponse.asGames)
            assertEquals(expected, result)

            val expectedDate = "Thursday, 17 August 2023"
            assertEquals(expectedDate, (result as UiState.Success).data.results[0].released)
        }

    @Test
    fun givenResponseSuccess_whenFetchListGame_shouldReturnEmptyResultListGame() = runTest {
        initialize()

        // given
        `when`(apiService.getAllGames(1)).thenReturn(dummyEmptyResponse)
        viewModel = HomeViewModel(repository)

        val state = viewModel.listGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.fetchListGame(1)

        // then
        val result = state.awaitItem()

        val expected = UiState.Success(dummyEmptyResponse.asGames)

        assertEquals(expected, result)

        assertTrue((result as UiState.Success).data.results.isEmpty())
    }

    @Test
    fun givenResponseError_whenFetchListGame_shouldReturnErrorMessage() = runTest {
        initialize()

        // given
        `when`(apiService.getAllGames(1)).then { throw SocketTimeoutException("Time out") }
        viewModel = HomeViewModel(repository)

        val state = viewModel.listGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.fetchListGame(1)

        // then
        val result = state.awaitItem()

        val expected = UiState.Error(Constants.GENERAL_ERROR_MESSAGE)

        assertEquals(expected, result)

        assertEquals(Constants.GENERAL_ERROR_MESSAGE, (result as UiState.Error).message)
    }

    // search games
    @Test
    fun givenResponseSuccess_whenSearchGame_shouldReturnListGame() = runTest {
        initialize()

        // given
        `when`(apiService.searchGames("test")).thenReturn(dummyResponse)
        viewModel = HomeViewModel(repository)

        val state = viewModel.searchGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.searchGame("test")

        // then
        val result = state.awaitItem()

        val expected = UiState.Success(dummyResponse.asGames)

        assertEquals(expected, result)

        assertTrue((result as UiState.Success).data.results.size == 1)
        assertEquals("Test Name", result.data.results[0].name)
    }

    @Test
    fun givenResponseSuccess_whenSearchGame_shouldReturnEmptyResultListGame() = runTest {
        initialize()

        // given
        `when`(apiService.searchGames("test")).thenReturn(dummyEmptyResponse)
        viewModel = HomeViewModel(repository)

        val state = viewModel.searchGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.searchGame("test")

        // then
        val result = state.awaitItem()

        val expected = UiState.Success(dummyEmptyResponse.asGames)

        assertEquals(expected, result)

        assertTrue((result as UiState.Success).data.results.isEmpty())
    }

    @Test
    fun givenResponseError_whenSearchGame_shouldReturnErrorMessage() = runTest {
        initialize()

        // given
        `when`(apiService.searchGames("test")).then { throw SocketTimeoutException("Time out") }
        viewModel = HomeViewModel(repository)

        val state = viewModel.searchGame.testIn(backgroundScope)
        assert(state.awaitItem() == UiState.Loading)

        // when
        viewModel.searchGame("test")

        // then
        val result = state.awaitItem()

        val expected = UiState.Error(Constants.GENERAL_ERROR_MESSAGE)

        assertEquals(expected, result)

        assertEquals(Constants.GENERAL_ERROR_MESSAGE, (result as UiState.Error).message)
    }
}
