package com.tahirabuzetoglu.weatherlogger.ui.weather.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepository


class DetailedWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val weatherID: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailedWeatherViewModel(weatherRepository, weatherID) as T
    }
}