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
import ru.easycode.intensive2itunessearch.core.data.cache.TrackCache
import ru.easycode.intensive2itunessearch.core.data.cache.TracksDao
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SaveTrackTest {

    private lateinit var dao: TracksDao
    private lateinit var db: DashboardDataBase

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DashboardDataBase::class.java
        ).build()
        dao = db.trackDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun checkQuery() = runBlocking {
        dao.save(
            listOf(
                TrackCache("term1", 1, "a", "track1", "1", "1"),
                TrackCache("term2", 2, "b", "track2", "2", "2"),
                TrackCache("term1", 3, "c", "track3", "3", "3")
            )
        )
        val actual = dao.tracks("term1")

        val expected = listOf(
            TrackCache("term1", 1, "a", "track1", "1", "1"),
            TrackCache("term1", 3, "c", "track3", "3", "3")
        )
        assertEquals(expected, actual)
    }
}