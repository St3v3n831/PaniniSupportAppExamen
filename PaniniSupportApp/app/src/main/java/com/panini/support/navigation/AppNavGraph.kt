package com.panini.support.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.panini.support.data.repository.MockAuthRepository
import com.panini.support.data.repository.MockTicketRepository
import com.panini.support.ui.screens.create.CreateTicketScreen
import com.panini.support.ui.screens.create.CreateTicketViewModel
import com.panini.support.ui.screens.create.CreateTicketViewModelFactory
import com.panini.support.ui.screens.detail.TicketDetailScreen
import com.panini.support.ui.screens.detail.TicketDetailViewModel
import com.panini.support.ui.screens.detail.TicketDetailViewModelFactory
import com.panini.support.ui.screens.flags.FeatureFlagsScreen
import com.panini.support.ui.screens.login.LoginScreen
import com.panini.support.ui.screens.login.LoginViewModel
import com.panini.support.ui.screens.login.LoginViewModelFactory
import com.panini.support.ui.screens.tickets.TicketListScreen
import com.panini.support.ui.screens.tickets.TicketListViewModel
import com.panini.support.ui.screens.tickets.TicketListViewModelFactory

private val ticketRepository = MockTicketRepository()
private val authRepository   = MockAuthRepository()

@Composable
fun AppNavGraph(navController: NavHostController) {

    NavHost(
        navController    = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            val vm: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(authRepository)
            )
            LoginScreen(
                viewModel      = vm,
                onLoginSuccess = {
                    navController.navigate(Screen.TicketList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.TicketList.route) {
            val vm: TicketListViewModel = viewModel(
                factory = TicketListViewModelFactory(ticketRepository)
            )
            TicketListScreen(
                viewModel      = vm,
                onTicketClick  = { ticketId ->
                    navController.navigate(Screen.TicketDetail.createRoute(ticketId))
                },
                onCreateTicket = {
                    navController.navigate(Screen.CreateTicket.route)
                },
                onOpenFlags    = {
                    navController.navigate(Screen.FeatureFlags.route)
                }
            )
        }

        composable(
            route     = Screen.TicketDetail.route,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId") ?: return@composable
            val vm: TicketDetailViewModel = viewModel(
                factory = TicketDetailViewModelFactory(ticketRepository, ticketId)
            )
            TicketDetailScreen(
                viewModel = vm,
                onBack    = { navController.popBackStack() }
            )
        }

        composable(Screen.CreateTicket.route) {
            val vm: CreateTicketViewModel = viewModel(
                factory = CreateTicketViewModelFactory(ticketRepository)
            )
            CreateTicketScreen(
                viewModel       = vm,
                onBack          = { navController.popBackStack() },
                onTicketCreated = { navController.popBackStack() }
            )
        }

        composable(Screen.FeatureFlags.route) {
            FeatureFlagsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}