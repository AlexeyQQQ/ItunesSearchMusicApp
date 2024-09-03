package ru.easycode.intensive2itunessearch.playlist.data

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.create_playlist.data.CreatePlaylistRepository

class CreatePlaylistRepositoryTest {

    @Test
    fun test() = runBlocking {
        val now = FakeNow()
        val playlistDao = FakePlaylistDao()
        val repository = CreatePlaylistRepository.Base(
            now = now,
            playlistDao = playlistDao
        )

        repository.createPlaylist("playlist1")
        repository.createPlaylist("playlist2")

        val expected = listOf(
            PlaylistCache(1, "playlist1"),
            PlaylistCache(2, "playlist2")
        )
        val actual = playlistDao.listPlaylists

        assertEquals(expected, actual)
    }

    private class FakeNow : Now {
        private var time = 1L

        override fun timeInMillis(): Long {
            return time++
        }
    }

    private class FakePlaylistDao : PlaylistDao {
        val listPlaylists = mutableListOf<PlaylistCache>()

        override suspend fun insert(playlist: PlaylistCache) {
            listPlaylists.add(playlist)
        }

        override suspend fun playlists(): List<PlaylistCache> = emptyList()

        override suspend fun deletePlaylist(playlistId: Long) = Unit

        override suspend fun update(playlistId: Long, name: String) = Unit
    }
}