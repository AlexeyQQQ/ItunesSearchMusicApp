package ru.easycode.intensive2itunessearch.core

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.presentation.PlayTrackRepository

class PlayTrackRepositoryTest {

    @Test
    fun test() {
        val currentPlaylistCache = FakeCurrentPlaylistCache()
        val currentTrackIdCache = FakeCurrentTrackIdCache()
        val repository = Repository(currentTrackIdCache, currentPlaylistCache)

        repository.saveCurrentlyPlayingId(100L)
        assertEquals(100L, currentTrackIdCache.calledTrackIdArg)

        repository.saveCurrentPlayingPlaylist("1234")
        assertEquals("1234", currentPlaylistCache.calledPlaylistIdArg)

        val actualSavedId = repository.readCurrentlyPlayingId()
        assertEquals(100L, actualSavedId)
    }
}

private class Repository(
    currentTrackIdCache: CurrentTrackIdCache,
    currentPlaylistCache: CurrentPlaylistCache,
) : PlayTrackRepository.Abstract(currentTrackIdCache, currentPlaylistCache)

private class FakeCurrentPlaylistCache : CurrentPlaylistCache {

    var calledPlaylistIdArg: String = ""

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

    override fun currentPlaylistQuery(): String = calledPlaylistIdArg

    override fun saveCurrentPlaylistId(id: String) {
        calledPlaylistIdArg = id
    }
}

private class FakeCurrentTrackIdCache : CurrentTrackIdCache {

    var calledTrackIdArg: Long = -1

    override fun save(id: Long) {
        calledTrackIdArg = id
    }

    override fun read(): Long = calledTrackIdArg
}