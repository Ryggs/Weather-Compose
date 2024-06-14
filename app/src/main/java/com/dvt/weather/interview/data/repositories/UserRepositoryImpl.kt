package com.dvt.weather.interview.data.repositories

import com.dvt.weather.interview.data.local.datastore.PreferenceStorage
import com.dvt.weather.interview.data.local.room.HistorySearchAddressDao
import com.dvt.weather.interview.data.local.room.HistorySearchAddressEntity
import com.dvt.weather.interview.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val searchAddressDao: HistorySearchAddressDao,
) : UserRepository {
    override suspend fun setDarkModeGoogleMap(isDarkMode: Boolean) = preferenceStorage.setDarkModeGoogleMap(isDarkMode)

    override val isDarkModeGoogleMap: Flow<Boolean> = preferenceStorage.isDarkModeGoogleMap

    override suspend fun addSearchAddress(historySearchAddressEntity: HistorySearchAddressEntity) {
        searchAddressDao.insertOrUpdate(historySearchAddressEntity)
    }

    override suspend fun clearAllSearchAddress() = searchAddressDao.deleteAll()

    override fun getSearchAddress(): Flow<List<HistorySearchAddressEntity>> = searchAddressDao.getAll()
}
