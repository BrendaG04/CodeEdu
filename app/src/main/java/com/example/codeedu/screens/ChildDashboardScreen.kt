package com.example.codeedu.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**Child's screen to select a game to play*/
@Composable
fun ChildDashboardScreen(
    childUsername: String,
    onGameSelect: (Int, Int) -> Unit,
    onLogout: () -> Unit
) {
    val levels = (1..2).toList()
    val gamesPerLevel = (1..3).toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Welcome, $childUsername", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select a game to start:", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            levels.forEach { levelNumber ->
                item {
                    Text(
                        "Level $levelNumber",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(gamesPerLevel.size) { gameIndex ->
                    val gameNumber = gameIndex + 1
                    Button(
                        onClick = { onGameSelect(levelNumber, gameNumber) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Game $gameNumber")
                    }
                }
            }

            // Adds the logout button at the end of the list
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    }
}
