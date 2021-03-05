package com.tahirabuzetoglu.weatherlogger.data.provider

interface LocationProvider {
    suspend fun getPreferredLocationString(): String
}