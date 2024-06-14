package com.dvt.weather.interview.domain.usecase

import android.content.Context
import android.location.Address
import com.dvt.weather.interview.R
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.repositories.LocationRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import com.dvt.weather.interview.presentation.utils.asFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocationFromTextUseCase @Inject constructor(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context,
    private val locationRepository: LocationRepository,
) : FlowUseCase<GetLocationFromTextUseCase.Params, Address>(mainDispatcher) {
    override fun execute(params: Params?): Flow<Address> = if (params == null) {
        WeatherException.SnackBarException(message = context.getString(R.string.error_message_lat_lng_are_invalid))
            .asFlow()
    } else {
        locationRepository.getLocationFromText(params.text)
    }

    data class Params(
        val text: String
    )
}
