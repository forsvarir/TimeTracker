package com.forsvarir.timetracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forsvarir.timetracker.R

@Preview
@Composable
fun CurrentActivityView() {
    var update by remember { mutableStateOf(false) }
    var currentItem by remember { mutableStateOf("Programming") }

    Column(Modifier.padding(all = 8.dp)) {
        Box(
            modifier = Modifier.wrapContentSize(Alignment.TopStart)
        ) {
            if (!update) {
                CurrentActivityStatus(currentItem) { update = true }
            } else {
                SelectCurrentActivity {
                    currentItem = it
                    update = false
                }
            }
        }
    }
}

@Composable
private fun CurrentActivityStatus(currentActivity: String, onUpdate: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = currentActivity,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "TickTickTick",
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedButton(onClick = { onUpdate() }) {
            Text("ChangeTask")
        }
    }
}

@Composable
fun SelectCurrentActivity(onSelected: (newItem: String) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("Programming", "Walking", "Sleeping", "Eating")
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Color.Gray
                )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            items.forEachIndexed { index, itemText ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedIndex = index
                    onSelected(itemText)
                }) {
                    Text(itemText)
                }
            }
        }
    }
}
