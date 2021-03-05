package com.tahirabuzetoglu.weatherlogger.ui.weather.detail


import androidx.lifecycle.ViewModel
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepository
import lazyDeferred

class DetailedWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val weatherID: String
) : ViewModel() {

    val detailedWeather by lazyDeferred {
        weatherRepository.getDetailedWeather(weatherID)
    }
}