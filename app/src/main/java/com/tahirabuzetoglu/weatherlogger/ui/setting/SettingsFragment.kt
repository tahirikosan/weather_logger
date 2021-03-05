package com.tahirabuzetoglu.weatherlogger.ui.setting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.tahirabuzetoglu.weatherlogger.R
import com.tahirabuzetoglu.weatherlogger.data.provider.USE_DEVICE_LOCATION
import com.tahirabuzetoglu.weatherlogger.util.MY_PERMISSION_ACCESS_COARSE_LOCATION


class SettingsFragment : PreferenceFragmentCompat() {

     lateinit var locationSwitch: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null


         locationSwitch = findPreference(USE_DEVICE_LOCATION) as SwitchPreference

        if (locationSwitch != null) {
            locationSwitch.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                if(!hasLocationPermission()){
                    requestLocationPermission()
                }

                true

            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }
}