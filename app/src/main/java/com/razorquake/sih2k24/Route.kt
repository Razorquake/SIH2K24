package com.razorquake.sih2k24

sealed class Route(
    val route: String,
) {
    data object SignTranslationScreen : Route("sign_translation_screen")
    data object HomeScreen : Route("home_screen")
    data object HistoryScreen : Route("history_screen")
    data object SettingsScreen : Route("settings_screen")
    data object TextTranslationScreen : Route("text_translation_screen")
}