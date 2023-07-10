package com.example.mydiary.presentation.screen.new_task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.di.new_task.NewTaskComponentHolder
import com.example.mydiary.presentation.mappers.toLocalTime
import com.example.mydiary.presentation.screen.task_list.AppBarAction
import com.example.mydiary.presentation.ui.theme.Purple40
import com.example.mydiary.utils.Click
import com.example.mydiary.utils.daggerViewModel
import com.example.mydiary.utils.getNewTaskText
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun NewTaskScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val viewModel = daggerViewModel {
        NewTaskComponentHolder.get(context).getViewModel()
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    NewTaskContent(state, viewModel::event)

    NewTaskActions(
        viewAction = action,
        navController = navController,
        context = context
    )
}

@Composable
private fun NewTaskActions(
    viewAction: NewTaskAction?,
    navController: NavController,
    context: Context
) {
    LaunchedEffect(viewAction) {
        when(viewAction) {
            null -> Unit
            NewTaskAction.NavigateBack -> navController.navigateUp()
            is NewTaskAction.ShowToast -> {
                Toast.makeText(context, viewAction.text, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun NewTaskContent(
    viewState: NewTaskState,
    eventHandler: (NewTaskEvent) -> Unit
) {
    NewTaskBaseMaterialPicker(eventHandler, viewState.isShowDatePicker) {
        NewTaskDatePicker(viewState, eventHandler)
    }
    NewTaskBaseMaterialPicker(eventHandler, viewState.isShowTimePicker) {
        NewTaskTimePicker(viewState, eventHandler)
    }

    Scaffold(
        topBar = { AppBar(eventHandler) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(text = "Название") },
                modifier = Modifier.fillMaxWidth(),
                value = viewState.name,
                onValueChange = { eventHandler(NewTaskEvent.OnChangeName(it)) }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "Начало",
                    modifier = Modifier.width(72.dp)
                )
                DateTimeButton(
                    text = viewState.startDate.getNewTaskText(),
                    modifier = Modifier.weight(1f)
                ) {
                    eventHandler(NewTaskEvent.OnShowDatePicker(true))
                }
                Spacer(modifier = Modifier.width(8.dp))
                DateTimeButton(text = viewState.startTime.getNewTaskText()) {
                    eventHandler(NewTaskEvent.OnShowTimePicker(true))
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Конец",
                    modifier = Modifier.width(72.dp)
                )
                DateTimeButton(
                    text = viewState.finishDate.getNewTaskText(),
                    modifier = Modifier.weight(1f)
                ) {
                    eventHandler(NewTaskEvent.OnShowDatePicker(false))
                }
                Spacer(modifier = Modifier.width(8.dp))
                DateTimeButton(text = viewState.finishTime.getNewTaskText()) {
                    eventHandler(NewTaskEvent.OnShowTimePicker(false))
                }
            }
            OutlinedTextField(
                label = { Text(text = "Описание события") },
                modifier = Modifier.fillMaxWidth(),
                value = viewState.description,
                onValueChange = { eventHandler(NewTaskEvent.OnChangeDescription(it)) }
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = { eventHandler(NewTaskEvent.OnCreateTask) }) {
                    Text(text = "Добавить событие")
                }
            }
        }
    }
}

@Composable
private fun NewTaskBaseMaterialPicker(
    eventHandler: (NewTaskEvent) -> Unit,
    isShow: Boolean,
    content: @Composable() (MaterialDialogScope.() -> Unit)
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("ОК")
            negativeButton(
                text = "Отмена",
                onClick = { eventHandler(NewTaskEvent.OnClosePicker) }
            )
        },
        onCloseRequest = { eventHandler(NewTaskEvent.OnClosePicker) }
    ) {
        content()
    }

    if (isShow) {
        dialogState.show()
    } else {
        dialogState.hide()
    }
}

@Composable
private fun MaterialDialogScope.NewTaskDatePicker(
    viewState: NewTaskState,
    eventHandler: (NewTaskEvent) -> Unit,
) {
    if (viewState.isStartEdit) {
        datepicker(
            initialDate = viewState.startDate
        ) {
            eventHandler(NewTaskEvent.OnChangeStartDate(it))
            eventHandler(NewTaskEvent.OnClosePicker)
        }
    } else {
        datepicker(
            initialDate = viewState.finishDate
        ) {
            eventHandler(NewTaskEvent.OnChangeFinishDate(it))
            eventHandler(NewTaskEvent.OnClosePicker)
        }
    }
}

@Composable
private fun MaterialDialogScope.NewTaskTimePicker(
    viewState: NewTaskState,
    eventHandler: (NewTaskEvent) -> Unit,
) {
    if (viewState.isStartEdit) {
        timepicker(
            initialTime = viewState.startTime.toLocalTime(),
            is24HourClock = true
        ) {
            eventHandler(NewTaskEvent.OnChangeStartTime(it))
            eventHandler(NewTaskEvent.OnClosePicker)
        }
    } else {
        timepicker(
            initialTime = viewState.finishTime.toLocalTime(),
            is24HourClock = true
        ) {
            eventHandler(NewTaskEvent.OnChangeFinishTime(it))
            eventHandler(NewTaskEvent.OnClosePicker)
        }
    }
}

@Composable
private fun AppBar(eventHandler: (NewTaskEvent) -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.task)) },
        navigationIcon = {
            AppBarAction(image = Icons.Filled.ArrowBack, contentDescription = "back") {
                eventHandler(NewTaskEvent.OnNavigateBack)
            }
        }
    )
}

@Composable
private fun DateTimeButton(text: String, modifier: Modifier = Modifier, onClick: Click) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Purple40.copy(alpha = 0.2f),
            contentColor = Purple40
        ),
        modifier = modifier
    ) {
        Text(text = text)
    }
}
