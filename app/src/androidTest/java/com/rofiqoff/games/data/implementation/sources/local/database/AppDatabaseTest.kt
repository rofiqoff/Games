package com.rofiqoff.games.data.implementation.sources.local.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var dao: GameFavoriteDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        dao = db.gameFavoriteDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeGameAndReadInList() = runTest {
        val game = GameFavoriteEntity(
            id = 1,
            name = "name",
            description = "description",
            released = "released",
            imageUrl = "backgroundImage",
            website = "website",
            rating = "1.0"
        )
        dao.insertGames(game)
        val byId = dao.findById(1)
        assert(byId?.id == 1L)
    }

}
