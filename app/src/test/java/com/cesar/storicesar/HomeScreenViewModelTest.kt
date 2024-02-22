package com.cesar.storicesar

import com.cesar.storicesar.data.repository.MovementRepository
import com.cesar.storicesar.entities.BankMovement
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.features.home.HomeViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val movementRepository: MovementRepository = mock()

    private val homeViewModel by lazy {
        HomeViewModel(movementRepository)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `Test get movements success`() {
        runTest {
            val movements = listOf(
                BankMovement("title", "description", "amount"),
                BankMovement("title", "description", "amount"),
                BankMovement("title", "description", "amount"),
                BankMovement("title", "description", "amount")
            )
            whenever(movementRepository.getMovements()).thenReturn(
                flowOf(
                    StateResult.Success(
                        movements
                    )
                )
            )

            val job = homeViewModel.getMovements()
            job.join()

            Assert.assertEquals(movements, homeViewModel.movements)

        }
    }


    @Test
    fun `Test get movements error`() {
        runTest {

            whenever(movementRepository.getMovements()).thenReturn(
                flowOf(
                    StateResult.Error
                )
            )

            val job = homeViewModel.getMovements()
            job.join()

            Assert.assertTrue(homeViewModel.emptyMovements)

        }
    }

}