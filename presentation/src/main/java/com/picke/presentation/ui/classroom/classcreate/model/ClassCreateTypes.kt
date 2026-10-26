package com.picke.presentation.ui.classroom.classcreate.model

import androidx.annotation.StringRes
import com.picke.presentation.R

enum class ClassCreateStep {
    METHOD,
    CONDITION,
    SELECTION,
    SETTING
}

enum class ClassCreateMethod {
    CONTENT,
    AI_TOPIC
}

enum class ClassLevel(
    @param:StringRes val labelRes: Int,
    val apiValue: String
) {
    BEGINNER(R.string.class_level_beginner, "BEGINNER"),
    INTERMEDIATE(R.string.class_level_intermediate, "INTERMEDIATE"),
    ADVANCED(R.string.class_level_advanced, "ADVANCED")
}

enum class ClassCategory(
    @param:StringRes val labelRes: Int,
    val apiValue: String,
    val topicApiValue: String
) {
    PHILOSOPHY(R.string.class_category_philosophy, "PHILOSOPHY", "철학"),
    SOCIETY(R.string.class_category_society, "SOCIETY", "사회"),
    LITERATURE(R.string.class_category_literature, "LITERATURE", "문학"),
    SCIENCE(R.string.class_category_science, "SCIENCE", "과학"),
    ART(R.string.class_category_art, "ART", "예술"),
    HISTORY(R.string.class_category_history, "HISTORY", "역사")
}

enum class ClassTopicSuggestStatus {
    NONE,
    GENERATING,
    SUCCEEDED,
    NEEDS_CLARIFICATION,
    FAILED;

    companion object {
        fun from(value: String): ClassTopicSuggestStatus = entries.find { it.name == value } ?: FAILED
    }
}

enum class ClassCreateErrorDialog {
    BATTLE_SEARCH_FAILED,
    TOPIC_GENERATION_FAILED,
    TOPIC_CLARIFICATION_NEEDED,
    CLASS_CREATION_FAILED
}