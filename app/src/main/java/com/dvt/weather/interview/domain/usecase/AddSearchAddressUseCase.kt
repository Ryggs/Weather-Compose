package com.dvt.weather.interview.domain.usecase

import android.content.Context
import com.dvt.weather.interview.R
import com.dvt.weather.interview.data.local.room.HistorySearchAddressEntity
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.usecase.base.SuspendUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddSearchAddressUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : SuspendUseCase<AddSearchAddressUseCase.Params, Unit>(ioDispatcher) {
    override suspend fun execute(params: Params?) {
        if (params != null) {
            userRepository.addSearchAddress(params.searchAddress)
        } else {
            throw WeatherException.SnackBarException(message = context.getString(R.string.error_message_default))
        }
    }

    data class Params(
        val searchAddress: HistorySearchAddressEntity
    )
}
