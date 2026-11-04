package com.picke.presentation.ui.todaybattle

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.CustomTopAppBarAction
import com.picke.presentation.ui.component.dialog.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.ui.todaybattle.component.OpinionCard
import com.picke.presentation.ui.todaybattle.component.TodayBattleSkeleton
import com.picke.presentation.ui.todaybattle.component.TopIndicatorBar
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiModel
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiState
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.captureBitmap
import com.picke.presentation.util.launchBitmapShare
import com.picke.presentation.util.loadBitmapFromUrl
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao
import kotlinx.coroutines.launch

private const val CLIP_LABEL = "battle_link"

private object TodayBattleScreenDimens {
    val tagMinHeight = 20.dp
    val timerHorizontalPadding = 13.dp
    val timerVerticalPadding = 7.dp
    val vsSize = 40.dp
}

@Composable
fun TodayBattleScreen(
    viewModel: TodayBattleViewModel = hiltViewModel(),
    initialBattleId: String? = null,
    onBackClick: () -> Unit,
    onNavigateToScenario: (String) -> Unit,
    onNavigateToPerspective: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboard = LocalClipboard.current

    val linkCopiedMessage = stringResource(R.string.share_link_copied)

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    TodayBattleScreen(
        uiState = uiState,
        initialBattleId = initialBattleId,
        onBackClick = onBackClick,
        onEnterBattle = { currentBattleId, selectedOptionId ->
            viewModel.enterBattle(
                battleId = currentBattleId.toLong(),
                optionId = selectedOptionId!!.toLong(),
                onNavigateToScenario = onNavigateToScenario,
                onNavigateToPerspective = onNavigateToPerspective
            )
        },
        onGetShareLink = { currentBattleId ->
            viewModel.getShareLink(
                battleId = currentBattleId,
                onSuccess = { url ->
                    coroutineScope.launch {
                        clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(CLIP_LABEL, url)))
                        Toast.makeText(context, linkCopiedMessage, Toast.LENGTH_SHORT).show()
                        viewModel.trackShareAction(ShareTarget.BATTLE, ShareChannel.LINK)
                    }
                },
                onError = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        },
        onTrackShareAction = viewModel::trackShareAction
    )
}

