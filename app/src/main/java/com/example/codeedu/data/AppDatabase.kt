package com.example.codeedu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** This is the main database class for the entire app.
*/
@Database(entities = [User::class, Level::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // This abstract function connects the database to our User DAO.
    abstract fun userDao(): UserDao

    abstract fun levelDao(): LevelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // This block ensures that only one thread can
            // create the database at a time, preventing conflicts.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "code_edu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
