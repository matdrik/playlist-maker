package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private var searchEditText: EditText? = null
    private var searchText: String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var historyContainer: LinearLayout
    private lateinit var historyTitle: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var tracksRecyclerView: RecyclerView
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var tracksAdapter: TrackAdapter

    companion object {
        const val SEARCH_TEXT_KEY = "SEARCH_TEXT_KEY"
        const val SEARCH_TEXT_DEFAULT = ""
        const val PREFERENCES_NAME = "playlist_maker_preferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация SharedPreferences и SearchHistory
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        historyContainer = findViewById(R.id.searchHistoryContainer)
        historyTitle = findViewById(R.id.searchHistoryTitle)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyRecyclerView = findViewById(R.id.searchHistoryRecyclerView)
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView)

        // Настройка RecyclerView для истории
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = TrackAdapter(ArrayList()) { track ->
            searchHistory.addTrack(track)
            updateHistoryDisplay()
        }
        historyRecyclerView.adapter = historyAdapter

        // Восстановление сохраненного текста при пересоздании activity
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(SEARCH_TEXT_KEY, SEARCH_TEXT_DEFAULT)
            searchEditText?.setText(searchText)
            // Обновляем видимость иконки очистки при восстановлении состояния
            clearButton.visibility = if (searchText.isEmpty()) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }
        } else {
            // Инициализация: скрываем иконку, если поле пустое
            clearButton.visibility = android.view.View.GONE
        }

        // Обработчик кнопки очистки
        clearButton.setOnClickListener {
            searchEditText?.setText("")
            // Скрыть клавиатуру
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText?.windowToken, 0)
        }

        // Обработчик кнопки очистки истории
        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            updateHistoryDisplay()
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

                // Обновляем отображение истории/результатов поиска
                updateSearchDisplay(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
                // Сохраняем текст из EditText в переменную
                searchText = s?.toString() ?: ""
            }
        }

        searchEditText?.addTextChangedListener(textWatcher)

        // Отслеживание фокуса
        searchEditText?.setOnFocusChangeListener { _, hasFocus ->
            updateSearchDisplay(searchText)
        }

        // Создание mock-данных для списка треков
        val tracks = ArrayList<Track>().apply {
            add(Track(
                trackId = 1,
                trackName = "Smells Like Teen Spirit",
                artistName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ))
            add(Track(
                trackId = 2,
                trackName = "Billie Jean",
                artistName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = ""
            ))
            add(Track(
                trackId = 3,
                trackName = "Stayin' Alive",
                artistName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ))
            add(Track(
                trackId = 4,
                trackName = "Whole Lotta Love",
                artistName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ))
            add(Track(
                trackId = 5,
                trackName = "Sweet Child O'Mine",
                artistName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            ))
        }

        // Настройка RecyclerView для результатов поиска
        tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        tracksAdapter = TrackAdapter(tracks) { track ->
            searchHistory.addTrack(track)
            updateHistoryDisplay()
        }
        tracksRecyclerView.adapter = tracksAdapter

        // Инициализация отображения истории
        updateHistoryDisplay()

        // Устанавливаем фокус на поле поиска при первом запуске, если текст пустой
        if (searchText.isEmpty()) {
            searchEditText?.requestFocus()
        }
    }

    private fun updateSearchDisplay(text: String) {
        val hasFocus = searchEditText?.hasFocus() == true
        val isEmpty = text.isEmpty()

        if (isEmpty && hasFocus) {
            // Показываем историю, если поле пустое и в фокусе
            val history = searchHistory.getHistory()
            if (history.isNotEmpty()) {
                historyAdapter.tracks.clear()
                historyAdapter.tracks.addAll(history)
                historyAdapter.notifyDataSetChanged()
                historyContainer.visibility = android.view.View.VISIBLE
                tracksRecyclerView.visibility = android.view.View.GONE
            } else {
                historyContainer.visibility = android.view.View.GONE
                tracksRecyclerView.visibility = android.view.View.GONE
            }
        } else {
            // Показываем результаты поиска
            historyContainer.visibility = android.view.View.GONE
            tracksRecyclerView.visibility = android.view.View.VISIBLE
        }
    }

    private fun updateHistoryDisplay() {
        val history = searchHistory.getHistory()
        if (history.isNotEmpty()) {
            historyAdapter.tracks.clear()
            historyAdapter.tracks.addAll(history)
            historyAdapter.notifyDataSetChanged()
        }
        // Обновляем отображение в зависимости от состояния поля поиска
        updateSearchDisplay(searchText)
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
