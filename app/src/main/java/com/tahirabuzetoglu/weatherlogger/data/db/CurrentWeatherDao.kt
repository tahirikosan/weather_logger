package com.tahirabuzetoglu.weatherlogger.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry

@Dao
interface CurrentWeatherDao {

    @Insert
    fun insert(weatherEntry: WeatherEntry)

    @Query("select * from weather order by id desc")
    fun getWeathers(): LiveData<List<WeatherEntry>>

    @Query("select * from weather where id = :weatherID")
    fun getDetailedWeather(weatherID: String): LiveData<WeatherEntry>

    @Delete
    fun deleteWeather(weatherEntry: WeatherEntry): Int

}