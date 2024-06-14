package com.dvt.weather.interview.data.repositories

import androidx.compose.ui.text.intl.Locale
import com.google.android.gms.maps.model.LatLng
import com.dvt.weather.interview.BuildConfig
import com.dvt.weather.interview.data.model.CurrentWeather
import com.dvt.weather.interview.data.model.OneCallResponse
import com.dvt.weather.interview.data.remote.apiservice.CurrentWeatherApiService
import com.dvt.weather.interview.data.remote.apiservice.OneCallApiService
import com.dvt.weather.interview.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val currentWeatherApiService: CurrentWeatherApiService,
    private val oneCallApiService: OneCallApiService,
) : WeatherRepository {
    override fun getCurrentWeatherByCity(city: String): Flow<CurrentWeather> =
        currentWeatherApiService.getCurrentWeatherByCity(
            city = city,
            lang = Locale.current.language,
            appId = BuildConfig.API_KEY,
        )

    override fun getCurrentWeatherByLocation(latLng: LatLng): Flow<CurrentWeather> =
        currentWeatherApiService.getCurrentWeatherByLocation(
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            lang = Locale.current.language,
            appId = BuildConfig.API_KEY,
        )

    override fun getHourWeather(latLng: LatLng): Flow<OneCallResponse> = oneCallApiService.getWeather(
        lat = latLng.latitude,
        lon = latLng.longitude,
        lang = Locale.current.language,
        appId = BuildConfig.API_KEY,
    )

    override fun getSevenDaysWeather(latLng: LatLng): Flow<OneCallResponse> = oneCallApiService.getSevenDaysWeather(
        lat = latLng.latitude,
        lon = latLng.longitude,
        lang = Locale.current.language,
        appId = BuildConfig.API_KEY,
    )
}
