package com.forsvarir.timetracker.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.forsvarir.timetracker.R
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
                    contentDescription = stringResource(R.string.navigate_to_current_activity)
                )
            }
            IconButton(onClick = { navController.navigate(NavigationHooks.ACTIVITY_HISTORY) }) {
                Icon(
                    Icons.Filled.List,
                    contentDescription = stringResource(R.string.navigate_to_previous_activities)
                )
            }
        }
    )
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    viewModel : CurrentActivityViewModel,
    newTitle: (String) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = NavigationHooks.CURRENT_ACTIVITY
    ) {
        composable(NavigationHooks.CURRENT_ACTIVITY) {
            newTitle(stringResource(R.string.current_activity_screen_title))
            val currentActivity by viewModel.currentActivity.observeAsState()
            CurrentActivityView(
                currentActivity?.name ?: "",
                viewModel.availableActivities.value ?: emptyList()
            ) {
                viewModel.startActivity(it)
            }
        }
        composable(NavigationHooks.ACTIVITY_HISTORY) {
            newTitle(stringResource(R.string.previous_activities_screen_title))
            ActivityHistoryView(viewModel.previousActivities.value ?: emptyList())
        }
    }
}
