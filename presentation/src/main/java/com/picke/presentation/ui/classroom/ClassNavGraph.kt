package com.picke.presentation.ui.classroom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.picke.presentation.ui.main.BottomNavItem

fun NavGraphBuilder.classGraph(navController: NavController) {
    composable(BottomNavItem.Class.route) {
        ClassScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToJoin = { },
            onNavigateToMyClass = { },
            onNavigateToCreate = { },
            onNavigateToTicket = { }
        )
    }
}