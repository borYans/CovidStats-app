package com.boryans.covidstats.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.boryans.covidstats.getOrAwaitValue
import com.boryans.covidstats.model.Country
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class) // this annotation means that this tests will run on Android Emulator and not on plain java/kotlin enviroment.
@SmallTest                     // this tell that test are unit tests (base of pyramid - testing)
class CountryDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: CountryDatabase
    private lateinit var dao: CountryDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CountryDatabase::class.java
        ).allowMainThreadQueries().build()  //multithreading is not okay with testing, so run it on main thread.
        dao = database.getCountryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertCountry() = runBlocking {
        val country = Country(id = 1, " ", "Skopje", 15450, "Europe",
        "North Macedonia", 4100, 250, 23, "43","77.3", "Europe",
        "23", 800000, 3400, 25000, "Saturday at 11am")
        dao.updateOrInsert(country)

        val allCountryItems = dao.getAllFavoritesCountries().getOrAwaitValue()

        assertThat(allCountryItems).contains(country)
    }
}