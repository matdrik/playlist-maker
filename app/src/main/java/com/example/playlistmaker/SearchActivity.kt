package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private var searchEditText: EditText? = null
    private var searchText: String = ""

    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        const val SEARCH_TEXT_DEFAULT = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButton)

        // Восстановление сохраненного текста при пересоздании activity
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_DEFAULT)
            searchEditText?.setText(searchText)
        }

        // Обработчик кнопки очистки
        clearButton.setOnClickListener {
            searchEditText?.setText("")
            // Скрыть клавиатуру
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText?.windowToken, 0)
        }

        // TextWatcher для отслеживания изменений текста
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Пустая реализация
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Показываем/скрываем кнопку очистки в зависимости от наличия текста
                clearButton.visibility = if (s.isNullOrEmpty()) {
                    android.view.View.GONE
                } else {
                    android.view.View.VISIBLE
                }

                // Заглушка для будущих задач
                // Здесь будет логика поиска
            }

            override fun afterTextChanged(s: Editable?) {
                // Сохраняем текст из EditText в переменную
                searchText = s?.toString() ?: ""
            }
        }

        searchEditText?.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текст из EditText в Bundle
        outState.putString(SEARCH_TEXT_KEY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Восстанавливаем данные из Bundle
        searchText = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_DEFAULT)
        // Устанавливаем восстановленные данные обратно в EditText
        searchEditText?.setText(searchText)
    }
}
