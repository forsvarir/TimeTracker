package com.forsvarir.timetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.data.TimeTrackerRepository
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

@Composable
fun MainActivityView(viewModel: CurrentActivityViewModel) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }
    val activity by viewModel.currentActivity.observeAsState()

    TimeTrackerTheme {
        Scaffold(topBar = {
            TopNavBar(navController, title)
        }, bottomBar = { BottomInfoBar(activity!!) }) { innerPadding ->
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

@Composable
fun BottomInfoBar(currentActivity: ActivityInstance) {
    val runningActivityDescription = stringResource(R.string.RunningActivityProgress)
    BottomAppBar {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentActivity.name != "Unknown") {
                Text(
                    text = currentActivity.name,
                    modifier = Modifier.semantics {
                        contentDescription = runningActivityDescription
                    })
            }
        }
    }
}

@Preview(widthDp = 320, heightDp = 480)
@Composable
private fun PreviewMainActivityView() {
    val availableActivityNames = listOf("Walking", "Programming", "Sleeping", "Travelling")

    val stubbedRepository = object : TimeTrackerRepository {
        override fun ready(): LiveData<Boolean> {
            return MutableLiveData(true)
        }

        override fun availableActivities(): LiveData<List<String>> {
            return MutableLiveData(availableActivityNames)
        }
    }
    val viewModel = CurrentActivityViewModel(stubbedRepository)
    viewModel.startActivity("Programming")
    MainActivityView(viewModel = viewModel)
}