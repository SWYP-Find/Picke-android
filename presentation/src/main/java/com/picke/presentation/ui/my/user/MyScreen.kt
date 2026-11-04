package com.picke.presentation.ui.my.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ads.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.component.ProfileImageKind
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.my.user.componenet.CreditCard
import com.picke.presentation.ui.my.user.componenet.MyPageMenuItem
import com.picke.presentation.ui.my.user.componenet.MySkeleton
import com.picke.presentation.ui.my.user.model.MyPhilosopherUiModel
import com.picke.presentation.ui.my.user.model.MyUiEvent
import com.picke.presentation.ui.my.user.model.MyUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

@Composable
fun MyScreen(
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToPoint: () -> Unit,
    onNavigateToTicket: () -> Unit,
    viewModel: MyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val loadFailedMessage = stringResource(R.string.toast_load_failed)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchMyInfo()
                viewModel.fetchUnreadAlarmStatus()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MyUiEvent.ShowLoadFailedToast ->
                    Toast.makeText(context, loadFailedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    MyScreen(
        uiState = uiState,
        onNavigateToAlarm = onNavigateToAlarm,
        onNavigateToSetting = onNavigateToSetting,
        onNavigateToDiscussion = onNavigateToDiscussion,
        onNavigateToPhilosopher = onNavigateToPhilosopher,
        onNavigateToContent = onNavigateToContent,
        onNavigateToNotice = onNavigateToNotice,
        onNavigateToPoint = onNavigateToPoint,
        onNavigateToTicket = onNavigateToTicket
    )
}

@Composable
fun MyScreen(
    uiState: MyUiState,
    onNavigateToAlarm: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToDiscussion: () -> Unit,
    onNavigateToPhilosopher: () -> Unit,
    onNavigateToContent: () -> Unit,
    onNavigateToNotice: () -> Unit,
    onNavigateToPoint: () -> Unit,
    onNavigateToTicket: () -> Unit
) {

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBeige,
        topBar = {
            CustomTopAppBar(
                backgroundColor = PickeTheme.colors.backgroundBeige,
                centerTitle = false,
                actions = {
                    if (uiState.isLoading || uiState.isAlarmStatusLoading) {
                        repeat(2) {
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .shimmer()
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = onNavigateToAlarm, modifier = Modifier.size(36.dp)) {
                            BadgedBox(
                                badge = {
                                    if (uiState.hasNewNotice) {
                                        Badge(
                                            containerColor = PickeTheme.colors.primary500,
                                            modifier = Modifier.offset(x = 4.dp, y = (-4).dp)
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_alarm),
                                    contentDescription = stringResource(R.string.alarm),
                                    tint = PickeTheme.colors.textDefault
                                )
                            }
                        }
                        IconButton(
                            onClick = onNavigateToSetting,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_setting),
                                contentDescription = stringResource(R.string.setting),
                                tint = PickeTheme.colors.textDefault
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            MySkeleton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(horizontal = SpacingTokens.s16)) {
                    ProfileSection(
                        nickname = uiState.profile?.nickname ?: stringResource(R.string.my_nickname_default),
                        userHandle = uiState.profile?.userTag?.let { "@$it" } ?: "",
                        profileImage = uiState.profile?.characterImageUrl
                    )

                    Spacer(modifier = Modifier.height(SpacingTokens.s16))
                    CreditCard(
                        credit = uiState.tier?.currentPoint ?: 0,
                        onClick = onNavigateToPoint
                    )

                    Spacer(modifier = Modifier.height(SpacingTokens.s16))
                    PhilosopherTypeSection(
                        philosopher = uiState.philosopher,
                        onClick = { onNavigateToPhilosopher() }
                    )
                }

                Spacer(modifier = Modifier.height(SpacingTokens.s16))
                MyPageMenuItem(
                    title = stringResource(R.string.my_menu_discussion),
                    onClick = { onNavigateToDiscussion() }
                )
                MyPageMenuItem(
                    title = stringResource(R.string.my_menu_content),
                    onClick = { onNavigateToContent() }
                )
                MyPageMenuItem(
                    title = stringResource(R.string.my_menu_notice),
                    onClick = { onNavigateToNotice() }
                )
                MyPageMenuItem(
                    title = stringResource(R.string.my_menu_ticket),
                    onClick = onNavigateToTicket
                )

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpacingTokens.s16),
                    contentAlignment = Alignment.Center
                ) {
                    AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_MY)
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    nickname: String,
    userHandle: String,
    profileImage: Any?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: 토큰 동기화 - color/surface/beige-darker
        ProfileImage(
            model = profileImage,
            size = 52.dp,
            backgroundColor = PickeTheme.colors.avatarBackround
        )

        Spacer(modifier = Modifier.width(SpacingTokens.s12))
        Column {
            // TODO: 토큰 동기화 - color/text/basic
            Text(
                text = nickname,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.headingSm
            )

            Spacer(modifier = Modifier.height(SpacingTokens.s2))
            Text(
                text = userHandle,
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodyXsRegular
            )
        }
    }
}

@Composable
fun PhilosopherTypeSection(
    philosopher: MyPhilosopherUiModel?,
    onClick: () -> Unit
) {
    val lockedName = stringResource(R.string.my_philosopher_locked)

    val isLocked = philosopher == null || philosopher.philosopherType == "UNKNOWN"
    val displayImage = if (isLocked) R.drawable.img_lock else philosopher?.imageUrl
    val displayName = if (isLocked) lockedName else philosopher?.philosopherLabel ?: ""
    val shape = RoundedCornerShape(RadiusTokens.default)

    // TODO: 토큰 동기화 - color/surface/beige-dark
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(PickeTheme.colors.surfaceBeigeStrong)
            .border(
                width = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.borderBeigeDefault,
                shape = shape
            )
            .clickable { onClick() }
            .padding(SpacingTokens.s16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            model = displayImage,
            size = ComponentNumberTokens.avatarMd,
            kind = if (isLocked) ProfileImageKind.LOCKED else ProfileImageKind.PHILOSOPHER
        )

        Spacer(modifier = Modifier.width(SpacingTokens.s12))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.my_menu_philosopher),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.captionMdMedium
            )

            Spacer(modifier = Modifier.height(SpacingTokens.s4))
            // TODO: 토큰 동기화 - color/text/basic
            Text(
                text = if (isLocked) displayName else "$displayName ",
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.bodySmSemiBold
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right_a),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.textDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    PickeTheme {
        MyScreen(
            uiState = MyUiState(),
            onNavigateToAlarm = { },
            onNavigateToSetting = { },
            onNavigateToDiscussion = { },
            onNavigateToPhilosopher = { },
            onNavigateToContent = { },
            onNavigateToNotice = { },
            onNavigateToPoint = { },
            onNavigateToTicket = { }
        )
    }
}