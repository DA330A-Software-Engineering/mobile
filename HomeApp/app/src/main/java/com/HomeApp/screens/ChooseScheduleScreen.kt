package com.HomeApp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.HomeApp.ui.composables.CustomFAB
import com.HomeApp.ui.composables.TitleBar
import com.HomeApp.ui.navigation.ChooseActions
import com.HomeApp.ui.navigation.Finish
import com.HomeApp.ui.navigation.Routines
import com.HomeApp.ui.theme.FadedLightGrey
import com.HomeApp.ui.theme.LightSteelBlue
import com.HomeApp.util.DayFilters
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

data class ScheduleData(
    var cronString: String = ""
)

object Schedule {
    private var scheduleData: ScheduleData = ScheduleData()

    fun setCronString(cronString: String) {
        scheduleData.cronString = cronString
    }

    fun getCronString(): String {
        return scheduleData.cronString
    }

    fun clearCronString() {
        scheduleData.cronString = ""
    }
}

@Composable
fun ChooseScheduleScreen(
    navController: NavController,
    OnSelfClick: () -> Unit = {}
) {
    val listHeight = LocalConfiguration.current.screenHeightDp

    val selectedMinute = remember { mutableStateOf(0) }
    val selectedHour = remember { mutableStateOf(0) }
    val selectedDays = remember { mutableStateOf("0,1,2,3,4,5,6") }
    val cronString = remember { mutableStateOf("0 0 0 * * 0,1,2,3,4,5,6") }

    val selectionState = remember {
        mutableStateMapOf<DayFilters, Boolean>().apply {
            putAll(DayFilters.values().associateWith { true })
        }
    }

    Scaffold(
        topBar = {
            TitleBar(
                title = "Schedule",
                iconLeft = Icons.Rounded.ArrowBack,
                routeLeftButton = ChooseActions.route,
                iconRight = Icons.Rounded.Close,
                routeRightButton = Routines.route,
                navController = navController
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .height(listHeight.dp)
                    .fillMaxWidth()
                    .padding(it)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    item {
                        Text(
                            modifier = Modifier.padding(bottom = 20.dp),
                            text = "Choose a time for the actions",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        SelectDays(
                            selectedDays = selectedDays,
                            selectionState = selectionState
                        )
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    item {
                        Row(
                            modifier = Modifier
                                .border(
                                    width = 4.dp,
                                    color = LightSteelBlue,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            SelectTime(
                                options = (0..23).map { item -> item },
                                selection = selectedHour
                            )
                            Column(
                                modifier = Modifier
                                    .height(100.dp)
                                    .padding(horizontal = 5.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = ":", fontSize = 40.sp)
                            }
                            SelectTime(
                                options = (0..59).map { item -> item },
                                selection = selectedMinute
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            CustomFAB(
                icon = Icons.Rounded.ArrowForward,
                onClick = {
                    if (selectedDays.value == "") selectedDays.value = "0,1,2,3,4,5,6"
                    val fields = cronString.value.split(" ")
                    cronString.value = "${fields[0]} ${selectedMinute.value} ${selectedHour.value} ${fields[3]} ${fields[4]} ${selectedDays.value}"
                    Schedule.setCronString(cronString = cronString.value)
                    navController.navigate(Finish.route)
                }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End
    )
}


@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun SelectTime(
    options: List<Int>,
    selection: MutableState<Int>,
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapperFlingBehavior(lazyListState = listState)
    val layoutInfo = rememberLazyListSnapperLayoutInfo(lazyListState = listState)
    layoutInfo.visibleItems.firstOrNull()?.index.let {
        if (it != null) selection.value = it
    }

    val textSize = 40.sp
    val spacerHeight = with(LocalDensity.current) {
        (textSize.toDp() * 1.45f) / 2
    }

    LazyColumn(
        modifier = Modifier
            .height(110.dp),
        verticalArrangement = Arrangement.Center,
        state = listState,
        flingBehavior = flingBehavior,
        content = {
            item {
                Spacer(modifier = Modifier.height(spacerHeight))
            }
            items(items = options, key = { item -> item }) {
                Row(
                    modifier = Modifier.width(75.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = String.format("%02d", it),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(spacerHeight))
            }
        }
    )
}

@Composable
private fun SelectDays(
    selectedDays: MutableState<String>,
    selectionState: SnapshotStateMap<DayFilters, Boolean>
) {
    val selectedColor = LightSteelBlue
    val notSelectedColor = FadedLightGrey

    val allDaysSelected = selectionState.values.all { it }
    val allDaysUnselected = selectionState.values.all { !it }

    selectedDays.value = ""
    DayFilters.values().toList().forEach {
        if (selectionState[it] == true) {
            if (it == DayFilters.SUNDAY) {
                if (selectedDays.value == "") {
                    selectedDays.value = it.cron
                } else {
                    selectedDays.value = "${it.cron},${selectedDays.value}"
                }
            } else {
                if (selectedDays.value == "") {
                    selectedDays.value = it.cron
                } else {
                    selectedDays.value = "${selectedDays.value},${it.cron}"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .border(
                width = 4.dp,
                color = LightSteelBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .weight(5f)
                    .height(40.dp),
                onClick = {
                    val dayFilters = DayFilters.values().toList()
                    dayFilters.forEach { dayFilter ->
                        selectionState[dayFilter] = false
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (allDaysUnselected) {
                        selectedColor
                    } else {
                        notSelectedColor
                    }
                ),
                content = {
                    Text(text = "UNCHECK ALL", fontWeight = FontWeight.Bold)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .weight(5f)
                    .height(40.dp),
                onClick = {
                    val dayFilters = DayFilters.values().toList()
                    dayFilters.forEach { dayFilter ->
                        selectionState[dayFilter] = true
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (allDaysSelected) {
                        selectedColor
                    } else {
                        notSelectedColor
                    }
                ),
                content = {
                    Text(text = "CHECK ALL", fontWeight = FontWeight.Bold)
                }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayFilters.values().forEach {
                // modify the IconButton to change background based on the selection state
                val selected = selectionState[it] ?: false
                IconButton(
                    modifier = Modifier
                        .scale(0.8f)
                        .background(
                            if (selected) selectedColor else notSelectedColor,
                            CircleShape
                        ),
                    onClick = {
                        val newSelectionState = !selected
                        selectionState[it] = newSelectionState
                    }
                ) {
                    Text(
                        modifier = Modifier.scale(1.5f),
                        text = it.letter,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
