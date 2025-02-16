package com.example.usertask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usertask.model.UserEntity
import com.example.usertask.repo.AddUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val repository: AddUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun createUser(name: String, job: String, isOnline: Boolean) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val user = UserEntity(name = name, job = job)
                repository.createUser(user, isOnline)
                _uiState.value = UiState.Success(isOnline)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val isOnline: Boolean) : UiState()
        data class Error(val message: String) : UiState()
    }
}


