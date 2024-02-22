package com.cesar.storicesar.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cesar.storicesar.entities.BankMovement
import com.cesar.storicesar.entities.User
import com.cesar.storicesar.features.home.HomeScreen
import com.cesar.storicesar.features.home.HomeViewModel
import com.cesar.storicesar.features.login.LoginContent
import com.cesar.storicesar.features.login.LoginViewModel
import com.cesar.storicesar.features.movementdetail.MovementDetailScreen
import com.cesar.storicesar.features.navigation.NavRoutes.ARG_MOVEMENT
import com.cesar.storicesar.features.navigation.NavRoutes.ARG_USER
import com.cesar.storicesar.features.navigation.NavRoutes.HOME
import com.cesar.storicesar.features.navigation.NavRoutes.LOGIN
import com.cesar.storicesar.features.navigation.NavRoutes.MOVEMENT_DETAIL
import com.cesar.storicesar.features.navigation.NavRoutes.ONBOARDING_PHOTO
import com.cesar.storicesar.features.navigation.NavRoutes.ONBOARDING_REGISTER
import com.cesar.storicesar.features.navigation.NavRoutes.ONBOARDING_SUCCESS
import com.cesar.storicesar.features.onboarding.OnboardingData
import com.cesar.storicesar.features.onboarding.OnboardingDataViewModel
import com.cesar.storicesar.features.onboarding.OnboardingSuccessScreen
import com.cesar.storicesar.features.onboarding.OnboardingTakePhotoScreen
import com.cesar.storicesar.features.onboarding.OnboardingTakePhotoViewModel

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = LOGIN,
    onFinish: () -> Unit
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(LOGIN) {
            val viewModel: LoginViewModel = hiltViewModel()

            LoginContent(
                uiState = viewModel.uiState,
                uiStateFlow = viewModel.loginState,
                onLogin = viewModel::login,
                onRegister = {
                    navController.navigate(ONBOARDING_REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(HOME)
                })
        }
        composable(ONBOARDING_REGISTER) {
            val viewModel: OnboardingDataViewModel = hiltViewModel()

            OnboardingData(
                uiState = viewModel.uiState,
                viewModel.uiStateFlow,
                viewModel::saveData,
                onSaveSuccess = {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(ARG_USER, it)
                    }
                    navController.navigate(ONBOARDING_PHOTO)
                })
        }
        composable(ONBOARDING_PHOTO) {
            val userArgument =
                navController.previousBackStackEntry?.savedStateHandle?.get<User>(
                    ARG_USER
                )
            val viewModel: OnboardingTakePhotoViewModel = hiltViewModel()
            if (userArgument != null) {
                viewModel.initUser(userArgument)
            }
            OnboardingTakePhotoScreen(
                viewModel.registerState,
                onImageSaved = { viewModel.saveUser(it) },
                onRegisterSuccess = {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(ARG_USER, userArgument)
                    }
                    navController.navigate(ONBOARDING_SUCCESS)
                })

        }
        composable(ONBOARDING_SUCCESS) {
            val userArgument =
                navController.previousBackStackEntry?.savedStateHandle?.get<User>(
                    ARG_USER
                )
            userArgument?.let { user ->
                OnboardingSuccessScreen(user, onFinish = {
                    navController.popBackStack(LOGIN, false)

                })
            }

        }
        composable(HOME) {
            val viewModel: HomeViewModel = hiltViewModel()
            viewModel.getMovements()
            HomeScreen(
                showEmptyMovements = viewModel.emptyMovements,
                bankAccount = viewModel.getBankAccount(),
                movements = viewModel.movements,
                onMovementClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set(ARG_MOVEMENT, it)
                    }
                    navController.navigate(MOVEMENT_DETAIL)
                },
                onBackPress = onFinish
            )
        }
        composable(
            MOVEMENT_DETAIL
        ) {

            val detailArgument =
                navController.previousBackStackEntry?.savedStateHandle?.get<BankMovement>(
                    ARG_MOVEMENT
                )
            if (detailArgument != null) {
                MovementDetailScreen(
                    detailArgument
                ) {
                    navController.popBackStack()
                }
            } else {
                navController.popBackStack()
            }

        }


    }
}
