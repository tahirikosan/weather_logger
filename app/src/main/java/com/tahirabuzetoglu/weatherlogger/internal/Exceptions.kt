package com.tahirabuzetoglu.weatherlogger.internal

import java.io.IOException

class NoConnectivityException: IOException()
class LocationPermissionNotGrantedException: Exception()
class IdNotFoundException: Exception()