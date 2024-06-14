package com.dvt.weather.interview.domain.repositories

import com.google.android.gms.maps.model.LatLng
import com.dvt.weather.interview.data.model.CurrentWeather
import com.dvt.weather.interview.data.model.OneCallResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeatherByCity(city: String): Flow<CurrentWeather>

    fun getCurrentWeatherByLocation(latLng: LatLng): Flow<CurrentWeather>

    fun getHourWeather(latLng: LatLng): Flow<OneCallResponse>

    fun getSevenDaysWeather(latLng: LatLng): Flow<OneCallResponse>
}
