package com.example.codeedu.data

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**File tracking*/
class ProgressFileLogger(private val context: Context) {
    private val file = File(context.filesDir, "progress_log.txt")

    fun log(username: String, event: String) {
        val ts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
        file.appendText("$ts | $username | $event\n")
    }

    fun readAll(): String = if (file.exists()) file.readText() else ""
}
