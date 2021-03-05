package com.tahirabuzetoglu.weatherlogger.data.db

import android.content.Context
import androidx.room.*
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry

@Database(
    entities = [WeatherEntry::class],
    version = 1
)
@TypeConverters(DataConverter::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract  fun curentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "WeatherEntries.db")
                .build()
    }
}