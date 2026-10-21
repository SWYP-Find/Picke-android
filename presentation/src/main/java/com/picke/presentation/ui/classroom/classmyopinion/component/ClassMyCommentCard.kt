package com.picke.presentation.ui.classroom.classmyopinion.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classmyopinion.model.ClassMyCommentUiModel
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.PickeIconCount
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassMyCommentCardDimens {
    val countGroupSpacing = 11.dp
}

@Composable
fun ClassMyCommentCard(
    comment: ClassMyCommentUiModel,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PickeTheme.colors.cardBaseBackgroundDefault, shape)
            .border(ComponentNumberTokens.borderWidthRegular, PickeTheme.colors.cardBaseBorderDefault, shape)
            .padding(SpacingTokens.s16),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s8)
    ) {
        PickeBadge(text = comment.stance)
        Text(
            text = comment.content,
            modifier = Modifier.padding(horizontal = SpacingTokens.s2),
            color = PickeTheme.colors.textBody,
            style = PickeTheme.typography.bodyXsRegular
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(ClassMyCommentCardDimens.countGroupSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PickeIconCount(
                iconRes = R.drawable.ic_heart_plus,
                contentDescription = stringResource(R.string.class_my_opinion_like),
                count = comment.likeCount,
                contentColor = PickeTheme.colors.cardBaseTextDecription
            )
            PickeIconCount(
                iconRes = R.drawable.ic_message,
                contentDescription = stringResource(R.string.class_my_opinion_reply),
                count = comment.replyCount,
                contentColor = PickeTheme.colors.cardBaseTextDecription
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMyCommentCardPreview() {
    PickeTheme {
        ClassMyCommentCard(comment = DummyData.dummyClassMyComments.first())
    }
}