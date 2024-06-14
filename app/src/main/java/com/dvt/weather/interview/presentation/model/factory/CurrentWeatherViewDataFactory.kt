package com.dvt.weather.interview.presentation.model.factory

import com.dvt.weather.interview.R
import com.dvt.weather.interview.presentation.model.CurrentWeatherViewData

fun previewCurrentWeatherViewData() = CurrentWeatherViewData(
    city = "Ha Noi",
    maxTemp = "40",
    minTemp = "30",
    temp = "35",
    weather = "Sunny",
    sunRise = "6:00",
    wind = "10",
    humidity = "70",
    background = R.drawable.bg_clear_sky
)
