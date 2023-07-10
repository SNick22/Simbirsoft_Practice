package com.example.mydiary.presentation.screen.task_list

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.di.task_list.TaskListComponentHolder
import com.example.mydiary.domain.entity.TaskEntity
import com.example.mydiary.presentation.mappers.toLocalDate
import com.example.mydiary.presentation.navigation.Screen
import com.example.mydiary.presentation.screen.new_task.NewTaskEvent
import com.example.mydiary.presentation.ui.theme.Purple40
import com.example.mydiary.presentation.ui.theme.Typography
import com.example.mydiary.utils.Click
import com.example.mydiary.utils.daggerViewModel
import com.example.mydiary.utils.getHeight
import com.example.mydiary.utils.getTopMargin
import com.example.mydiary.utils.group
import com.example.mydiary.utils.toTasksTime
import com.example.mydiary.utils.toToDoListString
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.collections.immutable.PersistentList

@Composable
fun ToDoListScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val viewModel = daggerViewModel {
        TaskListComponentHolder.get(context).getViewModel()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    ToDoListContent(
        state,
        viewModel::event,
    )

    ToDoListActions(
        viewAction = action,
        navController = navController
    )
}

@Composable
private fun ToDoListActions(
    viewAction: TaskListAction?,
    navController: NavController
) {
    LaunchedEffect(viewAction) {
        when(viewAction) {
            null -> Unit
            is TaskListAction.NavigateToDetails -> {
                navController.navigate("task_details/" + viewAction.taskId)
            }

            TaskListAction.NavigateToNewTask -> navController.navigate(Screen.NewTask.route)
        }
    }
}

@Composable
private fun Calendar(
    viewState: TaskListState,
    eventHandler: (TaskListEvent) -> Unit,
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("ОК")
            negativeButton(
                text = "Отмена",
                onClick = { eventHandler(TaskListEvent.OnCloseCalendar) }
            )
        },
        onCloseRequest = { eventHandler(TaskListEvent.OnCloseCalendar) }
    ) {
        datepicker(
            initialDate = viewState.selectedDay.toLocalDate()
        ) {
            eventHandler(TaskListEvent.OnCloseCalendar)
            eventHandler(TaskListEvent.OnChangeSelectedDayByData(it.dayOfMonth, it.monthValue, it.year))
        }
    }

    if (viewState.showCalendar) {
        dialogState.show()
    } else {
        dialogState.hide()
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ToDoListContent(
    viewState: TaskListState,
    eventHandler: (TaskListEvent) -> Unit,
) {

    Calendar(viewState, eventHandler)

    LaunchedEffect(Unit) {
        eventHandler(TaskListEvent.OnLoadTasks)
    }

    Scaffold(
        topBar = {
            AppBar(eventHandler)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DaysOfWeek()
            Days(viewState, eventHandler)
            Text(
                text = viewState.selectedDay.toToDoListString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(top = 8.dp)
            )
            TaskList(viewState, eventHandler)
        }
    }
}

@Composable
private fun TaskList(
    viewState: TaskListState,
    eventHandler: (TaskListEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val totalHeight = dimensionResource(R.dimen.tasks_list_height)

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp)
            .verticalScroll(scrollState)
            .height(totalHeight)
    ) {
        val hoursInDay = 24
        val listOfTimes = (0..hoursInDay).map { it.toTasksTime() }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            items(listOfTimes) {
                Timestamp(it)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                items(hoursInDay + 1) {
                    Divider()
                }
            }

            Tasks(
                eventHandler,
                viewState.tasks.group(),
                totalHeight.value,
                viewState.selectedDay.timeStart,
                viewState.selectedDay.timeFinish
            )
        }
    }
}

@Composable
private fun Tasks(
    eventHandler: (TaskListEvent) -> Unit,
    groupedTasks: PersistentList<PersistentList<TaskEntity>>,
    totalHeight: Float,
    dayStartTime: Long,
    dayFinishTime: Long
) {
    groupedTasks.forEach { groupOfTasks ->
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            groupOfTasks.forEach { task ->
                val offset = task.getTopMargin(totalHeight, dayStartTime)
                val height = task.getHeight(totalHeight, dayStartTime, dayFinishTime)
                Task(
                    task.name,
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = offset)
                        .height(height),
                    onClick =  {
                        eventHandler(TaskListEvent.OnNavigateToDetails(task.id))
                    }
                )
            }
        }
    }
}

@Composable
private fun Task(
    text: String,
    modifier: Modifier = Modifier,
    onClick: Click
) {
    Box(
        modifier = modifier
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() }
                .background(color = Purple40.copy(alpha = 0.2f))
                .padding(top = 4.dp, start = 8.dp)
                .fillMaxSize()
        ) {
            Text(
                text = text,
                color = Purple40,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun Timestamp(text: String) {
    Text(
        text = text,
        style = Typography.body2.copy(color = Color.Gray),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Days(
    viewState: TaskListState,
    eventHandler: (TaskListEvent) -> Unit
) {
    val pageCount = Int.MAX_VALUE
    val initialPage = pageCount / 2
    val pagerState = rememberPagerState(initialPage = initialPage)

    HorizontalPager(
        pageCount = pageCount,
        state = pagerState
    ) {
        val week = it - initialPage
        DaysItem(viewState, eventHandler, week)
    }
}

@Composable
private fun DaysItem(
    viewState: TaskListState,
    eventHandler: (TaskListEvent) -> Unit,
    week: Int
) {
    val days = viewState.loadedDaysOfWeek[week]
    if (days == null) {
        eventHandler(TaskListEvent.OnLoadDaysOfWeek(week))
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            val interactionSource = MutableInteractionSource()

            days.forEach {
                Day(
                    it == viewState.selectedDay,
                    it == viewState.today,
                    it.day.toString(),
                    Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            eventHandler(TaskListEvent.OnChangeSelectedDay(it))
                        }
                )
            }
        }
    }
}

@Composable
private fun Day(
    isSelected: Boolean,
    isToday: Boolean,
    day: String,
    modifier: Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(32.dp)
                .background(
                    color = if (isSelected) if (isToday) Color.Red else Color.Black else Color.Unspecified,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day,
                color = if (isSelected) Color.White else if (isToday) Color.Red else Color.Unspecified
            )
        }
    }
}

@Composable
private fun DaysOfWeek() {
    val listOfDaysOfWeek = listOf("п", "в", "с", "ч", "п", "с", "в")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
    ) {
        listOfDaysOfWeek.forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AppBar(eventHandler: (TaskListEvent) -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        actions = {
            AppBarAction(
                image = Icons.Filled.DateRange,
                contentDescription = "calendar"
            ) {
                eventHandler(TaskListEvent.OnShowCalendar)
            }

            AppBarAction(
                image = Icons.Filled.Add,
                contentDescription = "new"
            ) {
                eventHandler(TaskListEvent.OnNavigateToNewTask)
            }
        }
    )
}

@Composable
fun AppBarAction(image: ImageVector, contentDescription: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = image,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = contentDescription
        )
    }
}