package com.example.usertask.repo

import com.example.usertask.model.MovieDetail
import com.example.usertask.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieDetailsRepository @Inject constructor(
    @Named("moviesApi") private val apiService: ApiService
) {
    suspend fun getMovieDetails(movieId: Int, apiKey:String ): MovieDetail = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getMovieDetails(movieId,apiKey)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Movie details not found")
            } else {
                throw Exception("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch movie details: ${e.message}")
        }
    }
}

