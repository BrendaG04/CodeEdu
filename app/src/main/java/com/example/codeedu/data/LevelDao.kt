

package com.example.codeedu.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**Deals with the sqlite query */
@Dao
interface LevelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(level: Level)

    @Query("SELECT * FROM levels WHERE parentUsername = :username")
    suspend fun getLevelsForUser(username: String): List<Level>
}