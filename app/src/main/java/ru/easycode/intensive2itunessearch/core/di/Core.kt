package ru.easycode.intensive2itunessearch.core.di

import android.content.Context
import ru.easycode.intensive2itunessearch.BuildConfig
import ru.easycode.intensive2itunessearch.R
import ru.easycode.intensive2itunessearch.core.data.cache.CacheDashboardModule
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.StringCache
import ru.easycode.intensive2itunessearch.core.data.cache.StringPermanentStorage
import ru.easycode.intensive2itunessearch.core.presentation.RunAsync
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.ControllerWrapper
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.FakeControllerWrapper
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayer
import ru.easycode.intensive2itunessearch.core.presentation.exo_player.MediaPlayerRepository
import ru.easycode.intensive2itunessearch.main.presentation.MainObservable

class Core(context: Context) {

    val runUiTest: Boolean = !BuildConfig.DEBUG

    val runAsync: RunAsync = RunAsync.Base()

    val cacheDashboardModule = CacheDashboardModule.Base(context)

    val mainObservable = MainObservable.Base()

    val mediaPlayer: MediaPlayer

    val currentTrackIdCache: CurrentTrackIdCache

    val currentPlaylistCache: CurrentPlaylistCache

    val userQuery: StringCache

    init {
        val sharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

        val stringPermanentStorage = StringPermanentStorage.Base(sharedPreferences)
        userQuery = StringCache.Base(USER_QUERY_KEY, stringPermanentStorage, "")

        currentTrackIdCache = CurrentTrackIdCache.Base(sharedPreferences)
        currentPlaylistCache = CurrentPlaylistCache.Base(sharedPreferences)

        val database = cacheDashboardModule.database()

        mediaPlayer = MediaPlayer.Base(
            mediaPlayerRepository = MediaPlayerRepository.Base(
                currentTrackIdCache = currentTrackIdCache,
                currentPlaylistCache = currentPlaylistCache,
                tracksDao = database.trackDao(),
                relationDao = database.relationDao(),
            ),
            controllerWrapper = if (runUiTest) FakeControllerWrapper()
            else ControllerWrapper.Base(applicationContext = context)
        )
    }

    companion object {
        private const val USER_QUERY_KEY = "USER_QUERY_KEY"
    }
}