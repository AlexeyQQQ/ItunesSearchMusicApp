package ru.easycode.intensive2itunessearch.dashboard.data.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.core.data.cache.TracksDao
import ru.easycode.intensive2itunessearch.dashboard.data.cloud.TrackCloud

interface CacheDashboardDataSource {

    suspend fun save(term: String, data: List<TrackCloud>)

    suspend fun tracks(term: String): List<TrackCache>

    class Base(
        private val dao: TracksDao,
        private val now: Now,
    ) : CacheDashboardDataSource {

        private val mutex = Mutex()

        override suspend fun save(term: String, data: List<TrackCloud>) = mutex.withLock {
            val time = now.timeInMillis()
            val list = data.mapIndexed { index, it ->
                TrackCache(
                    term,
                    it.trackId,
                    it.artistName,
                    it.trackName,
                    it.previewUrl,
                    it.artworkUrl,
                    time + index,
                )
            }
            dao.save(list)
        }

        override suspend fun tracks(term: String): List<TrackCache> {
            return dao.tracks(term)
        }
    }
}
