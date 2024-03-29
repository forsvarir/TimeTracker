package com.forsvarir.timetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.data.entities.ActivityInstance
import com.forsvarir.timetracker.viewModels.ActivityHistoryViewModel
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.viewModels.LocalTimeFactory
import com.forsvarir.timetracker.viewModels.TimeFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.stream.Collectors
import java.util.stream.Stream.concat

@ExtendWith(InstantTaskExecutorExtension::class)
class CurrentActivityTests {
    @Test
    fun activitiesAvailable() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")

        val viewModel =
            CurrentActivityViewModel(
                StubbedTimeTrackerRepository(possibleActivities),
                ProgrammableTimeFactory(),
                "Idle"
            )

        assertThat(viewModel.availableActivities().value).containsExactly(
            "Programming",
            "Walking",
            "Sleeping"
        )
    }

    @Test
    fun initialState() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val timeTrackerRepository = StubbedTimeTrackerRepository(possibleActivities)
        val currentViewModel = CurrentActivityViewModel(
            timeTrackerRepository,
            LocalTimeFactory(),
            "Idle"
        )
        val historyViewModel =
            ActivityHistoryViewModel(timeTrackerRepository = timeTrackerRepository)

        assertThat(currentViewModel.currentActivity.value!!.name).isEqualTo("Idle")
        assertThat(historyViewModel.previousActivities.value).isEmpty()
    }

    @Test
    fun noCurrentActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val clock = ProgrammableTimeFactory()
        val timeTrackerRepository = StubbedTimeTrackerRepository(possibleActivities)
        val currentActivityViewModel =
            CurrentActivityViewModel(
                timeTrackerRepository,
                clock,
                "Idle"
            )
        val historyViewModel =
            ActivityHistoryViewModel(timeTrackerRepository = timeTrackerRepository)

        currentActivityViewModel.startActivity("Programming")

        assertThat(currentActivityViewModel.currentActivity.value).isNotNull
        val currentActivity = currentActivityViewModel.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(clock.now())
        assertThat(currentActivity.endTime).isNull()

        assertThat(historyViewModel.previousActivities.value).isEmpty()
    }

    @Test
    fun runningActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val initialActivityStartTime = LocalDateTime.of(2021, 1, 2, 5, 30)
        val secondActivityStartTime = initialActivityStartTime.plusHours(1)
        val clock = ProgrammableTimeFactory(initialActivityStartTime)
        val timeTrackerRepository = StubbedTimeTrackerRepository(possibleActivities)
        val currentActivityViewModel =
            CurrentActivityViewModel(
                timeTrackerRepository,
                clock,
                "Idle"
            )
        val historyViewModel =
            ActivityHistoryViewModel(timeTrackerRepository = timeTrackerRepository)

        currentActivityViewModel.startActivity("Programming")

        clock.setNow(secondActivityStartTime)
        currentActivityViewModel.startActivity("Walking")

        assertThat(currentActivityViewModel.currentActivity.value).isNotNull
        val currentActivity = currentActivityViewModel.currentActivity.value!!
        assertThat(historyViewModel.previousActivities.value).hasSize(1)
        assertThat(historyViewModel.previousActivities.value).isNotNull
        val secondActivity = historyViewModel.previousActivities.value!!.first()

        assertThat(currentActivity.name).isEqualTo("Walking")
        assertThat(currentActivity.startTime).isEqualTo(secondActivityStartTime)
        assertThat(currentActivity.endTime).isNull()

        assertThat(secondActivity.name).isEqualTo("Programming")
        assertThat(secondActivity.startTime).isEqualTo(initialActivityStartTime)
        assertThat(secondActivity.endTime).isEqualTo(secondActivityStartTime)
    }

    @Test
    fun restartCurrentActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val initialActivityStartTime = LocalDateTime.of(2021, 1, 2, 5, 30)
        val secondActivityStartTime = initialActivityStartTime.plusHours(1)
        val clock = ProgrammableTimeFactory(initialActivityStartTime)
        val timeTrackerRepository = StubbedTimeTrackerRepository(possibleActivities)
        val currentActivityViewModel =
            CurrentActivityViewModel(
                timeTrackerRepository,
                clock,
                "Idle"
            )
        val historyViewModel =
            ActivityHistoryViewModel(timeTrackerRepository = timeTrackerRepository)

        currentActivityViewModel.startActivity("Programming")
        clock.setNow(secondActivityStartTime)
        currentActivityViewModel.startActivity("Programming")

        assertThat(currentActivityViewModel.currentActivity.value).isNotNull
        val currentActivity = currentActivityViewModel.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(initialActivityStartTime)
        assertThat(currentActivity.endTime).isNull()

        assertThat(historyViewModel.previousActivities.value).hasSize(0)
    }

    class ProgrammableTimeFactory(initialTime: LocalDateTime = LocalDateTime.now()) :
        TimeFactory {
        private var currentTime: LocalDateTime = initialTime

        override fun now(): LocalDateTime {
            return currentTime
        }

        fun setNow(newNow: LocalDateTime) {
            currentTime = newNow
        }
    }

    class StubbedTimeTrackerRepository(private val availableActivities: List<String> = emptyList()) :
        TimeTrackerRepository {
        private val previousActivities: MutableLiveData<List<ActivityInstance>> =
            MutableLiveData(emptyList())
        private val currentActivity = MutableLiveData(ActivityInstance(name = "Idle"))

        override fun availableActivities(): LiveData<List<String>> {
            return MutableLiveData(availableActivities)
        }

        override fun currentActivity(): LiveData<ActivityInstance> {
            return currentActivity
        }

        override fun ready(): LiveData<Boolean> {
            return MutableLiveData(true)
        }

        override fun save(activityInstance: ActivityInstance) {
            if (activityInstance.endTime == null) {
                currentActivity.postValue(activityInstance)
            } else {
                previousActivities.postValue(
                    concat(
                        previousActivities.value!!.stream(),
                        listOf(activityInstance).stream()
                    )
                        .collect(Collectors.toList())
                )
            }
        }

        override fun allPreviousActivities(): LiveData<List<ActivityInstance>> {
            return previousActivities
        }
    }
}
