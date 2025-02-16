package com.example.usertask.util

import com.example.usertask.model.MovieDetail


sealed class MovieDetailState {
    object Loading : MovieDetailState()
    data class Success(val movie: MovieDetail) : MovieDetailState()
    data class Error(val message: String) : MovieDetailState()
}
