package com.example.codeedu.data

import android.content.Context

/**
 * UserRepository is the middle man between the UI and database.
 * It provides a API for the rest of the app to access user data.
 */
class UserRepository(context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun findByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    suspend fun registerParent(username: String, password: String): Result<Unit> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Username and password cannot be empty."))
        }

        if (findByUsername(username) != null) {
            return Result.failure(Exception("Username '$username' is already taken."))
        }

        val newUser = User(
            username = username,
            password = password,
            role = "parent",
            linkedParent = null
        )

        userDao.insertUser(newUser)
        return Result.success(Unit)
    }

    suspend fun registerChild(childUsername: String, childPassword: String, parentUsername: String): Result<Unit> {
        // Basic validation
        if (childUsername.isBlank() || childPassword.isBlank()) {
            return Result.failure(Exception("Username and password cannot be empty."))
        }
        if (parentUsername.isBlank()) {
            return Result.failure(Exception("Parent username must be provided."))
        }

        if (findByUsername(childUsername) != null) {
            return Result.failure(Exception("Username '$childUsername' is already taken."))
        }

        val parent = findByUsername(parentUsername)
        if (parent == null || parent.role != "parent") {
            return Result.failure(Exception("Parent with username '$parentUsername' not found."))
        }

        val newUser = User(
            username = childUsername,
            password = childPassword,
            role = "child",
            linkedParent = parentUsername
        )

        userDao.insertUser(newUser)
        return Result.success(Unit)
    }

    suspend fun getChildrenForParent(parentUsername: String): List<User> {
        return userDao.getChildrenForParent(parentUsername)
    }


}
