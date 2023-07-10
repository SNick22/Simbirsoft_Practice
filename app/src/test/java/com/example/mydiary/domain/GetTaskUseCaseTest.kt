package com.example.mydiary.domain

import com.example.mydiary.domain.entity.TaskEntity
import com.example.mydiary.domain.repository.TaskRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetTaskUseCaseTest {

    @MockK
    lateinit var repository: TaskRepository

    private lateinit var useCase: GetTaskUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetTaskUseCase(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenReturnedTaskCorrect() {
        //arrange
        val expectedTask = TaskEntity(1, 0, 0, "name", "desc")
        coEvery {
            repository.getTask(1)
        } returns TaskEntity(1, 0, 0, "name", "desc")

        //act
        runTest {
            val result = useCase.invoke(1)

            //assert
            assertEquals(expectedTask, result)
        }
    }
}