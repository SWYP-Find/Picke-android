package com.picke.presentation.ui.classroom.classshare

import android.content.ClipData
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiAction
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiEvent
import com.picke.presentation.ui.classroom.classshare.model.ClassShareUiState
import com.picke.presentation.ui.classroom.component.ClassBattleCard
import com.picke.presentation.ui.classroom.component.ClassBottomButton
import com.picke.presentation.ui.classroom.component.ClassFormField
import com.picke.presentation.ui.classroom.component.rememberClassDeadlineText
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData
import java.time.LocalDateTime

private const val CLIP_LABEL = "class_code"

@Composable
fun ClassShareScreen(
    onBackClick: () -> Unit,
    onShareCodeClick: (code: String) -> Unit,
    onGoToClassClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ClassShareViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboard = LocalClipboard.current

    val codeCopiedMessage = stringResource(R.string.class_share_code_copied)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ClassShareUiEvent.NavigateBack -> onBackClick()
                is ClassShareUiEvent.ShareCode -> onShareCodeClick(event.code)
                is ClassShareUiEvent.CopyCode -> {
                    clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(CLIP_LABEL, event.code)))
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                        Toast.makeText(context, codeCopiedMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                is ClassShareUiEvent.NavigateToClass -> onGoToClassClick(event.classId)
            }
        }
    }

    ClassShareContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun ClassShareContent(
    uiState: ClassShareUiState,
    onAction: (ClassShareUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .background(PickeTheme.colors.backgroundBeige)
            .systemBarsPadding(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.class_share_title),
                onBackClick = { onAction(ClassShareUiAction.BackClick) },
                backgroundColor = PickeTheme.colors.backgroundBeige
            )
        },
        bottomBar = {
            ClassBottomButton(
                text = stringResource(R.string.class_share_go_to_class),
                onClick = { onAction(ClassShareUiAction.GoToClassClick) }
            )
        },
        containerColor = PickeTheme.colors.backgroundBeige,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s32)
        ) {
            ClassShareHeaderSection()

            ClassShareCodeSection(
                uiState = uiState,
                onShareCodeClick = { onAction(ClassShareUiAction.ShareCodeClick) },
                onCopyCodeClick = { onAction(ClassShareUiAction.CopyCodeClick) }
            )

            uiState.battle?.let { battle ->
                ClassShareBattleSection(battle = battle)
            }
        }
    }
}

@Composable
private fun ClassShareHeaderSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.class_share_headline),
            color = PickeTheme.colors.textDefault,
            style = PickeTheme.typography.headingXl
        )
        Text(
            text = stringResource(R.string.class_share_description),
            color = PickeTheme.colors.textMuted,
            style = PickeTheme.typography.bodyMdMedium
        )
    }
}

@Composable
private fun ClassShareCodeSection(
    uiState: ClassShareUiState,
    onShareCodeClick: () -> Unit,
    onCopyCodeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val deadlineText = rememberClassDeadlineText(uiState.deadline)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.surfaceBeigeDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.borderBeigeDefault,
                shape
            )
            .padding(SpacingTokens.s24),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.className,
                color = PickeTheme.colors.textDefault,
                style = PickeTheme.typography.headingMd
            )
            Text(
                text = stringResource(R.string.class_share_deadline, deadlineText),
                modifier = Modifier.alpha(if (uiState.hasDeadline) 1f else 0f),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodySmMedium
            )
            HorizontalDivider(
                modifier = Modifier.padding(top = SpacingTokens.s8),
                thickness = ComponentNumberTokens.borderWidthRegular,
                color = PickeTheme.colors.cardBaseBorderDefault
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.class_join_code_label),
                color = PickeTheme.colors.textMuted,
                style = PickeTheme.typography.bodySmSemiBold
            )
            Text(
                text = uiState.code,
                color = PickeTheme.colors.textPrimary,
                style = PickeTheme.typography.displayMd
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(ComponentNumberTokens.buttonPrimaryLargeHeight)
                    .clip(shape)
                    .background(PickeTheme.colors.buttonSecondaryBackgroundDefault)
                    .clickable(role = Role.Button, onClick = onShareCodeClick),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.class_share_code_action),
                    color = PickeTheme.colors.buttonSecondaryTextDefault,
                    style = PickeTheme.typography.bodyLgSemiBold
                )
            }
            Box(
                modifier = Modifier
                    .size(ComponentNumberTokens.buttonPrimaryLargeHeight)
                    .clip(shape)
                    .background(PickeTheme.colors.cardBaseBackgroundDefault)
                    .border(
                        ComponentNumberTokens.borderWidthRegular,
                        PickeTheme.colors.primary100,
                        shape
                    )
                    .clickable(role = Role.Button, onClick = onCopyCodeClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = stringResource(R.string.class_share_copy_code),
                    modifier = Modifier.size(ComponentNumberTokens.iconMd),
                    tint = PickeTheme.colors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun ClassShareBattleSection(
    battle: ClassBattleUiModel,
    modifier: Modifier = Modifier
) {
    ClassFormField(
        title = stringResource(R.string.class_setting_selected_battle),
        modifier = modifier
    ) {
        ClassBattleCard(battle = battle)
    }
}

@Preview(name = "마감일 있음", showBackground = true)
@Composable
private fun ClassShareContentPreview() {
    PickeTheme {
        ClassShareContent(
            uiState = ClassShareUiState(
                classId = 1L,
                className = "2학년 3반 1학기 토론",
                deadline = LocalDateTime.of(2026, 9, 23, 18, 0),
                code = "PK7M2Q",
                battle = DummyData.dummyClassBattles[1]
            ),
            onAction = {}
        )
    }
}

@Preview(name = "마감일 없음", showBackground = true)
@Composable
private fun ClassShareContentNoDeadlinePreview() {
    PickeTheme {
        ClassShareContent(
            uiState = ClassShareUiState(
                classId = 1L,
                className = "2학년 3반 1학기 토론",
                code = "PK7M2Q",
                battle = DummyData.dummyClassBattles[1]
            ),
            onAction = {}
        )
    }
}