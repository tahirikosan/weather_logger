package com.tahirabuzetoglu.weatherlogger.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tahirabuzetoglu.weatherlogger.data.db.DataConverter
import com.tahirabuzetoglu.weatherlogger.data.db.entity.*

@Entity(tableName = "weather")
data class WeatherEntry(
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Double,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Double,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val timezone: Double,
    val visibility: Double,
    @TypeConverters(DataConverter::class)
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind,
    val date: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
