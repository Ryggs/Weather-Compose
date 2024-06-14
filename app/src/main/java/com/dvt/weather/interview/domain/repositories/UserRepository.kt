package com.dvt.weather.interview.domain.repositories

import com.dvt.weather.interview.data.local.room.HistorySearchAddressEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun setDarkModeGoogleMap(isDarkMode: Boolean)

    val isDarkModeGoogleMap: Flow<Boolean>

    fun getSearchAddress(): Flow<List<HistorySearchAddressEntity>>

    suspend fun addSearchAddress(historySearchAddressEntity: HistorySearchAddressEntity)

    suspend fun clearAllSearchAddress()
}
