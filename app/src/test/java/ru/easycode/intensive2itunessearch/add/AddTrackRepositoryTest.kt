package ru.easycode.intensive2itunessearch.add

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.add.data.AddTrackRepository
import ru.easycode.intensive2itunessearch.add.data.Playlist
import ru.easycode.intensive2itunessearch.core.data.cache.Now
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.dashboard.data.Track

class AddTrackRepositoryTest {

    /**
     * 1. Save playlists
     * 2. Add a track to the second playlist
     * 3. Get all playlists for this track
     *  and make sure that there are 1 and 3 playlists there
     */
    @Test
    fun test() = runBlocking {
        val now = FakeNow()
        val playlistDao = FakePlaylistDao()
        val relationDao = FakeRelationDao(fakePlaylistDao = playlistDao)

        val repository = AddTrackRepository.Base(
            now = now,
            playlistDao = playlistDao,
            relationDao = relationDao,
        )

        val playlist1 = Playlist(1, "playlist1")
        val playlist2 = Playlist(2, "playlist2")
        val playlist3 = Playlist(3, "playlist3")
        val track1 = Track(1, "a", "track1", "1", "1")

        repository.savePlaylist("playlist1")
        repository.savePlaylist("playlist2")
        repository.savePlaylist("playlist3")

        var expected = listOf(playlist1, playlist2, playlist3)
        var actual = repository.playlists(track1)
        assertEquals(expected, actual)

        repository.addTrackToPlaylist(playlist = playlist2, track = track1)

        expected = listOf(playlist1, playlist3)
        actual = repository.playlists(track1)
        assertEquals(expected, actual)
    }
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

private class FakeRelationDao(
    private val fakePlaylistDao: FakePlaylistDao,
) : RelationDao {

    val listRelations = mutableListOf<RelationCache>()

    override suspend fun insert(relationCache: RelationCache) {
        listRelations.add(relationCache)
    }

    override suspend fun getPlaylistsWithoutTrack(trackId: Long): List<PlaylistCache> {
        val playlistsId = listRelations.filter {
            it.trackId == trackId
        }.map {
            it.playlistId
        }

        val list = fakePlaylistDao.listPlaylists.filter {
            it.id !in playlistsId
        }

        return list
    }

    override suspend fun playlistTracks(playlistId: Long): List<TrackCache> = emptyList()

    override suspend fun deleteRelationsForPlaylist(playlistId: Long) = Unit

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) = Unit
}