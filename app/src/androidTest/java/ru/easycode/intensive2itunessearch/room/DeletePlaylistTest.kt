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
class DeletePlaylistTest {

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
    fun testDeletePlaylist() = runBlocking {
        val playlist1 = PlaylistCache(100, "playlist1")
        val track1 = TrackCache("term1", 1, "a", "track1", "1", "1")

        tracksDao.save(listOf(track1))
        playlistDao.insert(playlist1)
        assertEquals(listOf(playlist1), playlistDao.playlists())

        relationDao.insert(RelationCache(trackId = track1.trackId, playlistId = playlist1.id))
        assertEquals(listOf(track1), relationDao.playlistTracks(playlist1.id))

        playlistDao.deletePlaylist(playlist1.id)
        assertEquals(emptyList<PlaylistCache>(), playlistDao.playlists())
        assertEquals(listOf(track1), relationDao.playlistTracks(playlist1.id))

        relationDao.deleteRelationsForPlaylist(playlist1.id)
        assertEquals(emptyList<PlaylistCache>(), playlistDao.playlists())
        assertEquals(emptyList<TrackCache>(), relationDao.playlistTracks(playlist1.id))
    }
}