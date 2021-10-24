package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme
import com.forsvarir.timetracker.views.MainNavigation
import com.forsvarir.timetracker.views.TopNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityView()
        }
    }
}

@Preview
@Composable
fun MainActivityView() {
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
                MainNavigation(navController) {
                    title = it
                }
            }
        }
    }
}
