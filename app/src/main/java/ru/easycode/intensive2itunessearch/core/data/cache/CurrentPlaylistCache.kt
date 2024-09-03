package ru.easycode.intensive2itunessearch.core.data.cache

import android.content.SharedPreferences

interface CurrentPlaylistCache {

    fun isPlaylistFromQuery(): Boolean

    fun currentPlaylistSaved(): Boolean

    fun currentPlaylistId(): Long

    fun currentPlaylistQuery(): String

    fun saveCurrentPlaylistId(id: String)

    class Base(
        private val sharedPreferences: SharedPreferences,
    ) : CurrentPlaylistCache {

        override fun isPlaylistFromQuery(): Boolean {
            val id = currentPlaylistQuery().toLongOrNull()
            return id == null
        }

        override fun currentPlaylistSaved(): Boolean {
            return currentPlaylistQuery().isNotEmpty()
        }

        override fun currentPlaylistId(): Long {
            return currentPlaylistQuery().toLong()
        }

        override fun currentPlaylistQuery(): String {
            return sharedPreferences.getString(KEY, "") ?: ""
        }

        override fun saveCurrentPlaylistId(id: String) {
            sharedPreferences.edit().putString(KEY, id).apply()
        }

        companion object {
            private const val KEY = "current_playlist_key"
        }
    }
}