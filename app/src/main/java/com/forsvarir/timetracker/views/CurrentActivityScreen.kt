package com.forsvarir.timetracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.forsvarir.timetracker.R
import com.forsvarir.timetracker.ui.theme.TimeTrackerTheme


@Preview(widthDp = 320, heightDp = 480)
@Composable
private fun PreviewCurrentActivityView() {
    TimeTrackerTheme {
        CurrentActivityView(
            currentActivity = "Doing Stuff",
            availableActivities = MutableLiveData(
                listOf(
                    "Doing Stuff",
                    "Other Stuff"
                )
            ),
            onCurrentActivityChanged = {}
        )
    }
}

@Composable
fun CurrentActivityView(
    currentActivity: String = "Doing Stuff",
    availableActivities: LiveData<List<String>>,
    onCurrentActivityChanged: (String) -> Unit
) {
    var update by remember { mutableStateOf(false) }
    val activities = availableActivities.observeAsState()

    Column(Modifier.padding(all = 8.dp)) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            if (!update) {
                CurrentActivityStatus(currentActivity) { update = true }
            } else {
                SelectCurrentActivity(currentActivity, activities.value!!) {
                    onCurrentActivityChanged(it)
                    update = false
                }
            }
        }
    }
}

@Composable
private fun CurrentActivityStatus(currentActivity: String, onUpdate: () -> Unit) {
    val currentRunningActivityDescription = stringResource(R.string.CurrentRunningActivity)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = currentActivity,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .semantics { contentDescription = currentRunningActivityDescription }
                .clickable(enabled = true, role = Role.Button) {
                    onUpdate()
                }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "TickTickTick",
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun SelectCurrentActivity(
    currentItem: String,
    availableActivities: List<String>,
    onSelected: (newItem: String) -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { onSelected(currentItem) },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
        ) {
            availableActivities.forEach { itemText ->
                val selectedModifier = Modifier.background(MaterialTheme.colors.secondaryVariant)
                DropdownMenuItem(
                    onClick = {
                        onSelected(itemText)
                    }, if (itemText == currentItem) {
                        selectedModifier
                    } else {
                        Modifier
                    }
                ) {
                    Text(itemText, color = MaterialTheme.colors.onSecondary)
                }
            }
        }
    }
}
