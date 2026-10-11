package com.picke.presentation.ui.classroom.classcreate.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.picke.presentation.R

data class ClassMethodGuideUiModel(
    @param:StringRes val titleRes: Int,
    @param:StringRes val headlineRes: Int,
    @param:StringRes val descriptionRes: Int,
    @param:DrawableRes val leftAvatarRes: Int,
    @param:StringRes val leftNameRes: Int,
    @param:StringRes val leftMessageRes: Int,
    @param:DrawableRes val rightAvatarRes: Int,
    @param:StringRes val rightNameRes: Int,
    @param:StringRes val rightMessageRes: Int,
    val isRightMe: Boolean
)

fun ClassCreateMethod.toGuideUiModel(): ClassMethodGuideUiModel = when (this) {
    ClassCreateMethod.CONTENT -> ClassMethodGuideUiModel(
        titleRes = R.string.class_guide_content_title,
        headlineRes = R.string.class_guide_content_headline,
        descriptionRes = R.string.class_guide_content_description,
        leftAvatarRes = R.drawable.illust_kant,
        leftNameRes = R.string.class_guide_content_chat_left_name,
        leftMessageRes = R.string.class_guide_content_chat_left_message,
        rightAvatarRes = R.drawable.illust_nietzsche,
        rightNameRes = R.string.class_guide_content_chat_right_name,
        rightMessageRes = R.string.class_guide_content_chat_right_message,
        isRightMe = false
    )

    ClassCreateMethod.AI_TOPIC -> ClassMethodGuideUiModel(
        titleRes = R.string.class_guide_custom_title,
        headlineRes = R.string.class_guide_custom_headline,
        descriptionRes = R.string.class_guide_custom_description,
        leftAvatarRes = R.drawable.illust_plato,
        leftNameRes = R.string.class_guide_custom_chat_left_name,
        leftMessageRes = R.string.class_guide_custom_chat_left_message,
        rightAvatarRes = R.drawable.illust_owl,
        rightNameRes = R.string.class_guide_custom_chat_right_name,
        rightMessageRes = R.string.class_guide_custom_chat_right_message,
        isRightMe = true
    )
}