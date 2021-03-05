package com.tahirabuzetoglu.weatherlogger.ui.weather.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.resocoder.forecastmvvm.internal.glide.GlideApp
import com.tahirabuzetoglu.weatherlogger.R
import com.tahirabuzetoglu.weatherlogger.internal.IdNotFoundException
import com.tahirabuzetoglu.weatherlogger.ui.base.ScopedFragment
import com.tahirabuzetoglu.weatherlogger.ui.weather.CurrentWeatherViewModel
import com.tahirabuzetoglu.weatherlogger.ui.weather.CurrentWeatherViewModelFactory
import com.tahirabuzetoglu.weatherlogger.util.UNIT_METRIC
import kotlinx.android.synthetic.main.detailed_weather_fragment.*
import kotlinx.android.synthetic.main.detailed_weather_fragment.imageView_condition_icon
import kotlinx.android.synthetic.main.detailed_weather_fragment.textView_condition
import kotlinx.android.synthetic.main.detailed_weather_fragment.textView_temperature
import kotlinx.android.synthetic.main.item_weathers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class DetailedWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory
            : ((String) -> DetailedWeatherViewModelFactory) by factory()
    private lateinit var viewModel: DetailedWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val safeArgs = arguments?.let { DetailedWeatherFragmentArgs.fromBundle(it) }
        val weatherID = safeArgs?.weatherID ?: throw IdNotFoundException()

        viewModel = ViewModelProviders.of(this, viewModelFactoryInstanceFactory(weatherID))
            .get(DetailedWeatherViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeather = viewModel.detailedWeather.await()

        futureWeather.observe(this@DetailedWeatherFragment, Observer {weatherEntry->
            if (weatherEntry == null) return@Observer

            updateDate(weatherEntry.date)
            updateTemperatures(weatherEntry.main.temp,
                weatherEntry.main.temp_min, weatherEntry.main.temp_max)
            updateDescription(weatherEntry.weather[0].description)
            updateHumidity(weatherEntry.main.humidity)
            updateWindSpeed(weatherEntry.wind.speed)
            updateLocation(weatherEntry.name)

            GlideApp.with(imageView_condition_icon)
                .load("http://openweathermap.org/img/wn/"+ weatherEntry.weather[0].icon+ "@2x.png")
                .into(imageView_condition_icon)
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDate(date:String) {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = date
    }

    private fun updateTemperatures(temperature: Double, min: Double, max: Double) {
        textView_temperature.text = "$temperature$UNIT_METRIC"
        textView_min_max_temperature.text = "Min: $min$UNIT_METRIC, Max: $max$UNIT_METRIC"
    }

    private fun updateDescription(description: String) {
        textView_condition.text = description
    }

    private fun updateHumidity(humidity: Double) {
        textView_precipitation.text = "Humidity: %$humidity"
    }

    private fun updateWindSpeed(windSpeed: Double) {
        textView_wind.text = "Wind speed: $windSpeed kph"
    }
}