package com.tahirabuzetoglu.weatherlogger.data.provider
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.core.content.ContextCompat
import asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import com.tahirabuzetoglu.weatherlogger.internal.LocationPermissionNotGrantedException
import kotlinx.coroutines.Deferred
import java.util.*


const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {
    private val appContext = context.applicationContext


    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return "${getCustomLocationName()}"

                val geocoder: Geocoder;
                val  addresses: List<Address>
                geocoder =  Geocoder(appContext, Locale.getDefault());

                addresses = geocoder.getFromLocation(
                    deviceLocation.latitude,
                    deviceLocation.longitude,
                    1
                )
                val countryName = addresses[0].countryName

                return countryName
            } catch (e: LocationPermissionNotGrantedException) {
                return "${getCustomLocationName()}"
            }
        }
        else
            return "${getCustomLocationName()}"
    }


    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, false)
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, "Riga")
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}