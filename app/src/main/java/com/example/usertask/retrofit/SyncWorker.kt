package com.example.usertask.retrofit

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.usertask.repo.AddUserRepository
import com.example.usertask.util.NetworkUtils
import com.example.usertask.util.SyncWorkerEntryPoint
import dagger.hilt.android.EntryPointAccessors


class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val repository: AddUserRepository
    private val networkUtils: NetworkUtils

    init {
        val appContext = applicationContext
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            SyncWorkerEntryPoint::class.java
        )
        repository = hiltEntryPoint.getAddUserRepository()
        networkUtils = hiltEntryPoint.getNetworkUtils()
    }

    override suspend fun doWork(): Result {
        return try {
            if (!networkUtils.isOnline()) return Result.retry()

            val syncResult = repository.syncUnsyncedUsers()
            Log.d("SyncWorker", "Synced $syncResult users.")

            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed: ${e.message}")
            Result.retry()
        }
    }
}
