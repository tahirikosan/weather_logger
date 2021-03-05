package com.tahirabuzetoglu.weatherlogger.ui.weather


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import com.tahirabuzetoglu.weatherlogger.data.network.response.CurrentWeatherResponse
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepository
import lazyDeferred
import androidx.databinding.Observable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel(), Observable  {

    lateinit var weatherEntry: WeatherEntry

    val weathers by lazyDeferred {
        weatherRepository.getWeathers()
    }

    val location by lazyDeferred {
        locationProvider.getPreferredLocationString()
    }

    fun getCurrentWeather() = viewModelScope.launch {
        weatherRepository.getCurrentWeather()
    }

    fun deleteWeather() = viewModelScope.launch {
        weatherRepository.deleteWeather(weatherEntry)
    }



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }


}