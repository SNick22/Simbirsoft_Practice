package com.example.mydiary.utils

import com.example.mydiary.domain.entity.TaskEntity
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.absoluteValue

const val TIMESTAMP_HOUR = 3600

private fun getGroupedTasks(tasks: MutableList<TaskEntity>, element: TaskEntity): PersistentList<TaskEntity> {
    val groupedTasks = mutableSetOf<TaskEntity>()
    for (i in tasks.indices) {
        val currentElement = tasks[i]
        if ((currentElement.dateStart - element.dateStart).absoluteValue < TIMESTAMP_HOUR) {
            groupedTasks.add(currentElement)
        }
    }
    tasks.removeAll(groupedTasks)
    if (groupedTasks.isNotEmpty()) {
        groupedTasks.forEach { task ->
            val childGroupedTasks = getGroupedTasks(tasks, task)
            groupedTasks.addAll(childGroupedTasks)
        }
    }
    groupedTasks.add(element)
    return groupedTasks.toPersistentList()
}

fun List<TaskEntity>.group(): PersistentList<PersistentList<TaskEntity>> {
    val oldList = this.sortedBy { it.dateStart }.toMutableList()
    val newList = mutableListOf<PersistentList<TaskEntity>>()
    while (oldList.isNotEmpty()) {
        val firstElement = oldList.removeFirst()
        val group = getGroupedTasks(oldList, firstElement)
        newList.add(group)
    }

    return newList.toPersistentList()
}