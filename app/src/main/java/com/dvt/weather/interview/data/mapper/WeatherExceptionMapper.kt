package com.dvt.weather.interview.data.mapper

import android.content.Context
import com.dvt.weather.interview.R
import com.dvt.weather.interview.data.exception.ResponseException
import com.dvt.weather.interview.domain.enums.ActionType
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.model.AlertDialog
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherExceptionMapper @Inject constructor(@ApplicationContext private val context: Context) {
    fun mapperToWeatherException(throwable: ResponseException): Throwable = when (throwable.getKind()) {
        ResponseException.Kind.NETWORK -> WeatherException.SnackBarException(
            code = -1,
            message = context.getString(R.string.error_message_network),
        )

        ResponseException.Kind.PREFERENCE -> WeatherException.SnackBarException(
            code = -1,
            message = context.getString(R.string.error_message_preferences),
        )

        ResponseException.Kind.HTTP -> {
            val errorCode = throwable.getResponse()?.code() ?: -1
            val errorUrl = throwable.getRetrofit()?.baseUrl() ?: context.getString(R.string.invalid)

            WeatherException.AlertException(
                code = errorCode,
                alertDialog = AlertDialog(
                    title = context.getString(R.string.error_title_http),
                    message = context.getString(R.string.error_message_http, errorCode, errorUrl),
                    positiveAction = ActionType.RETRY_API,
                    positiveMessage = context.getString(R.string.retry)
                ),
            )
        }

        ResponseException.Kind.HTTP_WITH_OBJECT -> {
            val errorCode: Int = throwable.getResponse()?.code() ?: -1
            val errorUrl = throwable.getRetrofit()?.baseUrl() ?: context.getString(R.string.invalid)

            WeatherException.AlertException(
                code = errorCode,
                alertDialog = AlertDialog(
                    title = context.getString(R.string.error_title_http),
                    message = context.getString(R.string.error_message_http, errorCode, errorUrl),
                    positiveAction = ActionType.RETRY_API,
                    positiveMessage = context.getString(R.string.retry)
                ),
            )
        }

        else -> throwable
    }
}
