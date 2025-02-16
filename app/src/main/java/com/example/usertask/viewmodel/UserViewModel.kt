package com.example.usertask.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.usertask.model.User
import com.example.usertask.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    init {
        Log.d("HiltTest", "UserRepository is injected successfully!")
    }

    val userList: Flow<PagingData<User>> = repository.getUsers().cachedIn(viewModelScope)
}
