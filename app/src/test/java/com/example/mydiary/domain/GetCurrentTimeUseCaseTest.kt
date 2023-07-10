package com.example.mydiary.domain

import com.example.mydiary.domain.entity.TimeEntity
import com.example.mydiary.domain.repository.TimeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetCurrentTimeUseCaseTest {

    @MockK
    lateinit var repository: TimeRepository

    private lateinit var useCase: GetCurrentTimeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetCurrentTimeUseCase(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenReturnedTimeIsCorrect() {
        //arrange
        val expectedTime = TimeEntity(3, 0, 0)
        coEvery { repository.getCurrentTime() } returns TimeEntity(3, 0, 0)

        //act
        runTest {
            val result = useCase.invoke()

            //assert
            assertEquals(expectedTime, result)
        }
    }
}
