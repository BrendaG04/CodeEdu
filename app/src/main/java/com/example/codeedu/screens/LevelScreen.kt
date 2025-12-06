package com.example.codeedu.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.codeedu.data.ProgressFileLogger
/**
 * LevelScreen -This is the main container for the games.
 * It uses a 'when' statement to decide which level s games to show
 * and tracks the player's progress for that level.
 */
@Composable
fun LevelScreen(
    childUsername: String,
    levelNumber: Int,
    onBack: () -> Unit,
    onAllGamesComplete: () -> Unit ,
    startGameNumber: Int,
) {
    var currentGame by remember { mutableStateOf(startGameNumber) }

    //File tracking progress
    val context = LocalContext.current
    val logger = remember { ProgressFileLogger(context) }

    // This is where we select which game to display based on the level Number and current Game
    when (levelNumber) {
        1 -> {
            when (currentGame) {
                1 -> BeginnerGame(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { currentGame = 2 },
                    onBack = onBack
                )
                2 -> LevelGame2(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { currentGame = 3 },
                    onBack = { currentGame =  1}
                )
                3 -> LevelGame3(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { onAllGamesComplete() },
                    onBack = { currentGame = 2 }
                )
                else -> {
                    Text("Congratulations! You finished Level 1!")
                }
            }
        }
        2 -> {
            when (currentGame) {
                1 -> Level2Game1(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { currentGame = 2 },
                    onBack = onBack
                )
                2 -> Level2Game2(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { currentGame = 3 },
                    onBack = { currentGame = 1 }
                )
                3 -> Level2Game3(
                    level = levelNumber,
                    childUsername = childUsername,
                    logger = logger,
                    onSuccess = { onAllGamesComplete() },
                    onBack = { currentGame = 2 }
                )
                else -> {
                    Text("Congratulations! You finished Level 2!")
                }
            }
        }
        else -> {
            Text("This level ($levelNumber) has not been created yet :(.")
        }
    }
}
