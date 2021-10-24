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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrentTaskScreen()
        }
    }
}

class NavigationHooks {
    companion object {
        const val CURRENT_ACTIVITY = "currentActivity"
        const val ACTIVITY_HISTORY = "activityHistory"
    }
}

@Preview
@Composable
fun CurrentTaskScreen() {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }

    TimeTrackerTheme {
        Scaffold(topBar = {
            TopAppBar(navController, title)
        }) { innerPadding ->
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = NavigationHooks.CURRENT_ACTIVITY
                ) {
                    composable(NavigationHooks.CURRENT_ACTIVITY) {
                        CurrentActivityView { t -> title = t }
                    }
                    composable(NavigationHooks.ACTIVITY_HISTORY) {
                        ActivityHistoryView { t -> title = t }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(navController: NavController, title: String) {
    TopAppBar(
        title = {
            Text(title)
        },
        actions = {
            IconButton(onClick = { navController.navigate(NavigationHooks.CURRENT_ACTIVITY) }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.current_activity)
                )
            }
            IconButton(onClick = { navController.navigate(NavigationHooks.ACTIVITY_HISTORY) }) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(R.string.previous_activities)
                )
            }
        }
    )
}

@Composable
fun CurrentActivityView(navTitle: (String) -> Unit) {
    var update by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf("Programming") }

    Column(Modifier.padding(all = 8.dp)) {
        navTitle(stringResource(R.string.current_activity))
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
    }
}

@Composable
fun ActivityHistoryView(navTitle: (String) -> Unit) {
    Column(Modifier.padding(all = 8.dp)) {
        navTitle(stringResource(R.string.previous_activities))
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
