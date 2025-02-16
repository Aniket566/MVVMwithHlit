package com.example.usertask.repo

import android.util.Log
import com.example.usertask.model.UserEntity
import com.example.usertask.retrofit.ApiService
import com.example.usertask.room.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class AddUserRepository @Inject constructor(
    @Named("userTasksApi") private val apiService: ApiService,
    private val userDao: UserDao
) {
    suspend fun createUser(user: UserEntity, isOnline: Boolean) {
        if (isOnline) {
            try {
                val response = apiService.createUser(user)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val updatedUser = user.copy(serverId = it.id, isSynced = 1)
                        userDao.insertUser(updatedUser)
                    }
                } else {
                    userDao.insertUser(user.copy(isSynced = 0))
                }
            } catch (e: Exception) {
                userDao.insertUser(user.copy(isSynced = 0))
            }
        } else {
            userDao.insertUser(user.copy(isSynced = 0))
        }
    }

    suspend fun syncUnsyncedUsers(): Int {
        var syncedCount = 0
        val unsyncedUsers = userDao.getUnsyncedUsers().first()

        Log.d("AddUserRepository", "Unsynced users: ${unsyncedUsers.size}")

        unsyncedUsers.forEach { user ->
            try {
                val response = apiService.createUser(user)
                if (response.isSuccessful) {
                    response.body()?.let {
                        userDao.updateUserSyncStatus(user.localId, it.id)
                        syncedCount++
                    }
                }
            } catch (e: Exception) {
                Log.e("AddUserRepository", "Error syncing user: ${e.message}")
            }
        }
        return syncedCount
    }
}
