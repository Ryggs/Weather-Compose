package com.dvt.weather.interview.data.di

import android.content.Context
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import com.dvt.weather.interview.data.local.datastore.PreferenceStorage
import com.dvt.weather.interview.data.local.room.HistorySearchAddressDao
import com.dvt.weather.interview.data.remote.apiservice.CurrentWeatherApiService
import com.dvt.weather.interview.data.remote.apiservice.OneCallApiService
import com.dvt.weather.interview.data.repositories.LocationRepositoryImpl
import com.dvt.weather.interview.data.repositories.UserRepositoryImpl
import com.dvt.weather.interview.data.repositories.WeatherRepositoryImpl
import com.dvt.weather.interview.domain.repositories.LocationRepository
import com.dvt.weather.interview.domain.repositories.UserRepository
import com.dvt.weather.interview.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideWeatherRepository(
        currentWeatherApiService: CurrentWeatherApiService,
        oneCallApiService: OneCallApiService,
    ): WeatherRepository = WeatherRepositoryImpl(currentWeatherApiService, oneCallApiService)

    @Provides
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        token: AutocompleteSessionToken,
        placesClient: PlacesClient,
    ): LocationRepository = LocationRepositoryImpl(context, token, placesClient)

    @Provides
    fun provideUserRepository(
        preferenceStorage: PreferenceStorage, searchAddressDao: HistorySearchAddressDao,
    ): UserRepository = UserRepositoryImpl(preferenceStorage, searchAddressDao)
}
