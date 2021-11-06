package com.forsvarir.timetracker

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.control.CurrentActivityScreenController
import org.junit.Rule
import org.junit.Test

class CurrentActivityTransitionTests {
    @get:Rule
    val mainActivityRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun selectingNewActivityChanges() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity("Programming")
        } verify {
            currentActivityIs("Programming")
        }
    }

    @Test
    fun currentActivityPersistsThroughOrientationChange() {
        launchCurrentActivityScreen(mainActivityRule) {
            setCurrentActivity("Programming")
            orientateLandscape()
        } verify {
            currentActivityIs("Programming")
        }
    }

    @Test
    fun changingActivityAddsToPreviousHistory() {
        var initialNodeCount = 0
        launchCurrentActivityScreen(mainActivityRule) {
            navigateToActivityHistoryScreen()
            val rule = mainActivityRule
            initialNodeCount = rule.onAllNodesWithText("Travelling")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).size
            navigateToCurrentActivityScreen()
            setCurrentActivity("Travelling")
            setCurrentActivity("Programming")
            navigateToActivityHistoryScreen()
        } verify {
            val rule = mainActivityRule
            rule.onAllNodesWithText("Travelling").assertCountEquals(initialNodeCount + 1)
        }
    }

    private fun launchCurrentActivityScreen(
        rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }
}