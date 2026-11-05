package com.picke.presentation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.picke.presentation.analytics.OnboardingStep
import com.picke.presentation.analytics.TrackScreenViews
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.deeplink.DeepLinkEvent
import com.picke.presentation.deeplink.DeepLinkHandler
import com.picke.presentation.ui.alarm.alarmGraph
import com.picke.presentation.ui.battleentry.battleGraph
import com.picke.presentation.ui.classroom.classGraph
import com.picke.presentation.ui.component.NotificationPermissionBottomSheet
import com.picke.presentation.ui.component.TermsOfServiceBottomSheet
import com.picke.presentation.ui.login.LoginScreen
import com.picke.presentation.ui.main.BottomNavItem
import com.picke.presentation.ui.main.MainScreen
import com.picke.presentation.ui.main.toTabAction
import com.picke.presentation.ui.my.myGraph
import com.picke.presentation.ui.onboarding.OnboardingScreen
import com.picke.presentation.ui.splash.SplashViewModel
import com.picke.presentation.ui.splash.model.SplashUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.todaybattle.todayBattleGraph
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@Composable
fun AppNavigation(
    splashViewModel: SplashViewModel,
    deepLinkHandler: DeepLinkHandler
) {
    val rootNavController = rememberNavController()
    val uiState by splashViewModel.uiState.collectAsStateWithLifecycle()
    val analyticsTracker = rememberAnalyticsTracker()

    // §2 화면 enum에 등재된 화면의 screen_view 자동 전송
    TrackScreenViews(rootNavController)

    var showNotificationSheet by remember { mutableStateOf(false) }
    var showTermsSheet by remember { mutableStateOf(false) }
    var requestedMainTab by remember { mutableStateOf<BottomNavItem?>(null) }
    var requestedMainRoute by remember { mutableStateOf<String?>(null) }

    val navigateToTab: (BottomNavItem) -> Unit = { item ->
        rootNavController.navigateToTab(item = item, onMainTabRequest = { requestedMainTab = it })
    }

    LaunchedEffect(showTermsSheet) {
        if (showTermsSheet) analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_SHOWN)
    }
    LaunchedEffect(showNotificationSheet) {
        if (showNotificationSheet) analyticsTracker.trackOnboardingStep(OnboardingStep.PERMISSION_ASKED)
    }

    val requestNotificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* FCM 토큰 발급은 추후 연동 */ }

    fun checkAndShowNotificationSheet(isNewUser: Boolean) {
        if (!isNewUser) return                                  // 신규 가입자에게만
        // 신규 가입자는 이전에 물어본 적이 있든 없든, 알림 권한이 켜져 있든 아니든 무조건 안내 시트를 띄운다.
        showNotificationSheet = true
    }

    fun markNotificationPermissionAsked() {
        splashViewModel.markNotificationPermissionAsked()
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SplashUiState.NavigateToOnboarding -> {
                rootNavController.navigate(AppRoute.Onboarding.route) { popUpTo(0) }
            }

            is SplashUiState.NavigateToLogin -> {
                rootNavController.navigate(AppRoute.Login.route) { popUpTo(0) }
            }

            is SplashUiState.NavigateToMain -> {
                rootNavController.navigate(AppRoute.Main.createRoute()) { popUpTo(0) }
                if (state.needsTermsAgreement) showTermsSheet = true
            }

            is SplashUiState.Loading -> return@LaunchedEffect

            is SplashUiState.NavigationHandled -> return@LaunchedEffect
        }

        splashViewModel.onNavigationHandled()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PickeTheme.colors.backgroundBeige
    ) {
        LaunchedEffect(Unit) {
            val isMainInBackStack = rootNavController.currentBackStackEntryFlow.map {
                runCatching { rootNavController.getBackStackEntry(AppRoute.Main.route) }.isSuccess
            }
            combine(deepLinkHandler.pendingEvent, isMainInBackStack) { event, isMainReady ->
                event.takeIf { isMainReady }
            }.filterNotNull().collect { event ->
                deepLinkHandler.consume()
                rootNavController.navigate(AppRoute.Main.createRoute()) {
                    popUpTo(AppRoute.Main.route) { inclusive = false }
                    launchSingleTop = true
                }
                when (event) {
                    is DeepLinkEvent.GoToBattle -> {
                        rootNavController.navigate(AppRoute.BattleRouting.createRoute(event.battleId))
                    }

                    is DeepLinkEvent.GoToTodayBattle -> {
                        rootNavController.navigate(AppRoute.TodayBattle.createRoute(event.battleId))
                    }

                    is DeepLinkEvent.GoToReport -> {
                        rootNavController.navigate(AppRoute.OtherPhilosopher.createRoute(event.reportId))
                    }

                    is DeepLinkEvent.GoToAlarm -> {
                        rootNavController.navigate(AppRoute.Alarm.route)
                    }

                    is DeepLinkEvent.GoToPerspective -> {
                        if (event.commentId != null) {
                            rootNavController.navigate(
                                AppRoute.Comment.createRoute(event.perspectiveId, event.commentId)
                            )
                        } else {
                            rootNavController.navigate(AppRoute.Comment.createRoute(event.perspectiveId))
                        }
                    }
                }
            }
        }

        NavHost(
            navController = rootNavController,
            startDestination = "blank_start",
            modifier = Modifier.fillMaxSize(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable("blank_start") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PickeTheme.colors.primary500),
                    contentAlignment = Alignment.Center
                ) { }
            }

            composable(AppRoute.Onboarding.route) {
                OnboardingScreen(
                    onNavigateToLogin = {
                        rootNavController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = AppRoute.Login.route,
                exitTransition = { fadeOut(animationSpec = tween(100)) }
            ) {
                LoginScreen(
                    onNavigateToMain = { isNewUser ->
                        analyticsTracker.trackOnboardingStep(OnboardingStep.HOME_ENTERED)
                        rootNavController.navigate(AppRoute.Main.createRoute()) {
                            popUpTo(AppRoute.Login.route) { inclusive = true }
                        }
                        checkAndShowNotificationSheet(isNewUser)
                    }
                )
            }

            composable(AppRoute.Main.route) {
                MainScreen(
                    rootNavController = rootNavController,
                    requestedTab = requestedMainTab,
                    requestedRoute = requestedMainRoute,
                    onRootTabClick = navigateToTab,
                    onRequestedTabOpened = { requestedMainTab = null },
                    onRequestedRouteOpened = { requestedMainRoute = null },
                    isNotificationSheetPending = showNotificationSheet
                )
            }

            classGraph(
                navController = rootNavController,
                onTabClick = { item ->
                    analyticsTracker.trackUiAction(item.toTabAction())
                    navigateToTab(item)
                },
                onNavigateToTicket = {
                    requestedMainRoute = AppRoute.Ticket.route
                    navigateToTab(BottomNavItem.My)
                },
                onNavigateToTicketRegister = {
                    requestedMainRoute = AppRoute.TicketRegister.route
                    navigateToTab(BottomNavItem.My)
                }
            )

            todayBattleGraph(navController = rootNavController)

            battleGraph(
                navController = rootNavController,
                onNavigateToExplore = { navigateToTab(BottomNavItem.Explore) }
            )

            alarmGraph(navController = rootNavController)

            myGraph(
                navController = rootNavController,
                onNavigateToExplore = { navigateToTab(BottomNavItem.Explore) }
            )
        }
    }

    if (showTermsSheet) {
        TermsOfServiceBottomSheet(
            onConfirm = {
                splashViewModel.markTermsAgreed()
                analyticsTracker.trackOnboardingStep(OnboardingStep.TERMS_AGREED)
                showTermsSheet = false
            }
        )
    }

    if (showNotificationSheet) {
        NotificationPermissionBottomSheet(
            onDismiss = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
            },
            onAgree = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                // TODO: FCM 토큰 발급 API 연동
            },
            onDisagree = {
                showNotificationSheet = false
                markNotificationPermissionAsked()
            }
        )
    }
}

private fun NavController.navigateToTab(
    item: BottomNavItem,
    onMainTabRequest: (BottomNavItem) -> Unit
) {
    when (item) {
        BottomNavItem.Home, BottomNavItem.Explore, BottomNavItem.My -> {
            onMainTabRequest(item)
            if (currentDestination?.route != AppRoute.Main.route && !popBackStack(
                    AppRoute.Main.route,
                    inclusive = false
                )
            ) {
                navigate(AppRoute.Main.route) { popUpTo(0) }
            }
        }

        BottomNavItem.TodayBattle -> navigate(item.route) { launchSingleTop = true }

        BottomNavItem.Class -> {
            if (!popBackStack(item.route, inclusive = false)) {
                navigate(item.route) { launchSingleTop = true }
            }
        }
    }
}