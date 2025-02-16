package com.example.usertask.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.usertask.model.User
import com.example.usertask.retrofit.ApiService

class UserPagingSource(private val apiService: ApiService) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getUsers(currentPage)

            LoadResult.Page(
                data = response.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.data.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
