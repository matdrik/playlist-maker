package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val HISTORY_KEY = "search_history_key"
        private const val MAX_HISTORY_SIZE = 10
    }

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()

        // Удаляем трек, если он уже есть в истории
        history.removeAll { it.trackId == track.trackId }

        // Добавляем новый трек в начало списка
        history.add(0, track)

        // Ограничиваем размер истории
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)
        }

        // Сохраняем обновленную историю
        saveHistory(history)
    }

    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return try {
            Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearHistory() {
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }

    private fun saveHistory(history: List<Track>) {
        val json = Gson().toJson(history)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }
}
