package com.app.shared.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {

    object Hoje : Screen("hoje", "Hoje", Icons.Filled.Today)
    object Acompanhamento : Screen("acompanhamento", "Acompanhamento", Icons.Filled.Analytics)
    object Configuracoes : Screen("configuracoes", "Configurações", Icons.Filled.Settings)
}
