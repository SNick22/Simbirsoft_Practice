package com.example.mydiary.presentation.screen.task_list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.domain.GetDayByDataUseCase
import com.example.mydiary.domain.GetDaysOfWeekUseCase
import com.example.mydiary.domain.GetTasksUseCase
import com.example.mydiary.domain.GetTodayUseCase
import com.example.mydiary.domain.SaveMocksUseCase
import com.example.mydiary.domain.entity.DayEntity
import com.example.mydiary.domain.entity.TaskEntity
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class TaskListState(
    val selectedDay: DayEntity,
    val today: DayEntity,
    val loadedDaysOfWeek: PersistentMap<Int, PersistentList<DayEntity>> = persistentMapOf(),
    val showCalendar: Boolean = false,
    val tasks: PersistentList<TaskEntity> = persistentListOf()
)

sealed interface TaskListEvent {
    data class OnLoadDaysOfWeek(val week: Int): TaskListEvent
    data class OnChangeSelectedDay(val day: DayEntity): TaskListEvent
    data class OnChangeSelectedDayByData(val day: Int, val month: Int, val year: Int): TaskListEvent
    object OnShowCalendar: TaskListEvent
    object OnCloseCalendar: TaskListEvent
    data class OnNavigateToDetails(val taskId: Int): TaskListEvent
    object OnLoadTasks: TaskListEvent
    object OnNavigateToNewTask: TaskListEvent
}

sealed interface TaskListAction {
    data class NavigateToDetails(val taskId: Int): TaskListAction
    object NavigateToNewTask: TaskListAction
}

class TaskListViewModel @Inject constructor(
    private val getDaysOfWeekUseCase: GetDaysOfWeekUseCase,
    getTodayUseCase: GetTodayUseCase,
    private val getDayByDataUseCase: GetDayByDataUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val saveMocksUseCase: SaveMocksUseCase
): ViewModel() {

    private val _state = MutableStateFlow(
        let{
            val today = getTodayUseCase()
            TaskListState(today = today, selectedDay = today)
        }
    )
    val state: StateFlow<TaskListState>
        get() = _state.asStateFlow()

    private val _action = MutableSharedFlow<TaskListAction?>()
    val action: SharedFlow<TaskListAction?>
        get() = _action.asSharedFlow()

    init {
        viewModelScope.launch {
            saveMocksUseCase()
        }
    }

    fun event(event: TaskListEvent) {
        when(event) {
            is TaskListEvent.OnLoadDaysOfWeek -> onLoadDaysOfWeek(event.week)
            is TaskListEvent.OnChangeSelectedDay -> onChangeSelectedDay(event.day)
            is TaskListEvent.OnChangeSelectedDayByData -> onChangeSelectedDayByData(
                event.day, event.month, event.year
            )
            TaskListEvent.OnShowCalendar -> onShowCalendar()
            TaskListEvent.OnCloseCalendar -> onCloseCalendar()
            is TaskListEvent.OnNavigateToDetails -> onNavigateToDetails(event.taskId)
            TaskListEvent.OnLoadTasks -> onLoadTasks()
            TaskListEvent.OnNavigateToNewTask -> onNavigateToNewTask()
        }
    }

    private fun onNavigateToNewTask() {
        viewModelScope.launch {
            _action.emit(TaskListAction.NavigateToNewTask)
        }
    }

    private fun onLoadTasks() {
        viewModelScope.launch {
            val selectedDay = _state.value.selectedDay
            val tasks = getTasksUseCase(selectedDay.timeStart, selectedDay.timeFinish)
            _state.emit(
                _state.value.copy(
                    tasks = tasks.toPersistentList()
                )
            )
        }
    }

    private fun onNavigateToDetails(taskId: Int) {
        viewModelScope.launch {
            _action.emit(TaskListAction.NavigateToDetails(taskId))
        }
    }

    private fun onLoadDaysOfWeek(week: Int) {
        viewModelScope.launch {
            val listOfDays = getDaysOfWeekUseCase(week)
            _state.emit(
                _state.value.copy(
                    loadedDaysOfWeek = _state.value.loadedDaysOfWeek
                        .put(week, listOfDays.toPersistentList())
                        .remove(week - 3)
                        .remove(week + 3)
                )
            )
        }
    }

    private fun onChangeSelectedDay(day: DayEntity) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    selectedDay = day,
                    tasks = getTasksUseCase(day.timeStart, day.timeFinish).toPersistentList()
                )
            )
        }
    }

    private fun onChangeSelectedDayByData(day: Int, month: Int, year: Int) {
        viewModelScope.launch {
            val selectedDay = getDayByDataUseCase(day, month, year)
            onChangeSelectedDay(selectedDay)
        }
    }

    private fun onShowCalendar() {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    showCalendar = true
                )
            )
        }
    }

    private fun onCloseCalendar() {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    showCalendar = false
                )
            )
        }
    }
}