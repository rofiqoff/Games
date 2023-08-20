package com.rofiqoff.games.data.implementation.sources.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rofiqoff.games.data.domain.model.GameResult
import com.rofiqoff.games.data.implementation.sources.remote.api.ApiService

class GamePagingSource(
    private val service: ApiService,
    private val query: String = "",
) : PagingSource<Int, GameResult>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    private var nextUrl = ""

    override fun getRefreshKey(state: PagingState<Int, GameResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameResult> {
        return kotlin.runCatching {
            val nextPageNumber = params.key ?: INITIAL_PAGE_INDEX

            val response = when (nextPageNumber) {
                1 -> {
                    if (query.isEmpty())
                        service.getAllGames(nextPageNumber)
                    else
                        service.searchGames(query)
                }

                else -> service.getGamesByDynamicUrl(nextUrl)
            }

            nextUrl = response.next.orEmpty()

            val games = response.asGames

            LoadResult.Page(
                data = games.results,
                prevKey = if (nextPageNumber == INITIAL_PAGE_INDEX) null else nextPageNumber - 1,
                nextKey = if (games.results.isEmpty()) null else nextPageNumber + 1
            )
        }.getOrElse { error ->
            LoadResult.Error(error)
        }
    }
}
