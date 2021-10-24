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
import com.forsvarir.timetracker.views.ActivityHistoryView
import com.forsvarir.timetracker.views.CurrentActivityView

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


