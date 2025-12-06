package com.example.codeedu


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.codeedu.screens.*
import com.example.codeedu.ui.theme.CodeEduTheme

/**Responsible for setting everything up at beginning*/
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeEduTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,

                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        SoundPlayer.playBackgroundMusic(this)
    }
}



/** Deals with the navigation of screens */
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("welcome") }
    var navPayload by remember { mutableStateOf("") }

    when (currentScreen) {

        "welcome" -> WelcomeScreen(
            onNavigate = { screen -> currentScreen = screen }
        )

        "register" -> RegisterScreen(
            onBack = { currentScreen = "welcome" },
            onSuccess = { currentScreen = "welcome" }
        )

        "login" -> LoginScreen(
            onLoginSuccess = { role, username ->
                navPayload = username
                currentScreen = if (role == "parent") "parent_dashboard" else "child_home"
            },
            onBack = { currentScreen = "welcome" }
        )

        "parent_dashboard" -> ParentDashboardScreen(
            parentUsername = navPayload,
            onLogout = { currentScreen = "welcome" },
            onChildClick = { childUsername ->
                navPayload = childUsername
                currentScreen = "child_home"
            }
        )


        "child_home" -> ChildDashboardScreen(
            childUsername = navPayload,
            onGameSelect = { level, game ->
                navPayload = "$navPayload|$level|$game"
                currentScreen = "level"
            },
            onLogout = { currentScreen = "welcome" }
        )

        "level" -> {
            val parts = navPayload.split("|")
            val username = parts.getOrNull(0) ?: ""
            val level = parts.getOrNull(1)?.toIntOrNull() ?: 1
            val game = parts.getOrNull(2)?.toIntOrNull() ?: 1

            LevelScreen(
                childUsername = username,
                levelNumber = level,
                startGameNumber = game,
                onBack = {
                    navPayload = username
                    currentScreen = "child_home"
                },
                onAllGamesComplete = {
                    navPayload = username
                    currentScreen = "child_home"
                }
            )
        }
    }
}
