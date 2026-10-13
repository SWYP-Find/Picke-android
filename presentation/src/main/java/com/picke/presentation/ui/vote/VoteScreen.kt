package com.picke.presentation.ui.vote

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.CustomTopAppBarAction
import com.picke.presentation.ui.component.dialog.CustomSingleActionDialog
import com.picke.presentation.ui.component.dialog.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.ui.vote.component.VoteCommentCountBadge
import com.picke.presentation.ui.vote.component.VoteOptionCard
import com.picke.presentation.ui.vote.component.VoteSkeleton
import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import com.picke.presentation.ui.vote.model.VoteClassInfoUiModel
import com.picke.presentation.ui.vote.model.voteScreenStyle
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.captureBitmap
import com.picke.presentation.util.launchBitmapShare
import com.picke.presentation.util.loadBitmapFromUrl
import com.picke.presentation.util.shareBattleToInstagramStoryBrightMode
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao

@Composable
fun VoteRoute(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler { onBackClick() }

    if (uiState.isLoading) {
        VoteSkeleton(voteType = voteType, modifier = Modifier.fillMaxSize())
    } else {
        val detail = uiState.battleDetail
        if (detail != null) {
            VoteScreen(
                voteType = voteType,
                battleDetail = detail,
                uiState = uiState,
                onBackClick = onBackClick,
                onVoteSubmit = onVoteSubmit,
                onNavigateToExplore = onNavigateToExplore,
                onSubmitVote = { selectedOptionId, onSuccess ->
                    viewModel.submitVote(
                        voteType = voteType,
                        selectedOptionId = selectedOptionId,
                        onSuccess = onSuccess
                    )
                },
                onTrackShare = { channel ->
                    viewModel.trackShare(voteType, channel)
                },
                onGetShareLink = { battleId, onSuccess, onError ->
                    viewModel.getShareLink(
                        battleId = battleId,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onDismissPointDialog = {
                    viewModel.dismissPointDialog()
                }
            )
        } else {
            BattleNotFoundScreen(onBackClick = onBackClick)
        }
    }
}

@Composable
fun VoteScreen(
    voteType: VoteType,
    battleDetail: BattleDetailUiModel,
    uiState: VoteUiState,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onSubmitVote: (String, () -> Unit) -> Unit,
    onTrackShare: (String) -> Unit,
    onGetShareLink: (Int, (String) -> Unit, (String) -> Unit) -> Unit,
    onDismissPointDialog: () -> Unit,
    classInfo: VoteClassInfoUiModel? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }
    var selectedOptionId by remember { mutableStateOf<String?>(null) }

    val isPreVote = voteType == VoteType.PRE
    val style = voteScreenStyle(voteType = voteType, classInfo = classInfo)
    val battleInfo = battleDetail.battleInfo

    val backgroundColor =
        if (selectedOptionId != null) PickeTheme.colors.primary500 else PickeTheme.colors.primary300

    val onKakaoShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { loadBitmapFromUrl(context, battleInfo.thumbnailUrl) },
            onBitmapReady = { bitmap ->
                shareBattleToKakao(
                    context = context,
                    bitmap = bitmap,
                    battleId = battleInfo.battleId,
                    battleTitle = battleInfo.title,
                    battleDescription = if (isPreVote) battleInfo.summary else battleDetail.description,
                    onComplete = { isSharing = false },
                    onSuccess = { onTrackShare(ShareChannel.KAKAO) }
                )
            },
            onBitmapMissing = {
                isSharing = false
                Toast.makeText(context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
            },
            onError = {
                isSharing = false
                Toast.makeText(context, "공유 실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { graphicsLayer.captureBitmap() },
            onBitmapReady = { bitmap ->
                if (isPreVote) {
                    shareBattleToInstagramStoryBrightMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShare(ShareChannel.INSTAGRAM) }
                    )
                } else {
                    shareBattleToInstagramStoryDarkMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShare(ShareChannel.INSTAGRAM) }
                    )
                }
            },
            onError = {
                isSharing = false
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = style.containerColor,
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Box(modifier = Modifier.statusBarsPadding()) {
                    CustomTopAppBar(
                        title = style.topBarTitle,
                        centerTitle = style.topBarTitle != null,
                        onBackClick = onBackClick,
                        backgroundColor = Color.Transparent,
                        backIconColor = PickeTheme.colors.textInverse,
                        titleColor = PickeTheme.colors.textInverse,
                        actions = {
                            if (style.isShareVisible) {
                                CustomTopAppBarAction(
                                    iconRes = R.drawable.ic_share,
                                    contentDescription = stringResource(R.string.top_app_bar_share),
                                    onClick = { showShareDialog = true },
                                    iconSize = 20.dp,
                                    tint = PickeTheme.colors.textInverse
                                )
                            }
                        }
                    )
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomButton(
                        text = stringResource(style.submitTextRes),
                        onClick = {
                            if (selectedOptionId != null) {
                                onSubmitVote(selectedOptionId!!) {
                                    /*val props = JSONObject().apply {
                                        put("battle_id", battleInfo.battleId.toString())
                                        put("battle_title", battleInfo.title)
                                    }

                                    if (isPreVote) {
                                        PickeApplication.mixpanel.track("pre_vote", props) // 기획서 명칭 일치
                                    } else {
                                        PickeApplication.mixpanel.track("post_vote", props) // 기획서 명칭 일치
                                    }*/
                                    onVoteSubmit(battleInfo.battleId)
                                }
                            }
                        },
                        backgroundColor = backgroundColor,
                        textColor = PickeTheme.colors.surfaceBeigeDefault
                    )
                    if (style.deadlineText != null) {
                        VoteDeadlineRow(deadlineText = style.deadlineText)
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(style.containerColor)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    SubcomposeAsyncImage(
                        model = battleInfo.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.3f),
                                                Color.Transparent,
                                                style.containerColor
                                            ),
                                            startY = 0f,
                                            endY = size.height
                                        )
                                    )
                                }
                            },
                        contentScale = ContentScale.Crop,
                        loading = {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(
                                        baseColor = if (isPreVote) null else PickeTheme.colors.gray600,
                                        highlightColor = if (isPreVote) null else PickeTheme.colors.gray400
                                    )
                            )
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                battleInfo.tags.forEach { tag ->
                                    Surface(
                                        color = Color.White.copy(alpha = 0.8f),
                                        shape = RoundedCornerShape(2.dp)
                                    ) {
                                        Text(
                                            text = "#${tag.name}",
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 2.dp
                                            ),
                                            style = PickeTheme.typography.captionLgMedium,
                                            color = PickeTheme.colors.primary500
                                        )
                                    }
                                }
                            }
                            if (style.commentCount != null) {
                                VoteCommentCountBadge(count = style.commentCount)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = battleInfo.title.replace(", ", ",\n"),
                            style = PickeTheme.typography.displayMd,
                            color = style.titleColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = battleDetail.description,
                            style = PickeTheme.typography.bodySmRegular,
                            color = style.descriptionColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (battleInfo.options.size >= 2) {
                            VoteOptionCard(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxHeight(),
                                option = battleInfo.options[0],
                                isSelected = selectedOptionId == battleInfo.options[0].optionId,
                                onClick = { selectedOptionId = battleInfo.options[0].optionId },
                                isImageVisible = style.isOptionImageVisible
                            )
                            VoteOptionCard(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxHeight(),
                                option = battleInfo.options[1],
                                isSelected = selectedOptionId == battleInfo.options[1].optionId,
                                onClick = { selectedOptionId = battleInfo.options[1].optionId },
                                isImageVisible = style.isOptionImageVisible
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = Color(0xFFF2E3C6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "VS",
                                style = PickeTheme.typography.bodySmMedium,
                                color = PickeTheme.colors.textDefault
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showShareDialog) {
            ShareDialog(
                onKakaoClick = {
                    showShareDialog = false
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onInstaShareClick()
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    onGetShareLink(
                        battleInfo.battleId.toInt(),
                        { url ->
                            clipboardManager.setText(AnnotatedString(url))
                            Toast.makeText(context, "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                            onTrackShare(ShareChannel.LINK)
                        },
                        { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = { showShareDialog = false }
            )
        }

        if (isSharing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PickeTheme.colors.primary900)
            }
        }

        if (uiState.isInsufficientPoints) {
            CustomSingleActionDialog(
                message = "컨텐츠를 시청하기 위한\n포인트가 부족해요!",
                subMessage = "매일 출석체크만 해도 5P를 받을 수 있어요!",
                buttonText = "배틀 주제 구경하러 가기",
                onDismiss = { onDismissPointDialog() },
                onConfirm = {
                    onDismissPointDialog()
                    onNavigateToExplore()
                }
            )
        }
    }
}

@Composable
private fun VoteDeadlineRow(
    deadlineText: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = PickeTheme.colors.cardBaseTextDecription
        )
        Text(
            text = deadlineText,
            color = PickeTheme.colors.cardBaseTextDecription,
            style = PickeTheme.typography.bodyXsRegular
        )
    }
}

@Composable
private fun BattleNotFoundScreen(onBackClick: () -> Unit) {
    Scaffold(
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBeige
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "빈 화면 로고",
                modifier = Modifier.size(width = 160.dp, height = 120.dp),
                tint = PickeTheme.colors.borderBeigeDefault
            )
            Text(
                text = "해당 배틀은 존재하지 않습니다",
                style = PickeTheme.typography.bodySmRegular,
                color = PickeTheme.colors.beige800
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteScreenPreview() {
    PickeTheme {
        VoteScreen(
            voteType = VoteType.PRE,
            battleDetail = DummyData.dummyBattleDetailList.first(),
            uiState = VoteUiState(),
            onBackClick = {},
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { _, _ -> },
            onTrackShare = { },
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {}
        )
    }
}

@Preview(name = "AI 배틀 사전 투표", showBackground = true)
@Composable
private fun VoteScreenClassPreview() {
    val classItem = DummyData.dummyMyClasses[1]
    val battle = DummyData.dummyClassAiQuestions.first()

    PickeTheme {
        VoteScreen(
            voteType = VoteType.PRE,
            battleDetail = DummyData.dummyClassAiBattleDetail,
            uiState = VoteUiState(),
            onBackClick = {},
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { _, _ -> },
            onTrackShare = { },
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {},
            classInfo = VoteClassInfoUiModel(
                className = classItem.title,
                commentCount = battle.commentCount,
                deadlineText = "2026. 10. 11. 일요일 23:59까지",
                isAiBattle = battle.isAiQuestion
            )
        )
    }
}