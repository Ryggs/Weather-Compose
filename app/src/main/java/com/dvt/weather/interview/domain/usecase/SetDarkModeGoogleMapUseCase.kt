package com.dvt.weather.interview.domain.usecase

import android.content.Context
import com.dvt.weather.interview.R
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.usecase.base.SuspendUseCase
import com.dvt.weather.interview.presentation.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetDarkModeGoogleMapUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : SuspendUseCase<SetDarkModeGoogleMapUseCase.Params, Unit>(ioDispatcher) {
    override suspend fun execute(params: Params?) {
        if (params == null) {
            throw WeatherException.SnackBarException(message = context.getString(R.string.error_message_default))
        } else {
            userRepository.setDarkModeGoogleMap(params.isDarkMode)
        }
    }

    data class Params(
        val isDarkMode: Boolean,
    )
}
