package com.example.usertask.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.usertask.model.UserEntity

@Database(entities = [UserEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
