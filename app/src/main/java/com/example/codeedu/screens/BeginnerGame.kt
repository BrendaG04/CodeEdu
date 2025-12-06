package com.example.codeedu.screens

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.ui.platform.LocalContext
import com.example.codeedu.SoundPlayer
import com.example.codeedu.data.ProgressFileLogger


// Defines the commands the player can use.
enum class Command { UP, DOWN, RIGHT , LEFT}

/**Functionality for level 1 game 1- includes the game content ui*/
@Composable
fun BeginnerGame(
    level: Int,
    childUsername: String,
    logger: ProgressFileLogger,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val commandSequence = remember { mutableStateListOf<Command?>(null, null, null) }
    var characterPosition by remember { mutableStateOf(Offset(20f, 25f)) }
    val animatedPosition by animateOffsetAsState(targetValue = characterPosition, label = "")

    val targetPosition = if (level == 1) Offset(275f, 150f) else Offset(150f, 200f)
    val successConditionMet = (animatedPosition.x in targetPosition.x - 10f..targetPosition.x + 10f) &&
            (animatedPosition.y in targetPosition.y - 10f..targetPosition.y + 10f)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE0F7FA)).padding(8.dp)) {
        Text("COMMANDS (Long press to drag)", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CommandSource(Command.UP, Icons.Default.ArrowUpward)
            CommandSource(Command.DOWN, Icons.Default.ArrowDownward)
            CommandSource(Command.RIGHT, Icons.Default.ArrowForward)
            CommandSource(Command.LEFT, Icons.Default.ArrowBack)

        }
        Divider()

        Text("YOUR SEQUENCE-LEVEL 1 GAME 1", style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0 until commandSequence.size) {
                CommandTarget(
                    command = commandSequence[i],
                    onDrop = { droppedCommand -> commandSequence[i] = droppedCommand }
                )
            }
        }
        Divider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val gridSize = 325f
                val strokeWidth = 10f
                val color = Color.LightGray

                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = color,
                        start = Offset(x = x, y = 0f),
                        end = Offset(x = x, y = size.height),
                        strokeWidth = strokeWidth
                    )
                    x += gridSize
                }

                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = y),
                        end = Offset(x = size.width, y = y),
                        strokeWidth = strokeWidth
                    )
                    y += gridSize
                }
            }
            Box(modifier = Modifier.size(75.dp).offset(targetPosition.x.dp, targetPosition.y.dp).background(Color.Green))
            Box(modifier = Modifier.size(75.dp).offset(animatedPosition.x.dp, animatedPosition.y.dp).background(Color.Red))
        }


        if (successConditionMet) {
            Text("SUCCESS!", color = Color.Green, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            SoundPlayer.playSuccessSound(context = LocalContext.current)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = {
                scope.launch {
                    var currentPos = Offset(20f, 25f)
                    characterPosition = currentPos
                    delay(200)

                    for (command in commandSequence) {
                        currentPos = when (command) {
                            Command.UP -> currentPos.copy(y = currentPos.y - 125f)
                            Command.DOWN -> currentPos.copy(y = currentPos.y + 125f)
                            Command.RIGHT -> currentPos.copy(x = currentPos.x + 125f)
                            Command.LEFT -> currentPos.copy(x = currentPos.x - 125f)
                            null -> currentPos
                        }
                        characterPosition = currentPos
                        delay(600)
                    }
                    if (characterPosition.x in targetPosition.x - 10f..targetPosition.x + 10f &&
                        characterPosition.y in targetPosition.y - 10f..targetPosition.y + 10f) {
                        val event = "Completed Level $level, Game 1"
                        logger.log(childUsername, event)
                        delay(500)
                        onSuccess()
                    }
                }
            }) { Text("Run")
            }

            Button(onClick = {
                commandSequence.fill(null)
                characterPosition = Offset(20f, 25f)
            }) { Text("Reset") }

            Button(onClick = onBack) { Text("Back") }
        }
    }
}

@Composable
fun CommandSource(command: Command, icon: ImageVector) {
    Icon(
        imageVector = icon,
        contentDescription = command.name,
        modifier = Modifier
            .size(60.dp)
            .background(Color.Yellow, shape = MaterialTheme.shapes.medium)
            .padding(8.dp)
            .dragAndDropSource(
                transferData = {
                    DragAndDropTransferData(
                        clipData = ClipData.newPlainText("command", command.name)
                    )
                }
            )

    )
}


// A target where the player can drop a command
@Composable
fun CommandTarget(command: Command?, onDrop: (Command) -> Unit) {
    var isDraggingOver by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(80.dp)
            .border(2.dp, if (isDraggingOver) Color.Blue else Color.Gray)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                },
                target = remember {
                    object : DragAndDropTarget {
                        override fun onDrop(event: DragAndDropEvent): Boolean {
                            val droppedCmdName = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString()
                            onDrop(Command.valueOf(droppedCmdName))
                            isDraggingOver = false
                            return true
                        }
                        override fun onEntered(event: DragAndDropEvent) { isDraggingOver = true }
                        override fun onExited(event: DragAndDropEvent) { isDraggingOver = false }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (command != null) {
            Icon(
                imageVector = when(command) {
                    Command.UP -> Icons.Default.ArrowUpward
                    Command.DOWN -> Icons.Default.ArrowDownward
                    Command.RIGHT -> Icons.Default.ArrowForward
                    Command.LEFT -> Icons.Default.ArrowBack
                },
                contentDescription = command.name,
                modifier = Modifier.size(40.dp)
            )
        } else {
            Text(text = "Drop Here", textAlign = TextAlign.Center, fontSize = 12.sp)
        }
    }
}
