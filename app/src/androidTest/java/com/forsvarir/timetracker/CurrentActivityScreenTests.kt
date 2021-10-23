package com.forsvarir.timetracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule

import org.junit.Test

import org.junit.Rule

class CurrentActivityScreenTests {
    @get:Rule
    val mainActivityRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun canNavigateToCurrentActivityScreen() {
        launchCurrentActivityScreen(mainActivityRule) {
        } verify {
            timelineScreenIsPresent()
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
    infix fun verify(
        block: CurrentActivityScreenVerifier.() -> Unit
    ): CurrentActivityScreenVerifier {
        return CurrentActivityScreenVerifier(rule).apply(block)
    }
}

class CurrentActivityScreenVerifier(private val rule: MainActivityComposeTestRule) {
    fun timelineScreenIsPresent() {
        val currentActivity = rule.activity.getString(R.string.currentActivity)
        rule.onNodeWithText(currentActivity)
            .assertIsDisplayed()
    }
}
