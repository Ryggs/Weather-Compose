package com.dvt.weather.interview.domain.usecase

import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDarkModeGoogleMapUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, Boolean>(ioDispatcher) {
    override fun execute(params: Unit?): Flow<Boolean> = userRepository.isDarkModeGoogleMap
}
