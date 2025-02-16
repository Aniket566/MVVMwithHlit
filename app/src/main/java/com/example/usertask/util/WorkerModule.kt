package com.example.usertask.util

import androidx.work.ListenableWorker
import com.example.usertask.retrofit.SyncWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@InstallIn(SingletonComponent::class)
@Module
interface WorkerBindingModule {
//    @Binds
//    @IntoMap
//    @WorkerKey(SyncWorker::class)
//    fun bindSyncWorkerFactory(factory: SyncWorkerFactory): WorkerAssistedFactory
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

