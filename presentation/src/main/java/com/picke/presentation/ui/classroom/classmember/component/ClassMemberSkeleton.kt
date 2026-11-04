package com.picke.presentation.ui.classroom.classmember.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.component.SkeletonLine
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassMemberSkeletonDimens {
    val profileImageSize = 36.dp
    val nameWidth = 96.dp
    val nameHeight = 16.dp
}

@Composable
fun ClassMemberSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpacingTokens.s16),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .size(ClassMemberSkeletonDimens.profileImageSize)
                    .clip(CircleShape)
                    .shimmer()
            )
            SkeletonLine(width = ClassMemberSkeletonDimens.nameWidth, height = ClassMemberSkeletonDimens.nameHeight)
        }

        HorizontalDivider(
            thickness = ComponentNumberTokens.borderWidthRegular,
            color = PickeTheme.colors.borderBeigeDefault
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMemberSkeletonPreview() {
    PickeTheme {
        Column(modifier = Modifier.padding(horizontal = SpacingTokens.s16)) {
            ClassMemberSkeleton()
            ClassMemberSkeleton()
        }
    }
}