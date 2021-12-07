package com.forsvarir.timetracker

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import com.forsvarir.timetracker.app.TimeTrackerApplication
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme
import com.forsvarir.timetracker.viewModels.ActivityHistoryViewModel
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.viewModels.TimeFactory
import com.forsvarir.timetracker.views.BottomInfoBar
import com.forsvarir.timetracker.views.MainNavigation
import com.forsvarir.timetracker.views.TopNavBar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private val currentActivityViewModel: CurrentActivityViewModel by viewModel()
    private val activityHistoryViewModel: ActivityHistoryViewModel by viewModel()
    lateinit var mainHandler: Handler

    private val heartbeat = object : Runnable {
        override fun run() {
            currentActivityViewModel.incrementTick()
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityView(
                currentActivityViewModel,
                TimeTrackerApplication.getContext().applicationContext.getString(R.string.ActivityIdle),
                activityHistoryViewModel
            )
        }

        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(heartbeat)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(heartbeat)
    }
}

@Composable
fun MainActivityView(
    viewModel: CurrentActivityViewModel,
    idleActivityName: String,
    activityHistoryViewModel: ActivityHistoryViewModel
) {
    val navController = rememberNavController()
    var title by remember { mutableStateOf("") }
    val activity by viewModel.currentActivity.observeAsState()
    val tick by viewModel.tick.observeAsState()

    TimeTrackerTheme {
        Scaffold(
            topBar = {
                TopNavBar(navController, title)
            },
            bottomBar = { BottomInfoBar(activity!!, idleActivityName, tick) },
            modifier = Modifier
                .semantics { contentDescription = "Time Tracker Application" }) { innerPadding ->
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier
                    .padding(innerPadding)
                    .semantics { contentDescription = "Info Panel" }
            ) {
                MainNavigation(navController, viewModel, activityHistoryViewModel) {
                    title = it
                }
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

        override fun save(activityInstance: ActivityInstance) {
            TODO("Not yet implemented")
        }

        override fun allPreviousActivities(): LiveData<List<ActivityInstance>> {
            TODO("Not yet implemented")
        }

        override fun availableActivities(): LiveData<List<String>> {
            return MutableLiveData(availableActivityNames)
        }

        override fun currentActivity(): LiveData<ActivityInstance> {
            TODO("Not yet implemented")
        }
    }
    val currentTime = ProgrammableTimeFactory()
    val viewModel = CurrentActivityViewModel(stubbedRepository, ProgrammableTimeFactory(), "Idle")
    val activityHistoryViewModel = ActivityHistoryViewModel(stubbedRepository)
    viewModel.startActivity("Programming")
    currentTime.setNow(currentTime.now().plusDays(2).plusHours(3).plusSeconds(44))
    MainActivityView(viewModel = viewModel, "Unknown", activityHistoryViewModel)
}

private class ProgrammableTimeFactory(initialTime: LocalDateTime = LocalDateTime.now()) :
    TimeFactory {
    private var currentTime: LocalDateTime = initialTime

    override fun now(): LocalDateTime {
        return currentTime
    }

    fun setNow(newNow: LocalDateTime) {
        currentTime = newNow
    }
}
