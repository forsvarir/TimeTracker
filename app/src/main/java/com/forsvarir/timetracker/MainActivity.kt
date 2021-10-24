package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrentTaskScreen()
        }
    }
}

@Preview
@Composable
fun CurrentTaskScreen() {
    TimeTrackerTheme {
        Scaffold(topBar = {
            TopAppBar()
        }) { innerPadding ->
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(innerPadding)
            ) {
                TaskView()
            }
        }
    }
}

@Composable
private fun TopAppBar() {
    TopAppBar(
        title = {
            Text("Time Tracker")
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.current_activity))
            }
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.List, contentDescription = stringResource(R.string.previous_activities))
            }
        }
    )
}

@Composable
fun TaskView() {
    var update by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf("Programming") }

    Column(Modifier.padding(all = 8.dp)) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.current_activity),
                style = MaterialTheme.typography.h3
            )
        }
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            if (!update) {
                CurrentActivityStatus(currentItem) { update = true }
            } else {
                SelectCurrentActivity {
                    currentItem = it
                    update = false
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = stringResource(R.string.previous_activities), style = MaterialTheme.typography.h3)
        }
        Row {
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Event",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Duration",
                style = MaterialTheme.typography.subtitle1
            )
        }
        EventRow("Programming", "00.11")
        EventRow("Walking", "01.11")
        EventRow("Sleeping", "08.01")
        EventRow("Eating", "02.11")
    }
}

@Composable
fun SelectCurrentActivity(onSelected: (newItem: String) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("Programming", "Walking", "Sleeping", "Eating")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Color.Gray
                )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            items.forEachIndexed { index, itemText ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedIndex = index
                    onSelected(itemText)
                }) {
                    Text(itemText)
                }
            }
        }
    }
}

@Composable
private fun CurrentActivityStatus(currentActivity: String, onUpdate: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = currentActivity,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "TickTickTick",
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedButton(onClick = { onUpdate() }) {
            Text("ChangeTask")
        }
    }
}

@Composable
private fun EventRow(activity: String, duration: String) {
    Row {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = activity,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = duration,
            style = MaterialTheme.typography.subtitle2
        )
    }
}
