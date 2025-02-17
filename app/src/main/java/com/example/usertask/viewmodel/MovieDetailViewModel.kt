package com.example.usertask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usertask.repo.MovieDetailsRepository
import com.example.usertask.util.MovieDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailsRepository
) : ViewModel() {

    private val _movieDetailState = MutableLiveData<MovieDetailState>()
    val movieDetailState: LiveData<MovieDetailState> = _movieDetailState

    fun loadMovieDetails(movieId: Int,apiKey:String) {
        viewModelScope.launch {
            _movieDetailState.value = MovieDetailState.Loading
            try {
                val result = repository.getMovieDetails(movieId,apiKey)
                _movieDetailState.value = MovieDetailState.Success(result)
            } catch (e: Exception) {
                _movieDetailState.value = MovieDetailState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

