package com.tahirabuzetoglu.weatherlogger.data.repository

import androidx.lifecycle.LiveData
import com.tahirabuzetoglu.weatherlogger.data.db.CurrentWeatherDao
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import com.tahirabuzetoglu.weatherlogger.data.network.WeatherNetworkDataSource
import com.tahirabuzetoglu.weatherlogger.data.network.response.CurrentWeatherResponse
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.util.*

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {


    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather, LocalDate.now())
            }
        }
    }

    override suspend fun getCurrentWeather(): LiveData<List<WeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeathers()
        }
    }

    override suspend fun getWeathers(): LiveData<List<WeatherEntry>> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getWeathers()
        }
    }

    override suspend fun deleteWeather(weatherEntry: WeatherEntry) :Int{
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.deleteWeather(weatherEntry)
        }

    }

    override suspend fun getDetailedWeather(weatherID: String): LiveData<WeatherEntry> {
        return withContext(Dispatchers.IO) {
            return@withContext currentWeatherDao.getDetailedWeather(weatherID)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse, date: LocalDate) {
        GlobalScope.launch(Dispatchers.IO) {

            val weatherEntry = WeatherEntry(
                base = fetchedWeather.base,
                clouds = fetchedWeather.clouds,
                cod = fetchedWeather.cod,
                coord = fetchedWeather.coord,
                dt = fetchedWeather.dt,
                main = fetchedWeather.main,
                sys = fetchedWeather.sys,
                timezone = fetchedWeather.timezone,
                visibility = fetchedWeather.visibility,
                weather = fetchedWeather.weather,
                wind = fetchedWeather.wind,
                date = date.toString(),
                name = fetchedWeather.name
            )
            currentWeatherDao.insert(weatherEntry)
        }
    }

    private suspend fun initWeatherData() {
        fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(
           locationProvider.getPreferredLocationString(),
            "metric"
        )
    }
}