package com.tahirabuzetoglu.weatherlogger.data.network.response
import com.tahirabuzetoglu.weatherlogger.data.db.entity.*

data class CurrentWeatherResponse(
        val base: String,
        val clouds: Clouds,
        val cod: Double,
        val coord: Coord,
        val dt: Double,
        val main: Main,
        val name: String,
        val sys: Sys,
        val timezone: Double,
        val visibility: Double,
        val weather: List<Weather>,
        val wind: Wind
)