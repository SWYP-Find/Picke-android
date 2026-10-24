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
    val apiValue: String
) {
    PHILOSOPHY(R.string.class_category_philosophy, "PHILOSOPHY"),
    SOCIETY(R.string.class_category_society, "SOCIETY"),
    LITERATURE(R.string.class_category_literature, "LITERATURE"),
    SCIENCE(R.string.class_category_science, "SCIENCE"),
    ART(R.string.class_category_art, "ART"),
    HISTORY(R.string.class_category_history, "HISTORY")
}

enum class ClassCreateErrorDialog {
    BATTLE_SEARCH_FAILED,
    TOPIC_GENERATION_FAILED,
    CLASS_CREATION_FAILED
}