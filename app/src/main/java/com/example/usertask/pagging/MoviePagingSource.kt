package com.example.usertask.pagging


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.usertask.model.Movie
import com.example.usertask.retrofit.ApiService

class MoviePagingSource(
    private val apiService: ApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getTrendingMovies(page = page, apiKey = apiKey)

            LoadResult.Page(
                data = response.movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.movies.isNotEmpty()) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
