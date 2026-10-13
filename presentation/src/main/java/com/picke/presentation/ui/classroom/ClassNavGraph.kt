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
import com.picke.presentation.ui.classroom.classmember.ClassMemberScreen
import com.picke.presentation.ui.classroom.classshare.ClassShareScreen
import com.picke.presentation.ui.classroom.classvote.ClassVoteScreen
import com.picke.presentation.ui.classroom.component.ClassTabScaffold
import com.picke.presentation.ui.classroom.myclass.MyClassScreen
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.perspective.PerspectiveScreen
import com.picke.presentation.ui.scenario.ScenarioScreen
import com.picke.presentation.ui.vote.model.VoteType

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
            onNavigateToClassDetail = { classId ->
                navController.navigate(AppRoute.ClassDetail.createRoute(classId)) {
                    popUpTo(AppRoute.ClassJoin.route) { inclusive = true }
                }
            }
        )
    }

    composable(route = AppRoute.ClassShare.route, arguments = classIdArguments) {
        ClassShareScreen(
            onBackClick = { navController.popBackStack() },
            onShareCodeClick = { },
            onGoToClassClick = {
                navController.navigate(AppRoute.MyClass.route) {
                    popUpTo(AppRoute.ClassShare.route) { inclusive = true }
                }
            }
        )
    }
    composable(
        route = AppRoute.ClassVote.route,
        arguments = classIdArguments + navArgument(AppRoute.ARG_VOTE_TYPE) { type = NavType.StringType }
    ) {
        ClassVoteScreen(
            onBackClick = { navController.popBackStack() },
            onNavigateToDebate = { },
            onNavigateToScenario = { classId, battleId ->
                navController.navigate(AppRoute.ClassScenario.createRoute(classId, battleId)) {
                    popUpTo(AppRoute.ClassVote.route) { inclusive = true }
                }
            },
            onNavigateToPerspective = { classId, battleId ->
                navController.navigate(AppRoute.ClassPerspective.createRoute(classId, battleId)) {
                    popUpTo(AppRoute.ClassVote.route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = AppRoute.ClassScenario.route,
        arguments = classIdArguments + navArgument(AppRoute.ARG_BATTLE_ID) { type = NavType.LongType }
    ) { backStackEntry ->
        val classId = backStackEntry.arguments?.getLong(AppRoute.ARG_CLASS_ID) ?: 0L
        val battleId = backStackEntry.arguments?.getLong(AppRoute.ARG_BATTLE_ID) ?: 0L
        ScenarioScreen(
            battleId = battleId.toString(),
            onBackClick = { navController.popBackStack() },
            onNextClick = {
                navController.navigate(AppRoute.ClassVote.createRoute(classId, VoteType.POST)) {
                    popUpTo(AppRoute.ClassScenario.route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = AppRoute.ClassPerspective.route,
        arguments = classIdArguments + navArgument(AppRoute.ARG_BATTLE_ID) { type = NavType.StringType }
    ) {
        PerspectiveScreen(
            onBackClick = { navController.popBackStack() },
            onNextClick = { },
            onMoreClick = { itemId, firstOptionId ->
                navController.navigate(AppRoute.Comment.createRoute(itemId, firstOptionId))
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
                onMemberClick = { classId ->
                    navController.navigate(AppRoute.ClassMember.createRoute(classId))
                },
                onBattleClick = { classId ->
                    navController.navigate(AppRoute.ClassVote.createRoute(classId, VoteType.PRE))
                },
                onShareCodeClick = { classId ->
                    navController.navigate(AppRoute.ClassShare.createRoute(classId))
                },
                onResultClick = { },
                modifier = contentModifier
            )
        }
    }

    composable(route = AppRoute.ClassMember.route, arguments = classIdArguments) {
        ClassTabScaffold(onTabClick = onTabClick) { contentModifier ->
            ClassMemberScreen(
                onBackClick = { navController.popBackStack() },
                modifier = contentModifier
            )
        }
    }
}