package com.example.usertask.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.usertask.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE isSynced = 0")
    fun getUnsyncedUsers(): Flow<List<UserEntity>>

    @Query("UPDATE users SET serverId = :serverId, isSynced = 1 WHERE localId = :localId")
    suspend fun updateUserSyncStatus(localId: Int, serverId: String): Int
}
