package com.picke.presentation.ui.classroom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classcreate.ClassCreateScreen
import com.picke.presentation.ui.classroom.classshare.ClassShareScreen
import com.picke.presentation.ui.classroom.component.ClassTabScaffold
import com.picke.presentation.ui.classroom.myclass.MyClassScreen
import com.picke.presentation.ui.main.BottomNavItem

fun NavGraphBuilder.classGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit
) {
    val classIdArguments = listOf(navArgument("classId") { type = NavType.LongType })

    composable(BottomNavItem.Class.route) {
        ClassScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToJoin = { },
            onNavigateToMyClass = { navController.navigate(AppRoute.MyClass.route) },
            onNavigateToCreate = { navController.navigate(AppRoute.ClassCreate.route) },
            onNavigateToTicket = { }
        )
    }

    composable(AppRoute.ClassCreate.route) {
        ClassCreateScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToShare = { classId ->
                navController.navigate(AppRoute.ClassShare.createRoute(classId)) {
                    popUpTo(AppRoute.ClassCreate.route) { inclusive = true }
                }
            }
        )
    }

    composable(route = AppRoute.ClassShare.route, arguments = classIdArguments) {
        ClassShareScreen(
            onBackClick = { navController.popBackStack() },
            onShareCodeClick = { },
            onGoToClassClick = { }
        )
    }
    composable(AppRoute.MyClass.route) {
        ClassTabScaffold(onTabClick = onTabClick) { contentModifier ->
            MyClassScreen(
                onBackClick = { navController.popBackStack() },
                onClassClick = { },
                onShareCodeClick = { classId ->
                    navController.navigate(AppRoute.ClassShare.createRoute(classId))
                },
                modifier = contentModifier
            )
        }
    }
}