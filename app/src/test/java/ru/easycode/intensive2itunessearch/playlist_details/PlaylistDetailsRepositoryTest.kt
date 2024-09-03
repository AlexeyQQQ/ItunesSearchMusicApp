package ru.easycode.intensive2itunessearch.playlist_details

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentPlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.CurrentTrackIdCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.dashboard.data.Track
import ru.easycode.intensive2itunessearch.playlist_details.data.PlaylistDetailsRepository

class PlaylistDetailsRepositoryTest {

    @Test
    fun test() = runBlocking {
        val relationDao = FakeRelationDao()
        val currentPlaylistCache = FakeCurrentPlaylistCache()
        val currentTrackIdCache = FakeCurrentTrackIdCache()

        val repository = PlaylistDetailsRepository.Base(
            relationDao = relationDao,
            currentPlaylistCache = currentPlaylistCache,
            currentTrackIdCache = currentTrackIdCache,
        )

        val actual = repository.tracks(playlistId = 100L)
        val expected = listOf(
            Track(
                trackId = 100L,
                trackUrl = "trackUrl1",
                coverUrl = "coverUrl1",
                trackName = "testTitle1",
                artistName = "testSubTitle1",
            ),
            Track(
                trackId = 200L,
                trackUrl = "trackUrl2",
                coverUrl = "coverUrl2",
                trackName = "testTitle2",
                artistName = "testSubTitle2",
            )
        )
        assertEquals(actual, expected)
        assertEquals(100L, relationDao.playlistIdCalledArg)
    }
}

private class FakeRelationDao(
) : RelationDao {

    private val currentList = listOf(
        TrackCache(
            term = "term",
            trackId = 100L,
            previewUrl = "trackUrl1",
            artworkUrl = "coverUrl1",
            trackName = "testTitle1",
            artistName = "testSubTitle1",
        ),
        TrackCache(
            term = "term",
            trackId = 200L,
            previewUrl = "trackUrl2",
            artworkUrl = "coverUrl2",
            trackName = "testTitle2",
            artistName = "testSubTitle2",
        )
    )

    var playlistIdCalledArg: Long = -1L

    override suspend fun playlistTracks(playlistId: Long): List<TrackCache> {
        playlistIdCalledArg = playlistId
        return currentList
    }

    override suspend fun deleteRelationsForPlaylist(playlistId: Long) = Unit

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) = Unit

    override suspend fun insert(relationCache: RelationCache) = Unit

    override suspend fun getPlaylistsWithoutTrack(trackId: Long): List<PlaylistCache> = emptyList()
}

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