package com.forsvarir.timetracker

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
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
            navigateCurrentActivityScreen()
        } verify {
            currentActivityScreenIsOpen()
        }
    }

    @Test
    fun selectingNewActivityChanges() {
        launchCurrentActivityScreen(mainActivityRule) {
            mainActivityRule
                .onNodeWithContentDescription("Current Activity")
                .performClick()
            mainActivityRule
                .onNodeWithText("Programming")
                .performClick()

        } verify {
            mainActivityRule.onNodeWithContentDescription("Current Activity")
                .assertTextContains("Programming")
        }
    }

    private fun launchCurrentActivityScreen(
        rule: MainActivityComposeTestRule,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }
}

typealias MainActivityComposeTestRule = AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>

class CurrentActivityScreenController(private val rule: MainActivityComposeTestRule) {
    fun navigateToActivityHistoryScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_previous_activities))
            .performClick()
    }

    fun navigateCurrentActivityScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_current_activity))
            .performClick()
    }

    infix fun verify(
        block: CurrentActivityScreenVerifier.() -> Unit
    ): CurrentActivityScreenVerifier {
        return CurrentActivityScreenVerifier(rule).apply(block)
    }
}

class CurrentActivityScreenVerifier(private val rule: MainActivityComposeTestRule) {
    fun currentActivityScreenIsOpen() {
        val currentActivity = rule.activity.getString(R.string.current_activity_screen_title)
        rule.onNodeWithText(currentActivity)
            .assertIsDisplayed()
    }

    fun activityHistoryScreenIsOpen() {
        val previousActivities = rule.activity.getString(R.string.previous_activities_screen_title)
        rule.onNodeWithText(previousActivities)
            .assertIsDisplayed()
    }
}
