package com.example.geoquest.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.geoquest.model.Quest

@Composable
fun TabLayout(
    questList: List<Quest>,
    navigateToViewQuest: (Int) -> Unit,
    selectedQuestId: MutableIntState,
) {
    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Active", "Completed")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(50))
                .padding(1.dp),
            indicator = {
                Box {}
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = {
                    Text(
                        title,
                        color = if (tabIndex == index) MaterialTheme.colors.primary
                        else Color.White
                    ) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    modifier = if (tabIndex == index)
                        Modifier.background(Color.White)
                    else Modifier.background(Color.Transparent),

                )
            }
        }
        when (tabIndex) {
            0 -> QuestList(
                questList = questList.filter { !it.isCompleted },
                navigateToViewQuest,
                selectedQuestId
            )
            1 -> QuestList(
                questList = questList.filter { it.isCompleted },
                navigateToViewQuest,
                selectedQuestId
            )
        }
    }
}