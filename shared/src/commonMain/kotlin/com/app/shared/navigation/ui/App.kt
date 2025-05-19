package com.app.shared.navigation.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.app.shared.navigation.Screen


@Composable
fun SharedBottomBar(
    items: List<Screen>,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
){
    NavigationBar {
        items.forEach { screen ->

                NavigationBarItem(
                    selected = currentRoute == screen.route,
                    onClick = {onNavigate(screen.route) },
                    label = { Text(screen.title) },
                    icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) }
                )
            } }
    }

