package com.dvt.weather.interview.domain.model

import com.dvt.weather.interview.domain.enums.TagType

data class Tag(
    val name: TagType,
    val message: String?,
)
