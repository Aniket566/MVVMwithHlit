package com.example.usertask.util

import android.content.Context
import com.example.usertask.repo.AddUserRepository
import com.example.usertask.retrofit.ApiService
import com.example.usertask.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }

    @Provides
    @Singleton
    fun provideAddUserRepository(
        @Named("userTasksApi") apiService: ApiService,
        userDao: UserDao
    ): AddUserRepository {
        return AddUserRepository(apiService, userDao)
    }
}