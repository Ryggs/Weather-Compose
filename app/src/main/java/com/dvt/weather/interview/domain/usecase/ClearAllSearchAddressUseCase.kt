package com.dvt.weather.interview.domain.usecase

import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.usecase.base.SuspendUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearAllSearchAddressUseCase @Inject constructor(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : SuspendUseCase<Unit, Unit>(mainDispatcher) {
    override suspend fun execute(params: Unit?) = userRepository.clearAllSearchAddress()
}
