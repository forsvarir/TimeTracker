package com.forsvarir.timetracker

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.control.CurrentActivityScreenController
import org.junit.Rule
import org.junit.Test


class NavigationTests {
    @get:Rule
    val mainActivityRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchOpensCurrentActivityScreen() {
        launchCurrentActivityScreen(mainActivityRule) {
        } verify {
            currentActivityScreenIsOpen()
        }
    }

    @Test
    fun canNavigateToActivityHistoryScreen() {
        launchCurrentActivityScreen(mainActivityRule) {
            navigateToActivityHistoryScreen()
        } verify {
            activityHistoryScreenIsOpen()
        }
    }

    @Test
    fun canNavigateToCurrentActivityScreen() {
        launchCurrentActivityScreen(mainActivityRule) {
            navigateToCurrentActivityScreen()
        } verify {
            currentActivityScreenIsOpen()
        }
    }

    private fun launchCurrentActivityScreen(
        rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }
}

