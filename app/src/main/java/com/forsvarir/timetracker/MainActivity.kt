package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TaskView()
                }
            }
        }
    }
}

@Composable
fun TaskView() {
    Column {
        Text(
            text = "Time Tracker",
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = "Programming",
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "00:11",
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}
