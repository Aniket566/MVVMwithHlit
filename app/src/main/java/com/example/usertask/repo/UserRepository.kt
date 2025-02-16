package com.example.usertask.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.usertask.model.User
import com.example.usertask.pagging.UserPagingSource
import com.example.usertask.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @Named("userTasksApi") private val apiService: ApiService
) {
    fun getUsers(): Flow<PagingData<User>> = Pager(
        config = PagingConfig(pageSize = 12, enablePlaceholders = false),
        pagingSourceFactory = { UserPagingSource(apiService) }
    ).flow
}


