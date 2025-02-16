package com.example.usertask.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val localId: Int = 0,
    val name: String,
    val job: String,
    var serverId: String? = null,
    var isSynced: Int = 0
)
