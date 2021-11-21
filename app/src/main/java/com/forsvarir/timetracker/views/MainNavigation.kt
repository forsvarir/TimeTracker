package com.forsvarir.timetracker.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel

class NavigationHooks {
    companion object {
        const val CURRENT_ACTIVITY = "currentActivity"
        const val ACTIVITY_HISTORY = "activityHistory"
    }
}

@Composable
fun TopNavBar(navController: NavController, title: String) {
    TopAppBar(
        title = {
            Text(title, modifier = Modifier.semantics { contentDescription = "Screen Title" })
        },
        actions = {
            IconButton(onClick = { navController.navigate(NavigationHooks.CURRENT_ACTIVITY) }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = stringResource(R.string.NavigateToCurrentActivity)
                )
            }
            IconButton(onClick = { navController.navigate(NavigationHooks.ACTIVITY_HISTORY) }) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(R.string.NavigateToPreviousActivities)
                )
            }
        }
    )
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    viewModel: CurrentActivityViewModel,
    newTitle: (String) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = NavigationHooks.CURRENT_ACTIVITY
    ) {
        composable(NavigationHooks.CURRENT_ACTIVITY) {
            newTitle(stringResource(R.string.TitleCurrentActivityScreen))
            val currentActivity by viewModel.currentActivity.observeAsState()
            CurrentActivityView(
                currentActivity?.name ?: "",
                viewModel.availableActivities(),
            ) {
                viewModel.startActivity(it)
            }
        }
        composable(NavigationHooks.ACTIVITY_HISTORY) {
            newTitle(stringResource(R.string.TitlePreviousActivitiesScreen))
            ActivityHistoryView(viewModel.previousActivities.value ?: emptyList())
        }
    }
}

@Composable
fun BottomInfoBar(currentActivity: ActivityInstance, idleActivityName: String, tick: Int?) {
    val runningActivityDescription = stringResource(R.string.RunningActivityProgress)
    BottomAppBar {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentActivity.name != idleActivityName) {
                Text(
                    text = "${currentActivity.name} ${currentActivity.getDuration()}",
                    modifier = Modifier.semantics {
                        contentDescription = runningActivityDescription
                    })
            }
        }
    }
}
