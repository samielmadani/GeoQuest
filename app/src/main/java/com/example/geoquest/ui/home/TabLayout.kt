package com.example.geoquest.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.geoquest.model.Quest

@Composable
fun TabLayout(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    selectedQuestId: MutableIntState,
    tabIndex: MutableIntState,
    onDelete: (Quest) -> Unit
) {

    val tabs = listOf("Active", "Completed")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex.intValue
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex.intValue == index,
                    onClick = {
                        selectedQuestId.intValue = -1
                        tabIndex.intValue = index
                              },
                )
            }
        }
        when (tabIndex.intValue) {
            0 -> QuestList(
                questList = questList.filter { !it.isCompleted },
                navigateToViewQuest,
                selectedQuestId, onDelete
            )
            1 -> QuestList(
                questList = questList.filter { it.isCompleted },
                navigateToViewQuest,
                selectedQuestId,
                onDelete
            )
        }
    }
}