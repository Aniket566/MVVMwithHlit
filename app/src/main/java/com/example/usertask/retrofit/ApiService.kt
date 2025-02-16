package com.example.usertask.retrofit

import com.example.usertask.model.AddUserResponse
import com.example.usertask.model.MovieDetail
import com.example.usertask.model.MovieResponse
import com.example.usertask.model.UserEntity
import com.example.usertask.model.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int): UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserEntity): Response<AddUserResponse>

    data class ApiResponse(val id: String)

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = "1a687b46c269af537f430499ec451057"
    ): Response<MovieDetail>
}
