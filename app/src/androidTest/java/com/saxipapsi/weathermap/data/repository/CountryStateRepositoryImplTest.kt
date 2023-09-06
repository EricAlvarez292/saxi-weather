package com.saxipapsi.weathermap.data.repository

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.saxipapsi.weathermap.common.Constant
import com.saxipapsi.weathermap.data.db.MapGeoDB
import com.saxipapsi.weathermap.data.db.dao.CityDao
import com.saxipapsi.weathermap.data.db.dao.CountryStateDao
import com.saxipapsi.weathermap.data.db.entity.CountryStateEntity
import com.saxipapsi.weathermap.utility.file.RawFileLoader
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CountryStateRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var mapGeoDB: MapGeoDB
    private lateinit var cityDao: CityDao
    private lateinit var countryStateDao: CountryStateDao
    private lateinit var countryStateRepositoryImpl: CountryStateRepositoryImpl
    private val mockId = 1
    private val mockData by lazy {
        CountryStateEntity(
            mockId,
            1,
            Constant.DEFAULT_ID,
            Constant.DEFAULT_ID,
            "New York",
            "Test State Code",
            false
        )
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mapGeoDB = Room.inMemoryDatabaseBuilder(context, MapGeoDB::class.java).build()
        cityDao = mapGeoDB.cityDao()
        countryStateDao = mapGeoDB.countryStateDao()
        val rawFileLoader = RawFileLoader(context)
        countryStateRepositoryImpl = CountryStateRepositoryImpl(rawFileLoader, countryStateDao, cityDao)
    }

    @After
    fun tearDown() {
        mapGeoDB.close()
    }

    @Test
    fun loadCountryState_withoutCountryState_returnEmptyList() = runBlocking {
        /*Arrange*/
        /*Act*/
        val countryState = countryStateRepositoryImpl.getAllStates()
        val emptyData : List<CountryStateEntity> = emptyList()
        /*Assert*/
        Assert.assertEquals(emptyData, countryState)
    }


    @Test
    fun addCountryState_withCountryState_countryStateAdded() = runBlocking {
        /*Arrange*/
        insertMockData()
        /*Act*/
        val countryState = countryStateDao.loadAll()
        /*Assert*/
        Assert.assertEquals(1, countryState.size)
    }

    @Test
    fun addCountryState_withCountryState_countryStateUpdate() = runBlocking {
        /*Arrange*/
        val updateData = CountryStateEntity(
            mockId,
            1,
            Constant.DEFAULT_ID,
            Constant.DEFAULT_ID,
            "California",
            "Test State Code",
            false
        )
        countryStateDao.insert(updateData)
        /*Act*/
        val countryState = countryStateDao.loadAll()
        /*Assert*/
        Assert.assertEquals(1, countryState.size)
        Assert.assertEquals(updateData.name, countryState.first().name)
    }

    private suspend fun insertMockData() {
        countryStateDao.insert(mockData)
    }

}