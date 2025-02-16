package com.example.usertask.util

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WorkManagerModule {
    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(
        workerFactory: MyWorkerFactory
    ): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context,
        configuration: Configuration
    ): WorkManager {
        WorkManager.initialize(context, configuration)
        return WorkManager.getInstance(context)
    }
}