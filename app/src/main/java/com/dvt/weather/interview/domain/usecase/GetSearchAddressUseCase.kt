package com.dvt.weather.interview.domain.usecase

import com.dvt.weather.interview.data.local.room.HistorySearchAddressEntity
import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.usecase.base.FlowUseCase
import com.dvt.weather.interview.presentation.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchAddressUseCase @Inject constructor(
    @MainDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : FlowUseCase<Unit, List<HistorySearchAddressEntity>>(ioDispatcher) {
    override fun execute(params: Unit?): Flow<List<HistorySearchAddressEntity>> = userRepository.getSearchAddress()
}
