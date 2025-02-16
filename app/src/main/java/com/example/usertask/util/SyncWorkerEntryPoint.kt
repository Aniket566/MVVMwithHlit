package com.example.usertask.util

import com.example.usertask.repo.AddUserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SyncWorkerEntryPoint {
    fun getAddUserRepository(): AddUserRepository
    fun getNetworkUtils(): NetworkUtils
}
