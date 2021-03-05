package com.tahirabuzetoglu.weatherlogger.data.network

import androidx.lifecycle.LiveData
import com.tahirabuzetoglu.weatherlogger.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units: String
    )

}