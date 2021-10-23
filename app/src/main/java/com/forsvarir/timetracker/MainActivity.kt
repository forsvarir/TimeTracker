package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
        }) {
            Surface(color = MaterialTheme.colors.background) {
                TaskView()
            }
        }
    }
}

@Composable
private fun TopAppBar() {
    TopAppBar(title = {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) { Text("Time Tracker") }
    }, actions = {})
}

@Composable
fun TaskView() {
    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Current Activity", style = MaterialTheme.typography.h3)
        }
        EventRow("Programming", "00.11")
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "Previous Events", style = MaterialTheme.typography.h3)
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
