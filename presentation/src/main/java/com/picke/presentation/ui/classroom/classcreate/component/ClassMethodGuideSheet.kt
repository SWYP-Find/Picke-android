package com.picke.presentation.ui.classroom.classcreate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.classroom.classcreate.model.ClassCreateMethod
import com.picke.presentation.ui.classroom.classcreate.model.ClassMethodGuideUiModel
import com.picke.presentation.ui.classroom.classcreate.model.toGuideUiModel
import com.picke.presentation.ui.classroom.component.ClassBottomSheet
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.tokens.ComponentNumberTokens
import com.picke.presentation.ui.theme.tokens.RadiusTokens
import com.picke.presentation.ui.theme.tokens.SpacingTokens

private object ClassMethodGuideSheetDimens {
    val titleLetterSpacing = (-0.6).sp
    val contentBottomSpacing = 28.dp
    val fadeHeight = 100.dp
    val bubbleWidth = 222.dp
}

@Composable
fun ClassMethodGuideSheet(
    method: ClassCreateMethod,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClassBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        ClassMethodGuideSheetContent(
            method = method,
            onConfirmClick = onDismiss
        )
    }
}

@Composable
private fun ClassMethodGuideSheetContent(
    method: ClassCreateMethod,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val guide = method.toGuideUiModel()

    Column(modifier = modifier.padding(top = SpacingTokens.s20, bottom = SpacingTokens.s16)) {
        Text(
            text = stringResource(guide.titleRes),
            color = PickeTheme.colors.textDefault,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = PickeTheme.typography.headingXl.copy(letterSpacing = ClassMethodGuideSheetDimens.titleLetterSpacing)
        )

        Column(
            modifier = Modifier.padding(top = SpacingTokens.s20),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            ClassGuideChatIllustration(guide = guide)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(guide.headlineRes),
                    color = PickeTheme.colors.textDefault,
                    textAlign = TextAlign.Center,
                    style = PickeTheme.typography.bodyLgBold
                )
                Text(
                    text = stringResource(guide.descriptionRes),
                    color = PickeTheme.colors.textBody,
                    textAlign = TextAlign.Center,
                    style = PickeTheme.typography.bodySmMedium
                )
            }
        }

        CustomButton(
            text = stringResource(R.string.class_guide_confirm),
            onClick = onConfirmClick,
            modifier = Modifier
                .padding(top = ClassMethodGuideSheetDimens.contentBottomSpacing)
                .height(ComponentNumberTokens.buttonPrimaryLargeHeight),
            backgroundColor = PickeTheme.colors.buttonPrimaryBackgroundDefault,
            textColor = PickeTheme.colors.buttonPrimaryTextDefault
        )
    }
}

@Composable
fun ClassGuideChatIllustration(
    guide: ClassMethodGuideUiModel,
    modifier: Modifier = Modifier
) {
    val nameColor =
        if (guide.isRightMe) PickeTheme.colors.textDefault else PickeTheme.colors.textSubtler
    val rightBubbleColor =
        if (guide.isRightMe) PickeTheme.colors.surfaceBeigeStrong else PickeTheme.colors.beige500
    val rightBubbleBorderColor =
        if (guide.isRightMe) PickeTheme.colors.borderBeigeDefault else PickeTheme.colors.beige700

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RadiusTokens.default))
            .background(PickeTheme.colors.backgroundBeige)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.s24, vertical = SpacingTokens.s32),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.s16)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4)) {
                Image(
                    painter = painterResource(id = guide.leftAvatarRes),
                    contentDescription = null
                )
                ClassGuideChatBubble(
                    name = stringResource(guide.leftNameRes),
                    message = stringResource(guide.leftMessageRes),
                    nameColor = nameColor,
                    bubbleColor = PickeTheme.colors.surfaceBeigeDefault,
                    bubbleBorderColor = PickeTheme.colors.borderBeigeDefault,
                    horizontalAlignment = Alignment.Start
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.s4, Alignment.End)
            ) {
                ClassGuideChatBubble(
                    name = stringResource(guide.rightNameRes),
                    message = stringResource(guide.rightMessageRes),
                    nameColor = nameColor,
                    bubbleColor = rightBubbleColor,
                    bubbleBorderColor = rightBubbleBorderColor,
                    horizontalAlignment = Alignment.End
                )
                Image(
                    painter = painterResource(id = guide.rightAvatarRes),
                    contentDescription = null
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(ClassMethodGuideSheetDimens.fadeHeight)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            PickeTheme.colors.backgroundSubtle.copy(alpha = 0f),
                            PickeTheme.colors.backgroundSubtle
                        )
                    )
                )
        )
    }
}

@Composable
private fun ClassGuideChatBubble(
    name: String,
    message: String,
    nameColor: Color,
    bubbleColor: Color,
    bubbleBorderColor: Color,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(RadiusTokens.default)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.s4),
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = name,
            color = nameColor,
            style = PickeTheme.typography.captionMdSemiBold
        )
        Text(
            text = message,
            modifier = Modifier
                .width(ClassMethodGuideSheetDimens.bubbleWidth)
                .background(bubbleColor, shape)
                .border(ComponentNumberTokens.borderWidthRegular, bubbleBorderColor, shape)
                .padding(
                    horizontal = SpacingTokens.s6,
                    vertical = SpacingTokens.s4
                ),
            color = PickeTheme.colors.textBody,
            style = PickeTheme.typography.captionSmRegular
        )
    }
}

@Preview(name = "Pické 배틀", showBackground = true)
@Composable
private fun ClassMethodGuideSheetContentPreview() {
    PickeTheme {
        ClassMethodGuideSheetContent(
            method = ClassCreateMethod.CONTENT,
            onConfirmClick = {},
            modifier = Modifier
                .background(PickeTheme.colors.surfaceBeigeDefault)
                .padding(horizontal = SpacingTokens.s16)
        )
    }
}

@Preview(name = "커스텀 배틀", showBackground = true)
@Composable
private fun ClassMethodGuideSheetCustomContentPreview() {
    PickeTheme {
        ClassMethodGuideSheetContent(
            method = ClassCreateMethod.AI_TOPIC,
            onConfirmClick = {},
            modifier = Modifier
                .background(PickeTheme.colors.surfaceBeigeDefault)
                .padding(horizontal = SpacingTokens.s16)
        )
    }
}