package com.dvt.weather.interview.data.di

import com.dvt.weather.interview.BuildConfig
import com.dvt.weather.interview.data.remote.apiservice.CurrentWeatherApiService
import com.dvt.weather.interview.data.remote.apiservice.OneCallApiService
import com.dvt.weather.interview.data.remote.calladapter.FlowCallAdapterFactory
import com.dvt.weather.interview.data.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(TIME_OUT, TimeUnit.MINUTES)
        .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(headerInterceptor)
        .build()

    @Provides
    @Named("OneCallRetrofit")
    fun provideOneCallRetrofit(
        flowCallAdapterFactory: FlowCallAdapterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ONECALL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(flowCallAdapterFactory)
        .build()

    @Provides
    @Named("CurrentWeatherRetrofit")
    fun provideWeatherRetrofit(
        flowCallAdapterFactory: FlowCallAdapterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(flowCallAdapterFactory)
        .build()

    @Provides
    fun provideOneCallApiService(@Named("OneCallRetrofit") retrofit: Retrofit): OneCallApiService =
        retrofit.create(OneCallApiService::class.java)

    @Provides
    fun provideCurrentWeatherApiService(@Named("CurrentWeatherRetrofit") retrofit: Retrofit): CurrentWeatherApiService =
        retrofit.create(CurrentWeatherApiService::class.java)

    private const val TIME_OUT = 1L
}
