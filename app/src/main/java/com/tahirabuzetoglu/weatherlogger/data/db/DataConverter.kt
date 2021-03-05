package com.tahirabuzetoglu.weatherlogger.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tahirabuzetoglu.weatherlogger.data.db.entity.Weather
import java.lang.reflect.Type


public class DataConverter  {

    @TypeConverter // note this annotation
    fun fromWeatherList(optionValues: List<Weather>): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather>>() {}.getType()
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    fun toWeatherList(optionValuesString: String?): List<Weather>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Weather>>() {}.getType()
        return gson.fromJson<List<Weather>>(optionValuesString, type)
    }
}