package com.example.usertask.repo

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.usertask.model.Movie
import com.example.usertask.pagging.MoviePagingSource
import com.example.usertask.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import androidx.paging.Pager
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    @Named("moviesApi") private val apiService: ApiService,
    private val apiKey: String
) {
    fun getMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { MoviePagingSource(apiService, apiKey) }
    ).flow
}


