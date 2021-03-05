package com.tahirabuzetoglu.weatherlogger.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepository


class CurrentWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, locationProvider) as T
    }
}