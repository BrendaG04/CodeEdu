
package com.example.codeedu.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels") // This tells Room to create a table named "levels"
data class Level(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val levelName: String,
    val isCompleted: Boolean,

    val parentUsername: String
)
