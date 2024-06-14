package com.dvt.weather.interview.domain.usecase

import android.content.Context
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.dvt.weather.interview.R
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.repositories.LocationRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.IoDispatcher
import com.dvt.weather.interview.presentation.utils.asFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAddressFromTextUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
) : FlowUseCase<GetAddressFromTextUseCase.Params, List<AutocompletePrediction>>(ioDispatcher) {
    override fun execute(params: Params?): Flow<List<AutocompletePrediction>> = if (params == null) {
        WeatherException.SnackBarException(message = context.getString(R.string.error_message_address_is_not_found))
            .asFlow()
    } else {
        locationRepository.getAddress(params.text)
    }

    data class Params(
        val text: String,
    )
}
