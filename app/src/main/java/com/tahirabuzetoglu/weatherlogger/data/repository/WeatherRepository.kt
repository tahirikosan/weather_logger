package com.tahirabuzetoglu.weatherlogger.data.repository

import androidx.lifecycle.LiveData
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import org.threeten.bp.LocalDate

interface WeatherRepository {
    suspend fun getCurrentWeather(): LiveData<List<WeatherEntry>>
    suspend fun getWeathers(): LiveData<List<WeatherEntry>>
    suspend fun deleteWeather(weatherEntry: WeatherEntry): Int
    suspend fun getDetailedWeather(weatherID: String): LiveData<WeatherEntry>
}