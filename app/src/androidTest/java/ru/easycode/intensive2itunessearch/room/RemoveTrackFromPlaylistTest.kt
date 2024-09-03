package ru.easycode.intensive2itunessearch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.easycode.intensive2itunessearch.core.data.cache.DashboardDataBase
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import ru.easycode.intensive2itunessearch.core.data.cache.RelationCache
import ru.easycode.intensive2itunessearch.core.data.cache.RelationDao
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RemoveTrackFromPlaylistTest {

    private lateinit var db: DashboardDataBase
    private lateinit var playlistDao: PlaylistDao
    private lateinit var relationDao: RelationDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DashboardDataBase::class.java
        ).build()
        playlistDao = db.playlistDao()
        relationDao = db.relationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testRemoveTrackFromPlaylist() = runBlocking {
        val playlist1 = PlaylistCache(100, "playlist1")
        val playlist2 = PlaylistCache(200, "playlist2")
        val track1 = TrackCache("term1", 1, "a", "track1", "1", "1")
        val track2 = TrackCache("term2", 2, "a", "track2", "2", "2")

        playlistDao.insert(playlist1)
        playlistDao.insert(playlist2)

        var expected = listOf(playlist1, playlist2)
        var actual = relationDao.getPlaylistsWithoutTrack(trackId = track1.trackId)
        assertEquals(expected, actual)

        actual = relationDao.getPlaylistsWithoutTrack(trackId = track2.trackId)
        assertEquals(expected, actual)

        relationDao.insert(RelationCache(trackId = track1.trackId, playlistId = playlist1.id))
        relationDao.insert(RelationCache(trackId = track1.trackId, playlistId = playlist2.id))
        relationDao.insert(RelationCache(trackId = track2.trackId, playlistId = playlist1.id))
        relationDao.insert(RelationCache(trackId = track2.trackId, playlistId = playlist2.id))

        expected = listOf()
        actual = relationDao.getPlaylistsWithoutTrack(trackId = track1.trackId)
        assertEquals(expected, actual)
        actual = relationDao.getPlaylistsWithoutTrack(trackId = track2.trackId)
        assertEquals(expected, actual)

        relationDao.removeTrackFromPlaylist(trackId = track1.trackId, playlistId = playlist1.id)

        expected = listOf(playlist1)
        actual = relationDao.getPlaylistsWithoutTrack(trackId = track1.trackId)
        assertEquals(expected, actual)

        expected = listOf()
        actual = relationDao.getPlaylistsWithoutTrack(trackId = track2.trackId)
        assertEquals(expected, actual)
    }
}