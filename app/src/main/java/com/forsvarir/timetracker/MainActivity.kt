package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme
import com.forsvarir.timetracker.views.ActivityHistoryView
import com.forsvarir.timetracker.views.CurrentActivityView
import com.forsvarir.timetracker.views.TopNavBar

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
            TopNavBar(navController, title)
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
                        title = stringResource(R.string.current_activity)
                        CurrentActivityView()
                    }
                    composable(NavigationHooks.ACTIVITY_HISTORY) {
                        title = stringResource(R.string.previous_activities)
                        ActivityHistoryView()
                    }
                }
            }
        }
    }
}

