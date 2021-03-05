package com.tahirabuzetoglu.weatherlogger

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tahirabuzetoglu.weatherlogger.data.db.WeatherDatabase
import com.tahirabuzetoglu.weatherlogger.data.network.*
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProvider
import com.tahirabuzetoglu.weatherlogger.data.provider.LocationProviderImpl
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepository
import com.tahirabuzetoglu.weatherlogger.data.repository.WeatherRepositoryImpl
import com.tahirabuzetoglu.weatherlogger.ui.weather.CurrentWeatherViewModelFactory
import com.tahirabuzetoglu.weatherlogger.ui.weather.detail.DetailedWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class WeatherApplication: Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().curentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { OpenWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from factory {weatherID: String -> DetailedWeatherViewModelFactory(instance(), weatherID) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

}