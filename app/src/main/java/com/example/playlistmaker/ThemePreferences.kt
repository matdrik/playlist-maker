package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val THEME_KEY = "theme_key"
        private const val THEME_LIGHT = "light"
        private const val THEME_DARK = "dark"
    }

    fun saveTheme(isDark: Boolean) {
        val theme = if (isDark) THEME_DARK else THEME_LIGHT
        sharedPreferences.edit()
            .putString(THEME_KEY, theme)
            .apply()
    }

    fun isDarkTheme(): Boolean {
        val theme = sharedPreferences.getString(THEME_KEY, null)
        return theme == THEME_DARK
    }
}
