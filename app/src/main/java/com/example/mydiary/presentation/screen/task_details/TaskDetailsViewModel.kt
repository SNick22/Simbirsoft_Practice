package com.example.mydiary.presentation.screen.task_details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.domain.DeleteTaskUseCase
import com.example.mydiary.domain.GetDayByTimestampUseCase
import com.example.mydiary.domain.GetTaskUseCase
import com.example.mydiary.domain.entity.DayEntity
import com.example.mydiary.domain.entity.TaskEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
data class TaskDetailsState(
    val task: TaskEntity? = null,
    val day: DayEntity? = null,
    val isConfirmDialog: Boolean = false
)

sealed interface TaskDetailsEvent {
    object OnShowConfirm: TaskDetailsEvent
    object OnCloseConfirm: TaskDetailsEvent
    object OnDeleteTask: TaskDetailsEvent
    object OnNavigateBack: TaskDetailsEvent
}

sealed interface TaskDetailsAction {
    object NavigateBack: TaskDetailsAction
}

class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val taskId: Int,
    private val getTaskUseCase: GetTaskUseCase,
    private val getDayByTimestampUseCase: GetDayByTimestampUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
): ViewModel() {

    private val _state = MutableStateFlow(TaskDetailsState())
    val state: StateFlow<TaskDetailsState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<TaskDetailsAction?>()
    val action: SharedFlow<TaskDetailsAction?>
        get() = _action.asSharedFlow()

    init {
        viewModelScope.launch {
            val task = getTaskUseCase(taskId)
            val day = getDayByTimestampUseCase(task.dateStart)
            _state.emit(
                _state.value.copy(
                    task = task,
                    day = day
                )
            )
        }
    }

    fun event(event: TaskDetailsEvent) {
        when(event) {
            TaskDetailsEvent.OnShowConfirm -> onShowConfirm()
            TaskDetailsEvent.OnCloseConfirm -> onCloseConfirm()
            TaskDetailsEvent.OnDeleteTask -> onDeleteTask()
            TaskDetailsEvent.OnNavigateBack -> onNavigateBack()
        }
    }

    private fun onShowConfirm() {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    isConfirmDialog = true
                )
            )
        }
    }

    private fun onCloseConfirm() {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    isConfirmDialog = false
                )
            )
        }
    }

    private fun onDeleteTask() {
        viewModelScope.launch {
            _state.value.task?.let {
                deleteTaskUseCase(it)
            }
        }.invokeOnCompletion {
            onNavigateBack()
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _action.emit(TaskDetailsAction.NavigateBack)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted taskId: Int): TaskDetailsViewModel
    }
}