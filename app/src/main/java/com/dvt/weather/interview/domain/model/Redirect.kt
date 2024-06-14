package com.dvt.weather.interview.domain.model

import com.dvt.weather.interview.domain.enums.RedirectType

data class Redirect(
    val redirect: RedirectType,
    val redirectObject: Any? = null,
)
