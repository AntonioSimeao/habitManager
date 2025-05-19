package com.app.habitmanager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.habitmanager.data.HabitDatabaseProvider
import com.app.habitmanager.ui.theme.screens.HojeScreen
import com.app.habitmanager.ui.viewmodel.HojeViewModel
import com.app.habitmanager.ui.viewmodel.HojeViewModelFactory
import com.app.shared.data.HabitRepository

import com.app.shared.navigation.Screen
import com.app.shared.navigation.ui.SharedBottomBar
import com.app.shared.ui.screens.AcompanhamentoScreen
import com.app.shared.ui.screens.ConfiguracoesScreen

@Composable
fun AndroidApp() {
    val navController = rememberNavController()
    val items = listOf(Screen.Hoje, Screen.Configuracoes, Screen.Acompanhamento)
    val currentRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            SharedBottomBar(
                items = items,
                currentRoute = currentRoute?.destination?.route,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Hoje.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Hoje.route) {
                val context = LocalContext.current
                val db = remember { HabitDatabaseProvider.getDatabase(context) }
                val repository = remember { HabitRepository(db.habitDao()) }
                val viewModel: HojeViewModel = viewModel(
                    factory = HojeViewModelFactory(repository)
                )

                HojeScreen(viewModel)
            }
            composable(Screen.Configuracoes.route) { ConfiguracoesScreen() }
            composable(Screen.Acompanhamento.route) { AcompanhamentoScreen() }
        }
    }
}
