package com.dvt.weather.interview.domain.usecase

import android.location.Address
import com.dvt.weather.interview.domain.repositories.LocationRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrentAddressUseCase @Inject constructor(
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
) : FlowUseCase<Unit, Address>(mainDispatcher) {
    override fun execute(params: Unit?): Flow<Address> = locationRepository.getCurrentAddress()
}
