package com.example.codeedu.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.codeedu.data.ProgressFileLogger
import com.example.codeedu.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**Parent's screen
 * No games, only childs'activity*/
@Composable
fun ParentDashboardScreen(
    parentUsername: String,
    onChildClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    val ctx = LocalContext.current
    val repo = remember { UserRepository(ctx) }
    val logger = remember { ProgressFileLogger(ctx) }

    var children by remember { mutableStateOf(listOf<com.example.codeedu.data.User>()) }
    var progressMap by remember { mutableStateOf(mapOf<String, String>()) }

    // Loads children linked to this parent
    LaunchedEffect(parentUsername) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = repo.getChildrenForParent(parentUsername)
            val map = mutableMapOf<String, String>()

            list.forEach { child ->
                val logs = logger.readAll()
                    .lines()
                    .filter { it.contains(child.username) }
                    .joinToString("\n")
                map[child.username] = if (logs.isBlank()) "No progress yet" else logs
            }

            CoroutineScope(Dispatchers.Main).launch {
                children = list
                progressMap = map
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Parent Dashboard", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (children.isEmpty()) {
            Text("No linked children yet")
        } else {
            LazyColumn {
                items(children) { child ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onChildClick(child.username) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(child.username, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = progressMap[child.username] ?: "No progress yet",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}


