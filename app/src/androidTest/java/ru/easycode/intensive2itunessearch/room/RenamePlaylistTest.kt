package ru.easycode.intensive2itunessearch.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.easycode.intensive2itunessearch.core.data.cache.DashboardDataBase
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistCache
import ru.easycode.intensive2itunessearch.core.data.cache.PlaylistDao
import java.io.IOException

class RenamePlaylistTest {
    private lateinit var db: DashboardDataBase
    private lateinit var playlistDao: PlaylistDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DashboardDataBase::class.java
        ).build()
        playlistDao = db.playlistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testAddTrackToPlaylist() = runBlocking {
        playlistDao.insert(PlaylistCache(100L, "playlist1"))
        assertEquals("playlist1", playlistDao.playlists()[0].name)

        playlistDao.update(playlistId = 100L, name = "new_name")
        assertEquals("new_name", playlistDao.playlists()[0].name)
    }
}