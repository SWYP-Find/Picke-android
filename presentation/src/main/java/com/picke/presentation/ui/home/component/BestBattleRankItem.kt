package com.picke.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object BestBattleRankItemDimens {
    val rankWidth = 16.dp
}

@Composable
fun BestBattleRankItem(
    item: HomeContentUiModel,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val versusText = stringResource(
        R.string.home_best_battle_versus,
        item.leftProfileName ?: stringResource(R.string.home_best_battle_left_default),
        item.rightProfileName ?: stringResource(R.string.home_best_battle_right_default)
    )

    val rankColor = if (rank == 1) PickeTheme.colors.primary500 else PickeTheme.colors.gray100

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s12, vertical = SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rank.toString(),
                modifier = Modifier.width(BestBattleRankItemDimens.rankWidth),
                color = rankColor,
                textAlign = TextAlign.Center,
                style = PickeTheme.typography.headingXl
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)) {
                    PickeBadge(text = versusText)
                    Text(
                        text = item.title,
                        color = PickeTheme.colors.cardBaseTextTitle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = PickeTheme.typography.headingXs
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                        item.tags.forEach { tag ->
                            Text(
                                text = stringResource(R.string.home_best_battle_tag, tag),
                                color = PickeTheme.colors.cardBaseTextDecription,
                                style = PickeTheme.typography.bodyXxsMedium
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clock),
                                contentDescription = null,
                                modifier = Modifier.size(ComponentNumberTokens.iconSm),
                                tint = PickeTheme.colors.cardBaseTextDecription
                            )
                            Text(
                                text = item.timeInfoText,
                                color = PickeTheme.colors.cardBaseTextDecription,
                                style = PickeTheme.typography.bodyXxsMedium
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s2),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_eye),
                                contentDescription = null,
                                modifier = Modifier.size(ComponentNumberTokens.iconSm),
                                tint = PickeTheme.colors.cardBaseTextDecription
                            )
                            Text(
                                text = item.viewCountText,
                                color = PickeTheme.colors.cardBaseTextDecription,
                                style = PickeTheme.typography.bodyXxsMedium
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.cardBaseBorderDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BestBattleRankItemPreview() {
    PickeTheme {
        Column {
            DummyData.dummyBestBattles.forEachIndexed { index, item ->
                BestBattleRankItem(
                    item = item,
                    rank = index + 1,
                    onClick = { }
                )
            }
        }
    }
}