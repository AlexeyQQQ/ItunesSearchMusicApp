package ru.easycode.intensive2itunessearch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.easycode.intensive2itunessearch.core.data.cache.DashboardDataBase
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.core.data.cache.TracksDao
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GetPlaylistTracksTest {

    private lateinit var db: DashboardDataBase
    private lateinit var tracksDao: TracksDao
    private lateinit var playlistDao: PlaylistDao
    private lateinit var relationDao: RelationDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DashboardDataBase::class.java
        ).build()
        tracksDao = db.trackDao()
        playlistDao = db.playlistDao()
        relationDao = db.relationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getPlaylistTracksTest() = runBlocking {
        val track1 = TrackCache("term1", 1, "a", "track1", "1", "1")
        val track2 = TrackCache("term2", 2, "b", "track2", "2", "2")
        val playlist1 = PlaylistCache(100, "playlist1")
        val playlist2 = PlaylistCache(200, "playlist2")

        tracksDao.save(listOf(track1, track2))
        playlistDao.insert(playlist1)
        playlistDao.insert(playlist2)

        var expectedPlaylist1 = emptyList<TrackCache>()
        var actualPlaylist1 = relationDao.playlistTracks(playlist1.id)
        assertEquals(expectedPlaylist1, actualPlaylist1)

        var expectedPlaylist2 = emptyList<TrackCache>()
        var actualPlaylist2 = relationDao.playlistTracks(playlist2.id)
        assertEquals(expectedPlaylist2, actualPlaylist2)

        relationDao.insert(RelationCache(trackId = track1.trackId, playlistId = playlist1.id))
        expectedPlaylist1 = listOf(track1)
        actualPlaylist1 = relationDao.playlistTracks(playlist1.id)
        actualPlaylist2 = relationDao.playlistTracks(playlist2.id)
        assertEquals(expectedPlaylist1, actualPlaylist1)
        assertEquals(expectedPlaylist2, actualPlaylist2)

        relationDao.insert(RelationCache(trackId = track2.trackId, playlistId = playlist1.id))
        relationDao.insert(RelationCache(trackId = track2.trackId, playlistId = playlist2.id))
        expectedPlaylist1 = listOf(track1, track2)
        actualPlaylist1 = relationDao.playlistTracks(playlist1.id)
        expectedPlaylist2 = listOf(track2)
        actualPlaylist2 = relationDao.playlistTracks(playlist2.id)
        assertEquals(expectedPlaylist1, actualPlaylist1)
        assertEquals(expectedPlaylist2, actualPlaylist2)
    }
}