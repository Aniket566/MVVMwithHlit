package com.example.usertask.util

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.usertask.retrofit.SyncWorker
import dagger.assisted.AssistedFactory

interface WorkerAssistedFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}


