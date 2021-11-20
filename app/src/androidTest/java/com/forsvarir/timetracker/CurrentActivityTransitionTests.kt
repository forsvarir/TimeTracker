package com.forsvarir.timetracker

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.control.CurrentActivityScreenController
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext

class CurrentActivityTransitionTests {
    @get:Rule
    val mainActivityRule = createAndroidComposeRule<MainActivity>()
    private val context: Context = GlobalContext.get().get()
    private val idleActivityName = context.getString(R.string.ActivityIdle)
    private val travellingActivityName = context.getString(R.string.ActivityTravelling)
    private val sleepingActivityName = context.getString(R.string.ActivitySleeping)

    @Test
    fun selectingNewActivityChanges() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity(sleepingActivityName)
        } verify {
            currentActivityIs(sleepingActivityName)
        }
    }

    @Test
    fun currentActivityPersistsThroughOrientationChange() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity(sleepingActivityName)
            orientateLandscape()
        } verify {
            currentActivityIs(sleepingActivityName)
        }
    }

    @Test
    fun changingActivityAddsToPreviousHistory() {
        var initialNodeCount = 0
        launchCurrentActivityScreen(mainActivityRule) {
            navigateToActivityHistoryScreen()
            initialNodeCount = mainActivityRule.onAllNodesWithText(travellingActivityName)
                .fetchSemanticsNodes(atLeastOneRootRequired = false).size

            navigateToCurrentActivityScreen()
            setCurrentActivity(travellingActivityName)
            setCurrentActivity(sleepingActivityName)

            navigateToActivityHistoryScreen()
        } verify {
            mainActivityRule.onAllNodesWithText(travellingActivityName)
                .assertCountEquals(initialNodeCount + 1)
        }
    }

    @Test
    fun changingActivityThroughUnknownNotTrackedOnPreviousHistory() {
        launchCurrentActivityScreen(mainActivityRule) {
            navigateToCurrentActivityScreen()
            setCurrentActivity(idleActivityName)
            setCurrentActivity(sleepingActivityName)

            navigateToActivityHistoryScreen()
        } verify {
            mainActivityRule.onAllNodesWithText(idleActivityName)
                .assertCountEquals(0)
        }
    }

    private fun launchCurrentActivityScreen(
        rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }
}