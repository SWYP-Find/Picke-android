package com.picke.presentation.ui.battleentry

import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.picke.presentation.AppRoute
import com.picke.presentation.analytics.ContentActionType
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.comment.CommentScreen
import com.picke.presentation.ui.perspective.PerspectiveScreen
import com.picke.presentation.ui.recommend.RecommendScreen
import com.picke.presentation.ui.scenario.ScenarioScreen
import com.picke.presentation.ui.vote.VoteScreen
import com.picke.presentation.ui.vote.model.VoteType

fun NavGraphBuilder.battleGraph(
    navController: NavController,
    onNavigateToExplore: () -> Unit
) {
    composable(
        route = AppRoute.BattleRouting.route,
        arguments = listOf(navArgument("battleId") { type = NavType.StringType })
    ) { backStackEntry ->
        val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
        BattleRoutingScreen(
            onNavigateToPreVote = { id ->
                navController.navigate(AppRoute.PreVote.createRoute(id)) {
                    popUpTo(AppRoute.BattleRouting.route) { inclusive = true }
                }
            },
            onNavigateToPerspective = { id ->
                navController.navigate(AppRoute.Perspective.createRoute(id)) {
                    popUpTo(AppRoute.BattleRouting.route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = AppRoute.PreVote.route,
        arguments = listOf(navArgument("battleId") { type = NavType.StringType })
    ) { backStackEntry ->
        val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
        VoteScreen(
            voteType = VoteType.PRE,
            onBackClick = dropUnlessResumed {
                val prevRoute = navController.previousBackStackEntry?.destination?.route
                if (prevRoute == null || prevRoute == AppRoute.Splash.route) {
                    navController.navigate(AppRoute.Main.createRoute()) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            },
            onVoteSubmit = { submittedBattleId ->
                navController.navigate(AppRoute.Scenario.createRoute(submittedBattleId)) {
                    popUpTo(AppRoute.PreVote.route) { inclusive = true }
                }
            },
            onNavigateToExplore = onNavigateToExplore
        )
    }

    composable(
        route = AppRoute.Scenario.route,
        arguments = listOf(navArgument("battleId") { type = NavType.StringType })
    ) { backStackEntry ->
        val contentId = backStackEntry.arguments?.getString("battleId") ?: ""
        ScenarioScreen(
            battleId = contentId,
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onNextClick = {
                navController.navigate(AppRoute.PostVote.createRoute(contentId)) {
                    popUpTo(AppRoute.Scenario.route) { inclusive = true }
                }
            }
        )
    }

    composable(
        route = AppRoute.PostVote.route,
        arguments = listOf(navArgument("battleId") { type = NavType.StringType })
    ) { backStackEntry ->
        val battleId = backStackEntry.arguments?.getString("battleId") ?: ""
        VoteScreen(
            voteType = VoteType.POST,
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onVoteSubmit = { submittedBattleId ->
                navController.navigate(AppRoute.Perspective.createRoute(submittedBattleId)) {
                    popUpTo(AppRoute.Main.route) { inclusive = false }
                }
            },
            onNavigateToExplore = onNavigateToExplore
        )
    }

    composable(
        route = AppRoute.Perspective.route,
        arguments = listOf(
            navArgument(AppRoute.ARG_BATTLE_ID) { type = NavType.StringType },
            navArgument(AppRoute.ARG_PERSPECTIVE_ID) {
                type = NavType.StringType; nullable = true; defaultValue = null
            }
        )
    ) { backStackEntry ->
        val perspectiveId = backStackEntry.arguments?.getString(AppRoute.ARG_PERSPECTIVE_ID)
        PerspectiveScreen(
            onBackClick = dropUnlessResumed {
                val prevRoute = navController.previousBackStackEntry?.destination?.route
                if (prevRoute == null || prevRoute == AppRoute.Splash.route || prevRoute == AppRoute.Login.route) {
                    navController.navigate(AppRoute.Main.createRoute()) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            },
            onNextClick = { itemId ->
                navController.navigate(AppRoute.Recommend.createRoute(itemId.toString()))
            },
            onMoreClick = { itemId, firstOptionId ->
                navController.navigate(
                    AppRoute.Comment.createRoute(itemId.toString(), firstOptionId)
                )
            },
            scrollToPerspectiveId = perspectiveId?.toLongOrNull()
        )
    }

    composable(
        route = AppRoute.Comment.route,
        arguments = listOf(
            navArgument(AppRoute.ARG_PERSPECTIVE_ID) {
                type = NavType.StringType
            },
            navArgument(AppRoute.ARG_FIRST_OPTION_ID) {
                type = NavType.LongType; defaultValue = 0L
            },
            navArgument(AppRoute.ARG_COMMENT_ID) {
                type = NavType.StringType; nullable = true; defaultValue = null
            }
        )
    ) { backStackEntry ->
        val commentId = backStackEntry.arguments?.getString(AppRoute.ARG_COMMENT_ID)
        CommentScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            scrollToCommentId = commentId?.toLongOrNull()
        )
    }

    composable(
        route = AppRoute.Recommend.route,
        arguments = listOf(navArgument("battleId") { type = NavType.StringType })
    ) {
        val analyticsTracker = rememberAnalyticsTracker()
        RecommendScreen(
            onBackClick = dropUnlessResumed { navController.popBackStack() },
            onCloseClick = {
                analyticsTracker.trackContentAction(ContentActionType.BATTLE_RECOMMEND_CLOSE)
                navController.popBackStack(AppRoute.Main.route, inclusive = false)
            },
            onItemClick = { clickedBattleId ->
                analyticsTracker.trackContentAction(
                    ContentActionType.BATTLE_CARD_TAP,
                    clickedBattleId
                )
                navController.navigate(AppRoute.BattleRouting.createRoute(clickedBattleId))
            }
        )
    }
}