@Composable
fun TodayBattleScreen(
    uiState: TodayBattleUiState,
    initialBattleId: String?,
    onBackClick: () -> Unit,
    onEnterBattle: (String, String?) -> Unit,
    onGetShareLink: (Int) -> Unit,
    onTrackShareAction: (String, String) -> Unit
) {
    val context = LocalContext.current

    val imageLoadFailedMessage = stringResource(R.string.share_image_load_failed)
    val shareFailedMessage = stringResource(R.string.share_failed)
    val captureFailedMessage = stringResource(R.string.share_capture_failed)

    val battleList = uiState.battleList

    val pagerState = rememberPagerState(pageCount = { battleList.size })
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var selectedOptionId by remember(pagerState.currentPage) { mutableStateOf<String?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    val isButtonEnabled = selectedOptionId != null && !uiState.isEntering
    val currentBattle = if (battleList.isNotEmpty()) battleList[pagerState.currentPage] else null

    val onKakaoShareClick = {
        currentBattle?.let { battle ->
            isSharing = true
            coroutineScope.launchBitmapShare(
                loadBitmap = { loadBitmapFromUrl(context, battle.imageUrl) },
                onBitmapReady = { bitmap ->
                    shareBattleToKakao(
                        context = context,
                        bitmap = bitmap,
                        battleId = battle.battleId,
                        battleTitle = battle.title,
                        battleDescription = battle.description,
                        onComplete = { isSharing = false },
                        onSuccess = { onTrackShareAction(ShareTarget.BATTLE, ShareChannel.KAKAO) }
                    )
                },
                onBitmapMissing = {
                    isSharing = false
                    Toast.makeText(context, imageLoadFailedMessage, Toast.LENGTH_SHORT).show()
                },
                onError = {
                    isSharing = false
                    Toast.makeText(context, shareFailedMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launchBitmapShare(
            loadBitmap = { graphicsLayer.captureBitmap() },
            onBitmapReady = { bitmap ->
                shareBattleToInstagramStoryDarkMode(
                    context = context,
                    bitmap = bitmap,
                    onComplete = { isSharing = false },
                    onSuccess = { onTrackShareAction(ShareTarget.BATTLE, ShareChannel.INSTAGRAM) }
                )
            },
            onError = {
                isSharing = false
                Toast.makeText(context, captureFailedMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    LaunchedEffect(battleList, initialBattleId) {
        if (initialBattleId != null && battleList.isNotEmpty()) {
            val targetPage = battleList.indexOfFirst { it.battleId == initialBattleId }
            if (targetPage >= 0) pagerState.animateScrollToPage(targetPage)
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            TodayBattleSkeleton(modifier = Modifier.fillMaxSize())

            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = Color.Transparent,
                    backIconColor = PickeTheme.colors.textInverse
                )
            }
        }

        return
    }

    if (battleList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    onBackClick = onBackClick,
                    backgroundColor = Color.Transparent,
                    backIconColor = PickeTheme.colors.textInverse
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
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
                    text = "아직 빠른 배틀이 선정되지 않았어요\n조금만 기다려주세요!",
                    style = PickeTheme.typography.bodySmRegular,
                    color = PickeTheme.colors.surfaceBeigeStrong,
                    textAlign = TextAlign.Center
                )
            }
        }

        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            bottomBar = {
                CustomButton(
                    text = stringResource(R.string.battle_start),
                    onClick = {
                        if (isButtonEnabled) {
                            val currentBattleId = battleList[pagerState.currentPage].battleId
                            onEnterBattle(currentBattleId, selectedOptionId)
                        }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(
                            start = SpacingTokens.s16,
                            top = SpacingTokens.s16,
                            end = SpacingTokens.s16,
                            bottom = SpacingTokens.s20
                        ),
                    backgroundColor = if (isButtonEnabled) PickeTheme.colors.primary500 else PickeTheme.colors.primary300,
                    textColor = PickeTheme.colors.textInverse
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(Color.Black)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    BattleContent(
                        item = battleList[page],
                        selectedOptionId = selectedOptionId,
                        onOptionSelect = { optionId -> selectedOptionId = optionId }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                ) {
                    if (battleList.size > 1) {
                        Box(
                            modifier = Modifier.padding(
                                start = SpacingTokens.s16,
                                top = SpacingTokens.s16,
                                end = SpacingTokens.s16
                            )
                        ) {
                            TopIndicatorBar(
                                currentPage = pagerState.currentPage,
                                totalPages = battleList.size
                            )
                        }
                    }

                    CustomTopAppBar(
                        onBackClick = onBackClick,
                        backgroundColor = Color.Transparent,
                        backIconColor = PickeTheme.colors.textInverse,
                        actions = {
                            CustomTopAppBarAction(
                                iconRes = R.drawable.ic_share,
                                contentDescription = stringResource(R.string.top_app_bar_share),
                                onClick = { showShareDialog = true },
                                tint = PickeTheme.colors.textInverse
                            )
                        }
                    )
                }
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
                    val currentBattleId = battleList[pagerState.currentPage].battleId.toInt()
                    onGetShareLink(currentBattleId)
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
    }
}

@Composable
fun BattleContent(
    item: TodayBattleUiModel,
    selectedOptionId: String?,
    onOptionSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                )
                            )
                        }
                    },
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer(PickeTheme.colors.gray600, PickeTheme.colors.gray400)
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = SpacingTokens.s16),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                    item.tags.forEach { tag ->
                        // TODO: 토큰 동기화 - color/element/beige
                        Surface(
                            shape = RoundedCornerShape(RadiusTokens.default),
                            color = PickeTheme.colors.beige600
                        ) {
                            Box(
                                modifier = Modifier
                                    .heightIn(min = TodayBattleScreenDimens.tagMinHeight)
                                    .padding(horizontal = SpacingTokens.s4),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#$tag",
                                    color = PickeTheme.colors.textPrimary,
                                    style = PickeTheme.typography.bodyXxsSemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(SpacingTokens.s20))
                Text(
                    text = item.title,
                    color = PickeTheme.colors.textInverse,
                    textAlign = TextAlign.Center,
                    style = PickeTheme.typography.headingXlBold
                )

                Spacer(modifier = Modifier.height(SpacingTokens.s12))
                Text(
                    text = item.description,
                    color = PickeTheme.colors.textMuted,
                    textAlign = TextAlign.Center,
                    style = PickeTheme.typography.bodySmRegular
                )

                Spacer(modifier = Modifier.height(SpacingTokens.s16))
                // TODO: 토큰 동기화 - color/border/gray-dark
                Surface(
                    shape = RoundedCornerShape(RadiusTokens.default),
                    color = Color.Transparent,
                    border = BorderStroke(
                        width = ComponentNumberTokens.borderWidthRegular,
                        color = PickeTheme.colors.gray300
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = TodayBattleScreenDimens.timerHorizontalPadding,
                            vertical = TodayBattleScreenDimens.timerVerticalPadding
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(ComponentNumberTokens.iconSm),
                            tint = PickeTheme.colors.textMuted
                        )
                        Spacer(modifier = Modifier.width(SpacingTokens.s4))
                        Text(
                            text = item.timeLeft,
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.bodyXxsSemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacingTokens.s32))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s16),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
                if (item.options.isNotEmpty()) {
                    val optionA = item.options[0]
                    OpinionCard(
                        name = optionA.name,
                        opinion = optionA.opinion,
                        quote = optionA.quote,
                        isSelected = selectedOptionId == optionA.optionId,
                        onClick = { onOptionSelect(optionA.optionId) }
                    )
                }

                if (item.options.size > 1) {
                    val optionB = item.options[1]
                    OpinionCard(
                        name = optionB.name,
                        opinion = optionB.opinion,
                        quote = optionB.quote,
                        isSelected = selectedOptionId == optionB.optionId,
                        onClick = { onOptionSelect(optionB.optionId) }
                    )
                }
            }

            Surface(
                modifier = Modifier.size(TodayBattleScreenDimens.vsSize),
                shape = CircleShape,
                color = PickeTheme.colors.secondary200
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // TODO: 토큰 동기화 - color/text/basic
                    Text(
                        text = stringResource(R.string.class_battle_versus),
                        color = PickeTheme.colors.textDefault,
                        style = PickeTheme.typography.bodySmSemiBold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(SpacingTokens.s16))
    }
}

@Preview(showBackground = true)
@Composable
fun TodayBattleScreenPreview() {
    PickeTheme {
        TodayBattleScreen(
            uiState = TodayBattleUiState(
                battleList = DummyData.dummyTodayBattles
            ),
            initialBattleId = "",
            onBackClick = { },
            onEnterBattle = { _, _ -> },
            onGetShareLink = { },
            onTrackShareAction = { _, _ -> }
        )
    }
}