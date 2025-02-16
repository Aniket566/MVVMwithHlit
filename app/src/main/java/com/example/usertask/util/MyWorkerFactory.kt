package com.example.usertask.util

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>, @JvmSuppressWildcards WorkerAssistedFactory>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry = workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factory = foundEntry?.value ?: return null
        return factory.create(appContext, workerParameters)
    }
}




