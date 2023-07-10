package com.example.mydiary.presentation.screen.new_task

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.data.mappers.toTimeEntity
import com.example.mydiary.domain.GetCurrentTimeUseCase
import com.example.mydiary.domain.GetTodayUseCase
import com.example.mydiary.domain.SaveTaskUseCase
import com.example.mydiary.domain.entity.TaskEntity
import com.example.mydiary.domain.entity.TimeEntity
import com.example.mydiary.presentation.mappers.toLocalDate
import com.example.mydiary.presentation.mappers.toLocalTime
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAccessor
import javax.inject.Inject

@Immutable
data class NewTaskState(
    val name: String = "",
    val description: String = "",
    val startDate: LocalDate,
    val startTime: TimeEntity,
    val finishDate: LocalDate,
    val finishTime: TimeEntity,
    val isShowDatePicker: Boolean = false,
    val isShowTimePicker: Boolean = false,
    val isStartEdit: Boolean = false
)

sealed interface NewTaskEvent {
    data class OnChangeName(val value: String): NewTaskEvent
    data class OnChangeDescription(val value: String): NewTaskEvent
    object OnNavigateBack: NewTaskEvent
    data class OnChangeStartDate(val date: LocalDate): NewTaskEvent
    data class OnChangeFinishDate(val date: LocalDate): NewTaskEvent
    data class OnChangeStartTime(val date: LocalTime): NewTaskEvent
    data class OnChangeFinishTime(val date: LocalTime): NewTaskEvent
    data class OnShowDatePicker(val isStartEdit: Boolean): NewTaskEvent
    data class OnShowTimePicker(val isStartEdit: Boolean): NewTaskEvent
    object OnClosePicker: NewTaskEvent
    object OnCreateTask: NewTaskEvent
}

sealed interface NewTaskAction {
    data class ShowToast(val text: String): NewTaskAction
    object NavigateBack: NewTaskAction
}

class NewTaskViewModel @Inject constructor(
    getTodayUseCase: GetTodayUseCase,
    getCurrentTimeUseCase: GetCurrentTimeUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
): ViewModel() {

    private val _state = MutableStateFlow(
        let {
            val currentDay = getTodayUseCase().toLocalDate()
            val currentTime = getCurrentTimeUseCase()
            NewTaskState(
                startDate = currentDay,
                startTime = currentTime,
                finishDate = currentDay,
                finishTime = currentTime
            )
        }
    )
    val state: StateFlow<NewTaskState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<NewTaskAction?>()
    val action: SharedFlow<NewTaskAction?>
        get() = _action.asSharedFlow()

    fun event(event: NewTaskEvent) {
        when(event) {
            is NewTaskEvent.OnChangeDescription -> onChangeDescription(event.value)
            is NewTaskEvent.OnChangeName -> onChangeName(event.value)
            NewTaskEvent.OnNavigateBack -> onNavigateBack()
            is NewTaskEvent.OnChangeFinishDate -> onChangeFinishDate(event.date)
            is NewTaskEvent.OnChangeFinishTime -> onChangeFinishTime(event.date)
            is NewTaskEvent.OnChangeStartDate -> onChangeStartDate(event.date)
            is NewTaskEvent.OnChangeStartTime -> onChangeStartTime(event.date)
            NewTaskEvent.OnClosePicker -> onClosePicker()
            is NewTaskEvent.OnShowDatePicker -> onShowDatePicker(event.isStartEdit)
            is NewTaskEvent.OnShowTimePicker -> onShowTimePicker(event.isStartEdit)
            NewTaskEvent.OnCreateTask -> onCreateTask()
        }
    }

    private fun onCreateTask() {
        val stateValue = _state.value
        val taskName = if (stateValue.name.isEmpty()) "Событие" else _state.value.name

        val timeZone = ZoneId.systemDefault()

        val startTime = LocalDateTime
            .of(stateValue.startDate, stateValue.startTime.toLocalTime())
            .atZone(timeZone)
            .toEpochSecond()
        val finishTime = LocalDateTime
            .of(stateValue.finishDate, stateValue.finishTime.toLocalTime())
            .atZone(timeZone)
            .toEpochSecond()

        viewModelScope.launch {
            if (startTime > finishTime) {
                _action.emit(NewTaskAction.ShowToast("Время завершения должно быть больше времени начала"))
            } else {
                val task = TaskEntity(
                    dateStart = startTime,
                    dateFinish = finishTime,
                    name = taskName,
                    description = stateValue.description
                )
                saveTaskUseCase(task)
                _action.emit(NewTaskAction.NavigateBack)
            }
        }
    }

    private fun onChangeFinishDate(date: LocalDate) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    finishDate = date
                )
            )
        }
    }

    private fun onChangeFinishTime(time: LocalTime) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    finishTime = time.toTimeEntity()
                )
            )
        }
    }

    private fun onChangeStartDate(date: LocalDate) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    startDate = date
                )
            )
        }
    }

    private fun onChangeStartTime(time: LocalTime) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    startTime = time.toTimeEntity()
                )
            )
        }
    }

    private fun onClosePicker() {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    isShowDatePicker = false,
                    isShowTimePicker = false
                )
            )
        }
    }

    private fun onShowDatePicker(isStartEdit: Boolean) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    isShowDatePicker = true,
                    isStartEdit = isStartEdit
                )
            )
        }
    }

    private fun onShowTimePicker(isStartEdit: Boolean) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    isShowTimePicker = true,
                    isStartEdit = isStartEdit
                )
            )
        }
    }

    private fun onNavigateBack() {
        viewModelScope.launch {
            _action.emit(NewTaskAction.NavigateBack)
        }
    }

    private fun onChangeDescription(value: String) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    description = value
                )
            )
        }
    }

    private fun onChangeName(value: String) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    name = value
                )
            )
        }
    }
}