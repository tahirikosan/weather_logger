package com.tahirabuzetoglu.weatherlogger.ui.weather

import android.graphics.PorterDuff
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View

import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.tahirabuzetoglu.weatherlogger.R
import com.tahirabuzetoglu.weatherlogger.data.db.entity.WeatherEntry
import com.tahirabuzetoglu.weatherlogger.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel

    private lateinit var weatherEntryList: List<WeatherEntry>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CurrentWeatherViewModel::class.java)

        bindUI()
    }


    private fun saveCurrentWeather() = launch {
        viewModel.getCurrentWeather()
    }

    private fun deleteWeather(weatherEntry: WeatherEntry) = launch {
        viewModel.weatherEntry = weatherEntry
        viewModel.deleteWeather()
    }

    private fun bindUI() = launch {
        val weathers = viewModel.weathers.await()
        weathers.observe(this@CurrentWeatherFragment, Observer {
            if(it == null) return@Observer

            weatherEntryList = it
            group_loading.visibility = View.GONE
            initRecyclerView(it.toWeatherItems())
        })

        val location = viewModel.location
        updateLocation(location = location.await())

        clearSubtitle()

        imageView_save.setOnClickListener {
            YoYo.with(Techniques.Bounce)
                .duration(700)
                .playOn(imageView_save)

            group_loading.visibility = View.VISIBLE
            saveCurrentWeather()
        }

    }

    private fun initRecyclerView(items: List<WeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CurrentWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? WeatherItem)?.let {
                 showWeatherDetail(it.weatherEntry.id.toString(), view)
            }
        }

        val swipeHandler = object : SwipeToDeleteCallback(context!!) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                deleteWeather(weatherEntryList.get(viewHolder.adapterPosition))
                groupAdapter.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun List<WeatherEntry>.toWeatherItems() : List<WeatherItem> {
        return this.map {
            WeatherItem(it)
        }
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun showWeatherDetail(weatherID: String, view: View) {
        val actionDetail = CurrentWeatherFragmentDirections.actionDetail(weatherID)
        Navigation.findNavController(view).navigate(actionDetail)
    }

    private fun clearSubtitle(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = ""
    }


}