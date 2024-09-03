package ru.easycode.intensive2itunessearch.main.data

import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.presentation.PlayTrackRepository

interface MainRepository : PlayTrackRepository {

    class Base(
        currentTrackIdCache: CurrentTrackIdCache,
        currentPlaylistCache: CurrentPlaylistCache,
    ) : PlayTrackRepository.Abstract(currentTrackIdCache, currentPlaylistCache), MainRepository
}