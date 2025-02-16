package com.example.usertask.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("results") val movies: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("original_language") val language: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val rating: Double
) {
    fun getPosterUrl(): String = "https://image.tmdb.org/t/p/w500$posterPath"
}

