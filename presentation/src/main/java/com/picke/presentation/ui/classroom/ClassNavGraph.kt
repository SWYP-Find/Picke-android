package com.picke.presentation.ui.classroom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute
import com.picke.presentation.ui.classroom.classcreate.ClassCreateScreen
import com.picke.presentation.ui.classroom.classdetail.ClassDetailScreen
import com.picke.presentation.ui.classroom.classjoin.ClassJoinScreen
import com.picke.presentation.ui.classroom.classshare.ClassShareScreen
import com.picke.presentation.ui.classroom.component.ClassTabScaffold
import com.picke.presentation.ui.classroom.myclass.MyClassScreen
import com.picke.presentation.ui.main.BottomNavItem

fun NavGraphBuilder.classGraph(
    navController: NavController,
    onTabClick: (BottomNavItem) -> Unit
) {
    val classIdArguments = listOf(navArgument(AppRoute.ARG_CLASS_ID) { type = NavType.LongType })

    composable(BottomNavItem.Class.route) {
        ClassScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToJoin = { navController.navigate(AppRoute.ClassJoin.route) },
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

    composable(AppRoute.ClassJoin.route) {
        ClassJoinScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToMyClass = {
                navController.navigate(AppRoute.MyClass.route) {
                    popUpTo(AppRoute.ClassJoin.route) { inclusive = true }
                }
            }
        )
    }

    composable(route = AppRoute.ClassShare.route, arguments = classIdArguments) {
        ClassShareScreen(
            onBackClick = { navController.popBackStack() },
            onShareCodeClick = { },
            onGoToClassClick = { classId ->
                navController.navigate(AppRoute.ClassDetail.createRoute(classId)) {
                    popUpTo(AppRoute.ClassShare.route) { inclusive = true }
                }
            }
        )
    }
    composable(AppRoute.MyClass.route) {
        ClassTabScaffold(onTabClick = onTabClick) { contentModifier ->
            MyClassScreen(
                onBackClick = { navController.popBackStack() },
                onClassClick = { classId ->
                    navController.navigate(AppRoute.ClassDetail.createRoute(classId))
                },
                onShareCodeClick = { classId ->
                    navController.navigate(AppRoute.ClassShare.createRoute(classId))
                },
                modifier = contentModifier
            )
        }
    }

    composable(route = AppRoute.ClassDetail.route, arguments = classIdArguments) {
        ClassTabScaffold(onTabClick = onTabClick) { contentModifier ->
            ClassDetailScreen(
                onBackClick = { navController.popBackStack() },
                onMemberClick = { },
                onBattleClick = { },
                onViewCodeClick = { classId ->
                    navController.navigate(AppRoute.ClassShare.createRoute(classId))
                },
                onResultClick = { },
                modifier = contentModifier
            )
        }
    }
}