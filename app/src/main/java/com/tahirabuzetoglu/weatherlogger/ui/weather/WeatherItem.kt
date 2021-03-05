package com.tahirabuzetoglu.weatherlogger.ui.weather
import android.icu.util.LocaleData
import com.resocoder.forecastmvvm.internal.glide.GlideApp
import com.tahirabuzetoglu.weatherlogger.R
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_weathers.*



class WeatherItem(
    val weatherEntry: WeatherEntry
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            updateDate()
            updateTemperature()
            updateWeatherCondition()
            updateConditionImage()
            textView_more_details.text = "more details"
        }
    }

    override fun notifyChanged() {
        super.notifyChanged()
    }

    override fun getLayout() = R.layout.item_weathers

    private fun ViewHolder.updateDate() {
        textView_date.text = weatherEntry.date
    }

    private fun ViewHolder.updateTemperature() {
        textView_temperature.text = weatherEntry.main.temp.toString() + "°C"
    }

    private fun ViewHolder.updateWeatherCondition() {
        textView_condition.text = weatherEntry.weather.get(0).description
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("http://openweathermap.org/img/wn/"+ weatherEntry.weather[0].icon+ "@2x.png")
            .into(imageView_condition_icon)
    }


}