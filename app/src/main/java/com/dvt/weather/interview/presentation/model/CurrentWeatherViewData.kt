package com.dvt.weather.interview.presentation.model

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.dvt.weather.interview.data.model.CurrentWeather
import com.dvt.weather.interview.presentation.utils.Constants
import com.dvt.weather.interview.presentation.utils.toBackground
import com.dvt.weather.interview.presentation.utils.toDateTime
import javax.inject.Inject

data class CurrentWeatherViewData(
    val city: String,
    val maxTemp: String,
    val minTemp: String,
    val temp: String,
    val weather: String,
    val sunRise: String,
    val wind: String,
    val humidity: String,
    val background: Int,
) : ViewData()

class CurrentWeatherMapper @Inject constructor() : DataModelMapper<CurrentWeather, CurrentWeatherViewData> {
    override fun mapToModel(viewData: CurrentWeatherViewData): CurrentWeather {
        TODO("Not yet implemented")
    }

    override fun mapToViewData(model: CurrentWeather): CurrentWeatherViewData {
        val city = model.name?.uppercase() ?: ""
        val maxTemp = model.main?.tempMax?.toString() ?: ""
        val minTemp = model.main?.tempMin?.toString() ?: ""
        val temp = model.main?.temp?.toString() ?: ""
        val weather = model.weatherItems?.firstOrNull()?.description?.capitalize(Locale.current) ?: ""
        val sunRise = model.sys?.sunrise?.toDateTime(Constants.DateFormat.HH_mm, model.timezone) ?: ""
        val wind = model.wind?.speed?.toString() ?: ""
        val humidity = model.main?.humidity?.toString() ?: ""
        val background = (model.weatherItems?.firstOrNull()?.icon ?: "").toBackground()

        return CurrentWeatherViewData(
            city = city,
            maxTemp = maxTemp,
            minTemp = minTemp,
            temp = temp,
            weather = weather,
            sunRise = sunRise,
            wind = wind,
            humidity = humidity,
            background = background,
        )
    }
}
