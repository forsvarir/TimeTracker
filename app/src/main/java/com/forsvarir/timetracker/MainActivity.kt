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
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.views.MainNavigation
import com.forsvarir.timetracker.views.TopNavBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val currentActivityViewModel: CurrentActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityView(currentActivityViewModel)
        }
    }
}

//@Preview
@Composable
fun MainActivityView(viewModel: CurrentActivityViewModel) {
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
                MainNavigation(navController, viewModel) {
                    title = it
                }
            }
        }
    }
}

//fun stubbedViewModel(): CurrentActivityViewModel {
//    val timeTrackerRepository = TimeTrackerRepository()
//    return CurrentActivityViewModel(timeTrackerRepository)
//}
