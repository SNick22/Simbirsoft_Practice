package com.example.mydiary.presentation.screen.task_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mydiary.R
import com.example.mydiary.di.task_details.TaskDetailsComponentHolder
import com.example.mydiary.presentation.screen.task_list.AppBarAction
import com.example.mydiary.utils.daggerViewModel
import com.example.mydiary.utils.getTime
import com.example.mydiary.utils.toToDoListString

@Composable
fun TaskDetailsScreen(taskId: Int, navController: NavController) {
    val context = LocalContext.current

    val viewModel = daggerViewModel {
        TaskDetailsComponentHolder.get(context)
            .getViewModelFactory()
            .create(taskId)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    TaskDetailsContent(
        state,
        viewModel::event,
    )

    TaskDetailsActions(
        action,
        navController
    )
}

@Composable
private fun TaskDetailsActions(
    viewAction: TaskDetailsAction?,
    navController: NavController
) {
    LaunchedEffect(viewAction) {
        when(viewAction) {
            null -> Unit
            TaskDetailsAction.NavigateBack -> navController.navigateUp()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun TaskDetailsContent(
    viewState: TaskDetailsState,
    eventHandler: (TaskDetailsEvent) -> Unit,
) {
    Scaffold(
        topBar = { AppBar(eventHandler) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(
                text = viewState.task?.name ?: "",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = viewState.day?.toToDoListString() ?: "",
                style = MaterialTheme.typography.caption,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "с ${viewState.task?.dateStart?.getTime()} до ${viewState.task?.dateFinish?.getTime()}",
                style = MaterialTheme.typography.caption,
                color = Color.DarkGray,
            )
            Text(
                text = viewState.task?.description ?: "",
                modifier = Modifier.padding(16.dp),
            )
        }
    }

    if (viewState.isConfirmDialog) {
        ConfirmDialog(eventHandler)
    }
}

@Composable
private fun ConfirmDialog(eventHandler: (TaskDetailsEvent) -> Unit) {
    AlertDialog(
        onDismissRequest = { eventHandler(TaskDetailsEvent.OnCloseConfirm) },
        title = { Text(text = "Подтвердите удаление") },
        text = { Text(text = "Вы уверены, что хотите удалить это событие?") },
        confirmButton = {
            TextButton(onClick = { eventHandler(TaskDetailsEvent.OnDeleteTask) }) {
                Text(text = "Ок")
            }
        },
        dismissButton = {
            TextButton(onClick = { eventHandler(TaskDetailsEvent.OnCloseConfirm) }) {
                Text(text = "Отмена")
            }
        }
    )
}

@Composable
private fun AppBar(eventHandler: (TaskDetailsEvent) -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.details)) },
        navigationIcon = {
            AppBarAction(image = Icons.Filled.ArrowBack, contentDescription = "back") {
                eventHandler(TaskDetailsEvent.OnNavigateBack)
            }
        },
        actions = {
            AppBarAction(image = Icons.Filled.Delete, contentDescription = "delete") {
                eventHandler(TaskDetailsEvent.OnShowConfirm)
            }
        }
    )
}
