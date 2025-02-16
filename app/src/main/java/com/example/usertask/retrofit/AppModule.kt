package com.example.usertask.retrofit

import android.content.Context
import androidx.room.Room
import com.example.usertask.BuildConfig
import com.example.usertask.repo.AddUserRepository
import com.example.usertask.repo.MovieDetailsRepository
import com.example.usertask.repo.MovieRepository
import com.example.usertask.repo.UserRepository
import com.example.usertask.room.AppDatabase
import com.example.usertask.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideReqresBaseUrl(): String = BuildConfig.REQRES_BASE_URL

    @Provides
    @Singleton
    fun provideTmdbBaseUrl(): String = BuildConfig.TMDB_BASE_URL

    @Provides
    @Singleton
    fun provideTmdbApiKey(): String = BuildConfig.TMDB_API_KEY

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("userTasksApi")
    fun provideApiServiceForUserTasks(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(provideReqresBaseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("moviesApi")
    fun provideApiServiceForMovies(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(provideTmdbBaseUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(@Named("userTasksApi") apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }

//    @Provides
//    @Singleton
//    fun provideAddUserRepository(@Named("userTasksApi") apiService: ApiService, userDao: UserDao): AddUserRepository {
//        return AddUserRepository(apiService, userDao)
//    }

    @Provides
    @Singleton
    fun provideMovieRepository(@Named("moviesApi") apiService: ApiService): MovieRepository {
        return MovieRepository(apiService, provideTmdbApiKey())
    }

    @Provides
    @Singleton
    fun provideMovieDetailsRepository(@Named("moviesApi") apiService: ApiService): MovieDetailsRepository {
        return MovieDetailsRepository(apiService)
    }
}

