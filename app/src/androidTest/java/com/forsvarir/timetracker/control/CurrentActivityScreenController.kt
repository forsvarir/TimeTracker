package com.forsvarir.timetracker.control

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.forsvarir.timetracker.MainActivity
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.verification.CurrentActivityScreenVerifier
import java.lang.Thread.sleep

class CurrentActivityScreenController(private val rule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    fun navigateToActivityHistoryScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_previous_activities))
            .performClick()
    }

    fun navigateToCurrentActivityScreen() {
        rule.onNodeWithContentDescription(rule.activity.getString(R.string.navigate_to_current_activity))
            .performClick()
    }

    fun setCurrentActivity(newActivity: String) {
        rule
            .onNodeWithContentDescription("Current Activity")
            .performClick()
        rule
            .onNodeWithText(newActivity)
            .performClick()
    }

    fun orientatePortrait() {
        setOrientation(
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            Configuration.ORIENTATION_PORTRAIT
        )
    }

    fun orientateLandscape() {
        setOrientation(
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            Configuration.ORIENTATION_LANDSCAPE
        )
    }

    private fun setOrientation(to: Int, expectedTo: Int) {
        rule.activity.requestedOrientation = to

        for (i in 1..5) {
            if (rule.activity.resources.configuration.orientation == expectedTo) {
                break
            }
            sleep(50)
        }
    }

    infix fun verify(
        block: CurrentActivityScreenVerifier.() -> Unit
    ): CurrentActivityScreenVerifier {
        return CurrentActivityScreenVerifier(rule).apply(block)
    }
}
