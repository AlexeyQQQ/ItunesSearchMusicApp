package ru.easycode.intensive2itunessearch.core.data.cache

import android.content.SharedPreferences

interface CurrentTrackIdCache {

    fun save(id: Long)

    fun read(): Long

    class Base(
        private val sharedPreferences: SharedPreferences,
    ) : CurrentTrackIdCache {

        override fun save(id: Long) {
            sharedPreferences.edit().putLong(KEY, id).apply()
        }

        override fun read(): Long {
            return sharedPreferences.getLong(KEY, -1)
        }

        companion object {
            private const val KEY = "current_track_key"
        }
    }
}