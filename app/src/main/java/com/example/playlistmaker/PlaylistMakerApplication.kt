package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class PlaylistMakerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Загружаем тему при запуске приложения
        val sharedPreferences = getSharedPreferences(SearchActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val themePreferences = ThemePreferences(sharedPreferences)
        val isDarkTheme = themePreferences.isDarkTheme()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
