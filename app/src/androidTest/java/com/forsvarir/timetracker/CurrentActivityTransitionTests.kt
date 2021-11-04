package com.forsvarir.timetracker

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
            mainActivityRule.activity.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

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
        rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
        block: CurrentActivityScreenController.() -> Unit
    ): CurrentActivityScreenController {
        return CurrentActivityScreenController(rule).apply(block)
    }
}