package com.rofiqoff.games.ui.home

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.testIn
import com.rofiqoff.games.data.domain.model.GameResult
import com.rofiqoff.games.data.domain.repository.GameRepository
import com.rofiqoff.games.data.implementation.sources.remote.response.GameResultResponse
import com.rofiqoff.games.data.implementation.sources.remote.response.GamesResponse
import com.rofiqoff.games.helper.MainDispatcherRule
import com.rofiqoff.games.ui.dashboard.home.HomeAdapter
import com.rofiqoff.games.ui.dashboard.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: GameRepository
    private lateinit var viewModel: HomeViewModel

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private val dummyResultResponse: GameResultResponse
        get() = GameResultResponse(
            1, "abc", "Test Name", "2023-08-17", "", 5.0
        )

    private val dummyResponse: GamesResponse
        get() = GamesResponse(
            7, "", "", listOf(dummyResultResponse)
        )

    private fun initialize() {
        repository = mock(GameRepository::class.java)
        viewModel = HomeViewModel(repository)
    }

    // get all games
    @Test
    fun givenResponseSuccess_whenFetchListGame_shouldReturnListGameResultInPagingData() = runTest {
        initialize()

        val pagingData = PagingData.from(dummyResponse.asGames.results)
        val expectedDummy = MutableStateFlow(pagingData)

        // given
        `when`(repository.getAllGames()).thenReturn(expectedDummy)

        val state = viewModel.dataGames.testIn(backgroundScope)
        assert(state.awaitItem() == null)

        //when
        viewModel.fetchListGame()

        // then
        val result = state.awaitItem()
        assertNotNull(result)

        val differ = asyncPagingDataDiffer(result)

        assertNotNull(differ.snapshot())
        assertEquals(dummyResponse.asGames.results.size, differ.snapshot().size)

    }

    @Test
    fun givenResponseSuccess_whenFetchSearchGame_shouldReturnListGameResultInPagingData() =
        runTest {
            initialize()
            viewModel = HomeViewModel(repository)

            val pagingData = PagingData.from(dummyResponse.asGames.results)
            val expectedDummy = MutableStateFlow(pagingData)

            // given
            `when`(repository.searchGame("test")).thenReturn(expectedDummy)

            val dataResult = viewModel.dataGames.testIn(backgroundScope)
            assert(dataResult.awaitItem() == null)

            // when
            viewModel.searchGame("test")

            // then
            val result = dataResult.awaitItem()
            assertNotNull(result)

            val differ = asyncPagingDataDiffer(result)

            assertNotNull(differ.snapshot())
            assertEquals(dummyResponse.asGames.results.size, differ.snapshot().size)

        }

    @Test
    fun givenResponseSuccess_whenFetchListGame_shouldReturnListGameAndShowCorrectReleaseDate() =
        runTest {
            initialize()

            val pagingData = PagingData.from(dummyResponse.asGames.results)
            val expectedDummy = MutableStateFlow(pagingData)

            // given
            `when`(repository.getAllGames()).thenReturn(expectedDummy)

            val state = viewModel.dataGames.testIn(backgroundScope)
            assert(state.awaitItem() == null)

            // when
            viewModel.fetchListGame()

            // then
            val result = state.awaitItem()
            assertNotNull(result)

            val differ = asyncPagingDataDiffer(result)

            assertNotNull(differ.snapshot())

            val expectedDate = "Thursday, 17 August 2023"
            assertEquals(expectedDate, differ.snapshot()[0]?.released)
        }

    private suspend fun asyncPagingDataDiffer(result: PagingData<GameResult>?): AsyncPagingDataDiffer<GameResult> {
        return AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_UTIL,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        ).also {
            result?.let { data -> it.submitData(data) }
        }
    }
}
