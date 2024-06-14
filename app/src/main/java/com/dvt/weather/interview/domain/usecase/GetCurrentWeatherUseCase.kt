package com.dvt.weather.interview.domain.usecase

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.dvt.weather.interview.R
import com.dvt.weather.interview.data.model.CurrentWeather
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.repositories.WeatherRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import com.dvt.weather.interview.presentation.utils.asFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentWeatherUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository,
) : FlowUseCase<GetCurrentWeatherUseCase.Params, CurrentWeather>(coroutineDispatcher) {
    override fun execute(params: Params?): Flow<CurrentWeather> = if (params != null) {
        weatherRepository.getCurrentWeatherByLocation(params.latLng)
    } else {
        WeatherException.SnackBarException(message = context.getString(R.string.error_message_lat_lng_are_invalid))
            .asFlow()
    }

    data class Params(
        val latLng: LatLng,
    )
}
