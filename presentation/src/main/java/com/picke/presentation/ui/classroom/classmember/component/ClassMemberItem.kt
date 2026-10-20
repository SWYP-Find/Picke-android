package com.picke.presentation.ui.classroom.classmember.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classmember.model.ClassMemberUiModel
import com.picke.presentation.ui.component.PickeBadge
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens
import com.picke.presentation.util.DummyData

private object ClassMemberItemDimens {
    val profileImageSize = 36.dp
}

@Composable
fun ClassMemberItem(
    member: ClassMemberUiModel,
    canRemove: Boolean,
    onEditNameClick: () -> Unit,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badgeText = when {
        member.isOperator -> stringResource(R.string.class_member_operator)
        member.isMe -> stringResource(R.string.class_member_me)
        else -> null
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SpacingTokens.s16),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            model = member.imageUrl,
            modifier = Modifier.size(ClassMemberItemDimens.profileImageSize)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s6),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = member.name,
                modifier = Modifier.weight(1f, fill = false),
                color = PickeTheme.colors.cardBaseTextTitle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = PickeTheme.typography.bodyLgMedium
            )
            if (member.isMe) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pencil),
                    contentDescription = stringResource(R.string.class_name_sheet_edit),
                    modifier = Modifier
                        .size(ComponentNumberTokens.iconSm)
                        .clickable(role = Role.Button, onClick = onEditNameClick),
                    tint = PickeTheme.colors.textDefault
                )
            }
        }

        when {
            badgeText != null -> PickeBadge(text = badgeText)
            canRemove -> {
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.size(ComponentNumberTokens.iconLg)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus_circle),
                        contentDescription = stringResource(R.string.class_member_remove),
                        modifier = Modifier.size(ComponentNumberTokens.iconMd),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassMemberItemPreview() {
    val members = DummyData.dummyClassMembers

    PickeTheme {
        Column(modifier = Modifier.padding(horizontal = SpacingTokens.s16)) {
            ClassMemberItem(
                member = members[0],
                canRemove = false,
                onEditNameClick = {},
                onRemoveClick = {}
            )
            ClassMemberItem(
                member = members[1].copy(isMe = true),
                canRemove = false,
                onEditNameClick = {},
                onRemoveClick = {}
            )
            ClassMemberItem(
                member = members[2],
                canRemove = true,
                onEditNameClick = {},
                onRemoveClick = {}
            )
        }
    }
}