package com.dvt.weather.interview.data.remote.response

import com.google.gson.annotations.SerializedName

data class ServerErrorResponse(
    @SerializedName("cod") val code: Int? = 0,
    @SerializedName("message") val message: String? = "",
)
