package com.forsvarir.timetracker.views

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.forsvarir.timetracker.NavigationHooks
import com.forsvarir.timetracker.R

@Composable
fun TopNavBar(navController: NavController, title: String) {
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
