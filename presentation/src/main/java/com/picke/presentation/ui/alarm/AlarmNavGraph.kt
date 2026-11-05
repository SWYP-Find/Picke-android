package com.picke.presentation.ui.alarm

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.picke.presentation.navigation.AppRoute

fun NavGraphBuilder.alarmGraph(navController: NavController) {
    composable(AppRoute.Alarm.route) {
        AlarmScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNavigateToPreVote = { battleId ->
                navController.navigate(AppRoute.PreVote.createRoute(battleId))
            },
            onNavigateToComment = { perspectiveId, commentId ->
                navController.navigate(AppRoute.Comment.createRoute(perspectiveId, commentId))
            },
            onNavigateToPoint = {
                navController.navigate(AppRoute.Point.route)
            },
            onNavigateToNotice = { noticeId ->
                navController.navigate(AppRoute.NoticeEvent.createRoute(noticeId))
            },
            onNavigateToTodayBattle = {
                navController.navigate(AppRoute.TodayBattle.createRoute(""))
            }
        )
    }
}