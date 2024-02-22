package com.cesar.storicesar

import android.net.Uri
import app.cash.turbine.test
import com.cesar.storicesar.data.repository.UserRepository
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.entities.User
import com.cesar.storicesar.features.onboarding.OnboardingTakePhotoViewModel
import com.cesar.storicesar.features.onboarding.TakePhotoState
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
class RegisterViewModelTest {
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val repository: UserRepository = mock()
    private val uri: Uri = mock()

    private val takePhotoViewModel by lazy {
        OnboardingTakePhotoViewModel(repository)
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
    fun `Test register success`() {
        runTest {
            val user = User("name", "surname", "email", "pass")
            takePhotoViewModel.initUser(user)
            whenever(repository.register(user, uri)).thenReturn(flowOf(StateResult.Success(Unit)))
            takePhotoViewModel.registerState.test {
                takePhotoViewModel.saveUser(uri)
                awaitItem()
                val item = this.awaitItem()
                Assert.assertNotNull(item)
                Assert.assertEquals(TakePhotoState.Success, item)
            }
        }
    }

    @Test
    fun `Test register error`() {
        runTest {
            val user = User("name", "surname", "email", "pass")
            takePhotoViewModel.initUser(user)
            whenever(repository.register(user, uri)).thenReturn(flowOf(StateResult.Error))
            takePhotoViewModel.registerState.test {
                takePhotoViewModel.saveUser(uri)
                awaitItem()
                val item = this.awaitItem()
                Assert.assertNotNull(item)
                Assert.assertEquals(TakePhotoState.Error, item)
            }
        }
    }

}