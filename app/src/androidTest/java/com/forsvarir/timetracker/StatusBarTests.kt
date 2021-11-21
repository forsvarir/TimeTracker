package com.forsvarir.timetracker

import android.content.Context
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.control.CurrentActivityScreenController
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.viewModels.TimeFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.time.LocalDateTime
import java.util.stream.Collectors
import java.util.stream.Stream

class StatusBarTests {
    private val context: Context = get().get()
    private val defaultTimeFactory: TimeFactory = get().get()
    private val programmableTimeFactory: ProgrammableTimeFactory = ProgrammableTimeFactory()
    private val defaultRepository: TimeTrackerRepository = get().get()
    private val idleActivityName = context.getString(R.string.ActivityIdle)
    private val travellingActivityName = context.getString(R.string.ActivityTravelling)
    private val stubbedTimeTrackerRepository: StubbedTimeTrackerRepository =
        StubbedTimeTrackerRepository(listOf(idleActivityName, travellingActivityName))


    @get:Rule
    val mainActivityRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun before() {
        replaceTimeFactoryAndRepo(programmableTimeFactory, stubbedTimeTrackerRepository)
    }

    @After
    fun after() {
        replaceTimeFactoryAndRepo(defaultTimeFactory, defaultRepository)
    }

    @Test
    fun currentRunningActivityNotDisplayedInStatusBarWhenUnknown() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity(idleActivityName)
        } verify {
            noCurrentRunningActivityDisplayed()
        }
    }

    @Test
    fun currentRunningActivityAndDurationIsDisplayedInStatusBar() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity(travellingActivityName)
        } verify {
            currentRunningActivityIs(travellingActivityName, "00:00.00")
        }
    }

    @Test
    fun currentRunningActivityAndDurationReflectsElapsedTimeInStatusBar() {
        launchCurrentActivityScreen(mainActivityRule) {
            val activityStartTime = programmableTimeFactory.now()
            setCurrentActivity(travellingActivityName)
            programmableTimeFactory.setNow(
                activityStartTime.plusHours(1).plusMinutes(23).plusSeconds(45)
            )
        } verify {
            currentRunningActivityIs(travellingActivityName, "01:23.45")
        }
    }

    @Test
    fun currentRunningActivityAndDurationReflectsElapsedTimeAfterTicksInStatusBar() {
        launchCurrentActivityScreen(mainActivityRule) {
            val activityStartTime = programmableTimeFactory.now()
            setCurrentActivity(travellingActivityName)
            programmableTimeFactory.setNow(
                activityStartTime.plusHours(1).plusMinutes(23).plusSeconds(45)
            )
            programmableTimeFactory.setNow(programmableTimeFactory.now().plusSeconds(10))
        } verify {
            currentRunningActivityIs(travellingActivityName, "01:23.55")
        }
    }

    @Test
    fun currentRunningActivityIsVisibleOnPreviousActivityHistory() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity(travellingActivityName)
        } verify {
            currentRunningActivityIs(travellingActivityName, "00:00.00")
        }
    }

    private fun launchCurrentActivityScreen(
        rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }

    private fun replaceTimeFactoryAndRepo(
        timeFactory: TimeFactory,
        timeTrackerRepository: TimeTrackerRepository
    ) {
        val replaceModule = module {
            factory(override = true) { timeFactory }
            factory(override = true) { timeTrackerRepository }
        }
        loadKoinModules(replaceModule)
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

    private class StubbedTimeTrackerRepository(private val availableActivities: List<String> = emptyList()) :
        TimeTrackerRepository {
        val previousActivities: MutableLiveData<List<ActivityInstance>> =
            MutableLiveData(emptyList())

        override fun availableActivities(): LiveData<List<String>> {
            return MutableLiveData(availableActivities)
        }

        override fun ready(): LiveData<Boolean> {
            return MutableLiveData(true)
        }

        override fun save(activityInstance: ActivityInstance) {
            previousActivities.postValue(
                Stream.concat(
                    previousActivities.value!!.stream(),
                    listOf(activityInstance).stream()
                )
                    .collect(Collectors.toList())
            )
        }

        override fun allPreviousActivities(): LiveData<List<ActivityInstance>> {
            return previousActivities
        }

    }
}
