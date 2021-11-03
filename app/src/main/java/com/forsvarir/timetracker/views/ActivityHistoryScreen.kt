package com.forsvarir.timetracker.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.forsvarir.timetracker.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forsvarir.timetracker.data.ActivityInstance
import com.forsvarir.timetracker.viewModels.CurrentActivityViewModel

@Preview
@Composable
fun ActivityHistoryView(events : List<ActivityInstance> = stubbedEvents()) {
    Column(Modifier.padding(all = 8.dp)) {
        Row {
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Event",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = "Duration",
                style = MaterialTheme.typography.subtitle1
            )
        }
        events.forEach {
            EventRow(it.name, it.duration)
        }
    }
}

fun stubbedEvents(): List<ActivityInstance> {
    return listOf(
        ActivityInstance("Programming"),
        ActivityInstance("Walking"),
        ActivityInstance("Programming"),
        ActivityInstance("Eating")
    )
}

@Composable
private fun EventRow(activity: String, duration: String) {
    Row {
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = activity,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5f),
            text = duration,
            style = MaterialTheme.typography.subtitle2
        )
    }
}
