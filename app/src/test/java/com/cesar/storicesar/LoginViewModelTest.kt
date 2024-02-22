package com.cesar.storicesar

import app.cash.turbine.test
import com.cesar.storicesar.data.repository.UserRepository
import com.cesar.storicesar.entities.StateResult
import com.cesar.storicesar.features.login.LoginState
import com.cesar.storicesar.features.login.LoginViewModel
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
class LoginViewModelTest {


    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val repository: UserRepository = mock()

    private val loginView by lazy {
        LoginViewModel(repository)
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
    fun `Test login success`() {
        runTest {
            val email = "test@test.com"
            val password = "password"
            loginView.uiState.setEmailText(email)
            loginView.uiState.setPasswordText(password)
            whenever(
                repository.login(
                    email,
                    password
                )
            ).thenReturn(flowOf(StateResult.Success(Unit)))

            loginView.loginState.test {
                loginView.login(email, password)
                awaitItem()
                val item = awaitItem()
                Assert.assertNotNull(item)
                Assert.assertEquals(LoginState.Success, item)
            }
        }
    }

    @Test
    fun `Test login error`() {
        runTest {
            val email = "test@test.com"
            val password = "password"
            loginView.uiState.setEmailText(email)
            loginView.uiState.setPasswordText(password)
            whenever(
                repository.login(
                    email,
                    password
                )
            ).thenReturn(flowOf(StateResult.Error))

            loginView.loginState.test {
                loginView.login(email, password)
                awaitItem()
                val item = awaitItem()
                Assert.assertNotNull(item)
                Assert.assertEquals(LoginState.Error, item)
            }
        }
    }

    @Test
    fun `Test login validation email not valid`() {
        runTest {
            val email = "test.com"
            val password = "password"
            loginView.uiState.setEmailText(email)
            loginView.uiState.setPasswordText(password)
            loginView.uiState.validateFields()
            Assert.assertEquals(
                "Please fill with a valid email",
                loginView.uiState.errorEmailMessage
            )
        }
    }

    @Test
    fun `Test login validation email and password empty`() {
        runTest {
            val email = ""
            val password = ""
            loginView.uiState.setEmailText(email)
            loginView.uiState.setPasswordText(password)
            Assert.assertEquals(
                false,
                loginView.uiState.validateFields()
            )
        }
    }
}