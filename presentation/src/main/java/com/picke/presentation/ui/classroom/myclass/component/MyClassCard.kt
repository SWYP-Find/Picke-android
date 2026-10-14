package com.picke.presentation.ui.classroom.myclass.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.component.ClassStatusBadge
import com.picke.presentation.ui.classroom.myclass.model.MyClassUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object MyClassCardDimens {
    val headerHeight = 22.dp
    val metaDotSize = 2.dp
}

@Composable
fun MyClassCard(
    item: MyClassUiModel,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(PickeTheme.colors.surfaceBeigeDefault)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.borderBeigeDefault, shape)
            .clickable(onClick = onClick)
            .padding(SpacingTokens.s12),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MyClassCardDimens.headerHeight),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                ClassStatusBadge(isInProgress = item.isInProgress)
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = stringResource(R.string.more),
                    modifier = Modifier
                        .size(ComponentNumberTokens.iconLg)
                        .clip(CircleShape)
                        .clickable(onClick = onMoreClick),
                    tint = PickeTheme.colors.textDefault
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Text(
                    text = item.title,
                    color = PickeTheme.colors.textSubtler,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PickeTheme.typography.headingSm
                )
                Text(
                    text = item.topic,
                    color = PickeTheme.colors.textMuted,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = PickeTheme.typography.bodyXsRegular
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)) {
            HorizontalDivider(thickness = ComponentNumberTokens.borderWidthRegular, color = PickeTheme.colors.borderBeigeDefault)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyClassMetaItem(iconResId = R.drawable.ic_class_member, text = stringResource(R.string.my_class_member_count, item.memberCount))
                    Box(
                        modifier = Modifier
                            .size(MyClassCardDimens.metaDotSize)
                            .background(PickeTheme.colors.textMuted, CircleShape)
                    )
                    MyClassMetaItem(iconResId = R.drawable.ic_class_deadline, text = item.deadlineText)
                }

                Row(
                    modifier = Modifier.padding(start = SpacingTokens.s8),
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.my_class_view),
                        color = PickeTheme.colors.textPrimary,
                        style = PickeTheme.typography.captionLgBold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right_a),
                        contentDescription = null,
                        modifier = Modifier.size(ComponentNumberTokens.iconXs),
                        tint = PickeTheme.colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun MyClassMetaItem(
    @DrawableRes iconResId: Int,
    text: String,
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
            modifier = Modifier.size(ComponentNumberTokens.iconXs),
            tint = Color.Unspecified
        )
        Text(
            text = text,
            color = PickeTheme.colors.textMuted,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = PickeTheme.typography.captionLgRegular
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyClassCardPreview() {
    PickeTheme {
        Column(
            modifier = Modifier.padding(SpacingTokens.s16),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s12)
        ) {
            MyClassCard(
                item = DummyData.dummyMyClasses[0],
                onClick = {},
                onMoreClick = {}
            )
            MyClassCard(
                item = DummyData.dummyMyClasses[2],
                onClick = {},
                onMoreClick = {}
            )
        }
    }
}