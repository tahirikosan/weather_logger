package com.tahirabuzetoglu.weatherlogger.data.db.entity

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Double,
    val sunset: Double,
    val type: Int
)