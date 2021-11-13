package com.forsvarir.timetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forsvarir.timetracker.data.ActivityConstants
import com.forsvarir.timetracker.data.TimeTrackerRepository
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel
import com.forsvarir.timetracker.viewModels.TimeFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CurrentActivityTests {
    val dispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun activitiesAvailable() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")

        val viewModel = createViewModel(possibleActivities, clock = ProgrammableTimeFactory())

        assertThat(viewModel.availableActivities.value).containsExactly(
            "Programming",
            "Walking",
            "Sleeping"
        )
    }

    @Test
    fun initialState() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val model = CurrentActivityViewModel(StubbedTimeTrackerRepository(possibleActivities))

        assertThat(model.currentActivity.value).isEqualTo(ActivityConstants.Unknown)
        assertThat(model.previousActivities.value).isEmpty()
    }

    @Test
    fun noCurrentActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val clock = ProgrammableTimeFactory()
        val model = createViewModel(possibleActivities, clock)

        model.startActivity("Programming")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(clock.now())
        assertThat(currentActivity.endTime).isNull()

        assertThat(model.previousActivities.value).isEmpty()
    }


    @Test
    fun runningActivity_newActivity() {
        val possibleActivities = listOf("Programming", "Walking", "Sleeping")
        val initialActivityStartTime = LocalDateTime.of(2021, 1, 2, 5, 30)
        val secondActivityStartTime = initialActivityStartTime.plusHours(1)
        val clock = ProgrammableTimeFactory(initialActivityStartTime)
        val model = createViewModel(possibleActivities, clock)

        model.startActivity("Programming")

        clock.setNow(secondActivityStartTime)
        model.startActivity("Walking")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(model.previousActivities.value).hasSize(1)
        assertThat(model.previousActivities.value).isNotNull
        val secondActivity = model.previousActivities.value!!.first()

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
        val model = createViewModel(possibleActivities, clock)

        model.startActivity("Programming")
        clock.setNow(secondActivityStartTime)
        model.startActivity("Programming")

        assertThat(model.currentActivity.value).isNotNull
        val currentActivity = model.currentActivity.value!!
        assertThat(currentActivity.name).isEqualTo("Programming")
        assertThat(currentActivity.startTime).isEqualTo(initialActivityStartTime)
        assertThat(currentActivity.endTime).isNull()

        assertThat(model.previousActivities.value).hasSize(0)
    }

    private fun createViewModel(
        possibleActivities: List<String>,
        clock: ProgrammableTimeFactory
    ): CurrentActivityViewModel {
        val model =
            CurrentActivityViewModel(StubbedTimeTrackerRepository(possibleActivities), clock)
        Thread.sleep(50) // TODO - There must be a better way to way for the IO thread to run
        return model
    }

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
    override fun availableActivities(): LiveData<List<String>> {
        return MutableLiveData(availableActivities)
    }

}