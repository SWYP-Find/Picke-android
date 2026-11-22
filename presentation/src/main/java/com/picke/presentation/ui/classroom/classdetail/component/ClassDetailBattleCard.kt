package com.picke.presentation.ui.classroom.classdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.model.ClassBattleUiModel
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassDetailBattleCardDimens {
    val imageWidth = 76.dp
    val imageMinHeight = 105.dp
}

@Composable
fun ClassDetailBattleCard(
    battle: ClassBattleUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.colorSurfaceWhite)
            .clickable(onClick = onClick)
    ) {
        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = SpacingTokens.s16, vertical = SpacingTokens.s12),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
        ) {
            AsyncImage(
                model = battle.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(ClassDetailBattleCardDimens.imageWidth)
                    .heightIn(min = ClassDetailBattleCardDimens.imageMinHeight)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(RadiusTokens.default))
                    .background(PickeTheme.colors.colorElementBeige),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s24)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (battle.tag.isNotEmpty()) {
                            PickeBadge(
                                text = battle.tag,
                                horizontalPadding = SpacingTokens.s4
                            )
                        }
                        Text(
                            text = battle.title,
                            modifier = Modifier.weight(1f),
                            color = PickeTheme.colors.colorTextBasic,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = PickeTheme.typography.bodySmSemiBold
                        )
                    }
                    Text(
                        text = battle.description,
                        modifier = Modifier.padding(horizontal = SpacingTokens.s2),
                        color = PickeTheme.colors.textMuted,
                        style = PickeTheme.typography.bodyXsRegular
                    )
                }

                battle.durationMinutes?.let { minutes ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            SpacingTokens.s2,
                            Alignment.End
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(ComponentNumberTokens.iconSm),
                            tint = PickeTheme.colors.textMuted
                        )
                        Text(
                            text = stringResource(R.string.class_battle_duration, minutes),
                            color = PickeTheme.colors.textMuted,
                            style = PickeTheme.typography.captionLgMedium
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassDetailBattleCardPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(vertical = SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassDetailBattleCard(
                battle = DummyData.dummyClassBattles.first(),
                onClick = {}
            )
            ClassDetailBattleCard(
                battle = DummyData.dummyClassAiQuestions.first(),
                onClick = {}
            )
        }
    }
}