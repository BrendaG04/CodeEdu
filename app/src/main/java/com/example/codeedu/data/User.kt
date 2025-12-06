package com.example.codeedu.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val username: String,

    val password: String,

    val role: String,
    val linkedParent: String?
)
    