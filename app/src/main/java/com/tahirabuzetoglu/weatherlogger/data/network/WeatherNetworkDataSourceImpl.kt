package com.tahirabuzetoglu.weatherlogger.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import com.tahirabuzetoglu.weatherlogger.data.network.response.CurrentWeatherResponse
import com.tahirabuzetoglu.weatherlogger.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val openWeatherApiService: OpenWeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, units: String) {
        try {
            val fetchedCurrentWeather = openWeatherApiService
                .getCurrentWeather(location, units)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity", "No internet connection")
        }
    }
}