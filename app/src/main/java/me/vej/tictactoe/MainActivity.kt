package me.vej.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.vej.tictactoe.ui.theme.TicTacToeTheme
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToe()
            }
        }
    }
}

// For some reason the values list doesn't clear properly so we use this in conjunction
var ticTacToeCurrentState = Array(9) { "" }
var currentPlayer = 0
var isGameRunning = true
var winner = -2
var player1Name = "Player 1"
var player2Name = "Player 2"
var allowedToClick = true

@Composable
fun TicTacToe() {
    Surface(color = MaterialTheme.colorScheme.background) {
        val values = remember { mutableStateListOf<String>() }
        repeat(9) {
            values.add("")
        }
        val dialogOpened = remember { mutableStateOf(false) }
        val changeNamesDialogOpened = remember { mutableStateOf(false) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(180,180,255)), modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.aspectRatio(1f)) {
                    Row(modifier = Modifier
                        .weight(1f)
                        .padding(24.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center)
                    {
                        TicTacToeButton(values[0], onValueChange = { newCount, id -> values[id] = newCount },1, 1, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[1], onValueChange = { newCount, id -> values[id] = newCount },1, 2, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[2], onValueChange = { newCount, id -> values[id] = newCount },1, 3, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                    }
                    Row(modifier = Modifier
                        .weight(1f)
                        .padding(24.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center)
                    {
                        TicTacToeButton(values[3], onValueChange = { newCount, id -> values[id] = newCount },2, 1, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[4], onValueChange = { newCount, id -> values[id] = newCount },2, 2, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[5], onValueChange = { newCount, id -> values[id] = newCount },2, 3, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                    }
                    Row(modifier = Modifier
                        .weight(1f)
                        .padding(24.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center)
                    {
                        TicTacToeButton(values[6], onValueChange = { newCount, id -> values[id] = newCount },3, 1, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[7], onValueChange = { newCount, id -> values[id] = newCount },3, 2, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                        TicTacToeButton(values[8], onValueChange = { newCount, id -> values[id] = newCount },3, 3, onDialogChange = {newDialogState -> dialogOpened.value = newDialogState})
                    }
                }
            }
            Button(
                modifier = Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(150, 30, 150)),
                onClick = {
                    changeNamesDialogOpened.value = true
                }
            ) {
                Text(text = "Choose Player Names", fontSize = 25.sp)
            }
            Button(
                modifier = Modifier
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    reset()
                    values.clear()
                    repeat(9) {
                        values.add("")
                    }
                }
            ) {
                Text(text = "Restart", fontSize = 25.sp)
            }
        }
        if (dialogOpened.value) {
            AlertDialog(
                title = {
                    Text(text = "TicTacToe")
                },
                text = {
                    when (winner) {
                        0 -> {
                            Text(text = "$player1Name has won the game!")
                        }
                        1 -> {
                            Text(text = "$player2Name has won the game!")
                        }
                        else -> {
                            Text(text = "Draw!")
                        }
                    }
                },
                onDismissRequest = {
                    dialogOpened.value = false
                    reset()
                    values.clear()
                    repeat(9) {
                        values.add("")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogOpened.value = false
                            reset()
                            values.clear()
                            repeat(9) {
                                values.add("")
                            }
                        }
                    ) {
                        Text("Restart")
                    }
                }
            )
        }
        if (changeNamesDialogOpened.value) {
            val text1 = remember { mutableStateOf("") }
            val text2 = remember { mutableStateOf("") }
            AlertDialog(
                title = {
                    Text(text = "TicTacToe")
                },
                text = {
                    Column {
                        Text(text = "Change player names. If player 2 has the name \"Console\" you will be playing against the computer.", modifier = Modifier.padding(12.dp))
                        OutlinedTextField(
                            value = text1.value,
                            onValueChange = { text1.value = it},
                            label = { Text("Player 1 Name")},
                            modifier = Modifier.padding(12.dp)
                        )
                        OutlinedTextField(
                            value = text2.value,
                            onValueChange = { text2.value = it },
                            label = { Text("Player 2 Name") },
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                },
                onDismissRequest = {
                    changeNamesDialogOpened.value = false
                },
                confirmButton = {
                    Row {
                        Button(
                            onClick = {
                                changeNamesDialogOpened.value = false
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text("Dismiss")
                        }
                        Button(
                            onClick = {
                                changeNamesDialogOpened.value = false
                                if(text1.value != "") player1Name = text1.value
                                if(text2.value != "") player2Name = text2.value
                                reset()
                                values.clear()
                                repeat(9) {
                                    values.add("")
                                }
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            )
        }
    }
}

fun reset() {
    allowedToClick = true
    currentPlayer = 0
    isGameRunning = true
    ticTacToeCurrentState = Array(9) { "" }
}
@Composable
fun RowScope.TicTacToeButton(
    value: String,
    onValueChange: (String, Int) -> Unit,
    row: Int,
    column: Int,
    onDialogChange: (Boolean) -> Unit
) {
    Button(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .padding(8.dp)
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(containerColor = Color(100, 100, 255)),
        onClick = {
            if (isGameRunning && allowedToClick && ticTacToeCurrentState[(row-1)*3+column-1] == "") {
                onValueChange(if (currentPlayer == 0) "O" else "X", (row-1)*3+column-1)
                ticTacToeCurrentState[(row-1)*3+column-1] = if (currentPlayer == 0) "O" else "X"
                currentPlayer = (currentPlayer + 1) % 2
                winner = checkWinner()
                if (winner > -2) {
                    isGameRunning = false
                    onDialogChange(true)
                } else if (player2Name.equals("Console", ignoreCase = true)) {
                    allowedToClick = false
                    val makeComputerMove = object : TimerTask() {
                        override fun run() {
                            val count = ticTacToeCurrentState.count{it  == ""}
                            val selectedFirstEmpty = (1..count).random()
                            var j = 0
                            for (i in (0..ticTacToeCurrentState.size)) {
                                if (ticTacToeCurrentState[i] == "") {
                                    j++
                                    if (j == selectedFirstEmpty) {
                                        onValueChange(if (currentPlayer == 0) "O" else "X", i)
                                        ticTacToeCurrentState[i] = if (currentPlayer == 0) "O" else "X"
                                        currentPlayer = (currentPlayer + 1) % 2
                                        winner = checkWinner()
                                        if (winner > -2) {
                                            isGameRunning = false
                                            onDialogChange(true)
                                        }
                                        break
                                    }
                                }
                            }
                            allowedToClick = true
                        }
                    }
                    Timer().schedule(makeComputerMove, 1000)
                }
            }
        }
    ) {
        Text(text = value, fontSize = 70.sp)
    }
}

fun checkWinner(): Int {
    for (i in 0..2) {
        if (ticTacToeCurrentState[i*3] == ticTacToeCurrentState[i*3+1] &&
            ticTacToeCurrentState[i*3+2] == ticTacToeCurrentState[i*3+1] &&
            ticTacToeCurrentState[i*3] != "")
            return if (ticTacToeCurrentState[i*3] == "O") 0 else 1
    }
    for (i in 0..2) {
        if (ticTacToeCurrentState[i] == ticTacToeCurrentState[i+3] &&
            ticTacToeCurrentState[i+3] == ticTacToeCurrentState[i+6] &&
            ticTacToeCurrentState[i] != "")
            return if (ticTacToeCurrentState[i] == "O") 0 else 1
    }
    if (ticTacToeCurrentState[0] == ticTacToeCurrentState[4] &&
        ticTacToeCurrentState[4] == ticTacToeCurrentState[8] &&
        ticTacToeCurrentState[0] != "")
        return if (ticTacToeCurrentState[0] == "O") 0 else 1
    if (ticTacToeCurrentState[2] == ticTacToeCurrentState[4] &&
        ticTacToeCurrentState[4] == ticTacToeCurrentState[6] &&
        ticTacToeCurrentState[2] != "")
        return if (ticTacToeCurrentState[2] == "O") 0 else 1
    if (!ticTacToeCurrentState.contains("")) return -1

    return -2
}

@Preview(showBackground = true, widthDp=400, heightDp=1000)
@Composable
fun DefaultPreview() {
    TicTacToeTheme {
        TicTacToe()
    }
}