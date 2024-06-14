package com.dvt.weather.interview.presentation.base

import com.dvt.weather.interview.domain.exception.WeatherException

open class ViewState(
    open val isLoading: Boolean = false,
    open val error: WeatherException? = null,
)
