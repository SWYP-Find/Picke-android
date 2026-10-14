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

enum class ClassLevel(@param:StringRes val labelRes: Int) {
    BEGINNER(R.string.class_level_beginner),
    INTERMEDIATE(R.string.class_level_intermediate),
    ADVANCED(R.string.class_level_advanced)
}

enum class ClassCategory(@param:StringRes val labelRes: Int) {
    PHILOSOPHY(R.string.class_category_philosophy),
    SOCIETY(R.string.class_category_society),
    LITERATURE(R.string.class_category_literature),
    SCIENCE(R.string.class_category_science),
    ART(R.string.class_category_art),
    HISTORY(R.string.class_category_history)
}