package ru.easycode.intensive2itunessearch.delete_playlist

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.delete_playlist.data.EditPlaylistRepository

class EditPlaylistRepositoryTest {

    @Test
    fun test() = runBlocking {
        val playlistDao = FakePlaylistDao()
        val relationDao = FakeRelationDao()

        val repository = EditPlaylistRepository.Base(
            playlistDao = playlistDao,
            relationDao = relationDao,
        )

        repository.deletePlaylist(playlistId = 100L)
        assertEquals(100L, playlistDao.deleteIdCalledArg)
        assertEquals(100L, relationDao.playlistIdCalledArg)

        repository.renamePlaylist(playlistId = 200L, name = "new_name")
        assertEquals(200L, playlistDao.updateIdCalledArg)
        assertEquals("new_name", playlistDao.updateNameCalledArg)
    }
}

private class FakePlaylistDao : PlaylistDao {

    var deleteIdCalledArg: Long = 0L
    var updateIdCalledArg: Long = 0L
    var updateNameCalledArg: String = ""

    override suspend fun deletePlaylist(playlistId: Long) {
        deleteIdCalledArg = playlistId
    }

    override suspend fun update(playlistId: Long, name: String) {
        updateIdCalledArg = playlistId
        updateNameCalledArg = name
    }

    override suspend fun insert(playlist: PlaylistCache) = Unit

    override suspend fun playlists(): List<PlaylistCache> = emptyList()
}

private class FakeRelationDao : RelationDao {

    var playlistIdCalledArg: Long = 0L

    override suspend fun deleteRelationsForPlaylist(playlistId: Long) {
        playlistIdCalledArg = playlistId
    }

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) = Unit

    override suspend fun insert(relationCache: RelationCache) = Unit

    override suspend fun getPlaylistsWithoutTrack(trackId: Long): List<PlaylistCache> = emptyList()

    override suspend fun playlistTracks(playlistId: Long): List<TrackCache> = emptyList()
}