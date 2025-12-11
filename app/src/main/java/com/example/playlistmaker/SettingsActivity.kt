package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val sharedPreferences = getSharedPreferences(SearchActivity.PREFERENCES_NAME, Context.MODE_PRIVATE)
        val themePreferences = ThemePreferences(sharedPreferences)

        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Настройка переключателя темы
        val switchTheme = findViewById<Switch>(R.id.switchTheme)
        switchTheme.isChecked = themePreferences.isDarkTheme()

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            themePreferences.saveTheme(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Кнопка "Поделиться приложением"
        val shareAppLayout = findViewById<RelativeLayout>(R.id.shareAppLayout)
        shareAppLayout.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message))
            }
            val chooser = Intent.createChooser(shareIntent, null)
            startActivity(chooser)
        }

        // Кнопка "Написать в поддержку"
        val supportLayout = findViewById<RelativeLayout>(R.id.supportLayout)
        supportLayout.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_body))
            }
            startActivity(emailIntent)
        }

        // Кнопка "Пользовательское соглашение"
        val userAgreementLayout = findViewById<RelativeLayout>(R.id.userAgreementLayout)
        userAgreementLayout.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_url)))
            startActivity(browserIntent)
        }
    }
}
