package com.picke.presentation.ui.classroom.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.model.ClassBattleOptionUiModel
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassBattleCardDimens {
    val checkboxSize = 20.dp
    val versusSize = 24.dp
    val headerMetaGap = 10.dp
}

@Composable
fun ClassBattleCard(
    battle: ClassBattleUiModel,
    modifier: Modifier = Modifier,
    isSelected: Boolean? = null,
    onClick: (() -> Unit)? = null,
    onPreviewClick: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(RadiusTokens.default)
    val borderColor = if (isSelected == true) {
        PickeTheme.colors.borderPrimaryDefault
    } else {
        PickeTheme.colors.cardBaseBorderDefault
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(ComponentNumberTokens.borderWidthRegular, borderColor, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            ClassBattleCardHeader(
                battle = battle,
                isSelected = isSelected,
                onPreviewClick = onPreviewClick
            )

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = battle.title,
                    color = PickeTheme.colors.cardBaseTextTitle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PickeTheme.typography.headingXs
                )
                Text(
                    text = battle.description,
                    color = PickeTheme.colors.gray200,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PickeTheme.typography.bodyXxsMedium
                )
            }
        }

        if (battle.options.size == 2) {
            ClassBattleOptionRow(
                options = battle.options,
                showsAvatar = !battle.isAiQuestion
            )
        }
    }
}

@Composable
private fun ClassBattleCardHeader(
    battle: ClassBattleUiModel,
    isSelected: Boolean?,
    onPreviewClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = battle.tag,
            modifier = Modifier
                .background(
                    PickeTheme.colors.badgeFilledBackground,
                    RoundedCornerShape(RadiusTokens.default)
                )
                .padding(
                    horizontal = SpacingTokens.s6,
                    vertical = SpacingTokens.s2
                ),
            color = PickeTheme.colors.badgeFilledText,
            style = PickeTheme.typography.bodyXxsSemiBold
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                ClassBattleCardDimens.headerMetaGap,
                Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            battle.durationMinutes?.let { minutes ->
                ClassBattleMeta(
                    iconResId = R.drawable.ic_clock,
                    text = stringResource(R.string.class_battle_duration, minutes),
                    color = PickeTheme.colors.textMuted,
                    style = PickeTheme.typography.bodyXxsMedium
                )
            }

            if (onPreviewClick != null) {
                ClassBattleMeta(
                    iconResId = R.drawable.ic_play,
                    text = stringResource(R.string.class_battle_preview),
                    color = PickeTheme.colors.textPrimary,
                    style = PickeTheme.typography.bodyXxsSemiBold,
                    modifier = Modifier.clickable(onClick = onPreviewClick)
                )
            }

            if (isSelected != null) {
                ClassCheckbox(
                    isChecked = isSelected,
                    size = ClassBattleCardDimens.checkboxSize
                )
            }
        }
    }
}

@Composable
private fun ClassBattleMeta(
    @DrawableRes iconResId: Int,
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(ComponentNumberTokens.iconSm),
            tint = color
        )
        Text(
            text = text,
            color = color,
            style = style
        )
    }
}

@Composable
private fun ClassBattleOptionRow(
    options: List<ClassBattleOptionUiModel>,
    showsAvatar: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ClassBattleOptionBox(
            option = options[0],
            showsAvatar = showsAvatar,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(ClassBattleCardDimens.versusSize)
                .background(PickeTheme.colors.secondary200, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.class_battle_versus),
                color = PickeTheme.colors.gray900,
                style = PickeTheme.typography.captionSmBold
            )
        }

        ClassBattleOptionBox(
            option = options[1],
            showsAvatar = showsAvatar,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ClassBattleOptionBox(
    option: ClassBattleOptionUiModel,
    showsAvatar: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Row(
        modifier = modifier
            .background(PickeTheme.colors.cardOpinionBackgroundDefault, shape)
            .border(
                ComponentNumberTokens.borderWidthRegular,
                PickeTheme.colors.cardBaseBorderDefault,
                shape
            )
            .padding(SpacingTokens.s8),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showsAvatar) {
            ProfileImage(
                model = option.imageUrl,
                modifier = Modifier.size(ComponentNumberTokens.avatarMd)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s2)) {
            Text(
                text = option.stance,
                color = PickeTheme.colors.cardBaseTextTitle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = PickeTheme.typography.captionMdSemiBold
            )
            Text(
                text = option.subText,
                color = PickeTheme.colors.cardBaseTextDecription,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = PickeTheme.typography.captionSmRegular
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassBattleCardPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassBattleCard(
                battle = DummyData.dummyClassBattles[1],
                isSelected = true,
                onClick = {},
                onPreviewClick = {}
            )
            ClassBattleCard(
                battle = DummyData.dummyClassAiQuestions[0],
                isSelected = false,
                onClick = {}
            )
        }
    }
}