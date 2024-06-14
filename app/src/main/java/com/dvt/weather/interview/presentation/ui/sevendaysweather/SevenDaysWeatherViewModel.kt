package com.dvt.weather.interview.presentation.ui.sevendaysweather

import com.google.android.gms.maps.model.LatLng
import com.dvt.weather.interview.domain.exception.WeatherException
import com.dvt.weather.interview.domain.usecase.GetAddressFromLocationUseCase
import com.dvt.weather.interview.domain.usecase.GetCurrentAddressUseCase
import com.dvt.weather.interview.domain.usecase.GetLocationFromTextUseCase
import com.dvt.weather.interview.domain.usecase.GetSevenDaysWeatherUseCase
import com.dvt.weather.interview.presentation.base.BaseViewModel
import com.dvt.weather.interview.presentation.base.ViewState
import com.dvt.weather.interview.presentation.model.DayWeatherViewData
import com.dvt.weather.interview.presentation.model.SevenWeatherViewDataMapper
import com.dvt.weather.interview.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SevenDaysWeatherViewModel @Inject constructor(
    private val getCurrentAddressUseCase: GetCurrentAddressUseCase,
    private val getSevenDaysWeatherUseCase: GetSevenDaysWeatherUseCase,
    private val sevenWeatherViewDataMapper: SevenWeatherViewDataMapper,
    private val getLocationFromTextUseCase: GetLocationFromTextUseCase,
    private val getAddressFromLocationUseCase: GetAddressFromLocationUseCase,
) : BaseViewModel() {
    private val _state = MutableStateFlow(SevenDaysViewState(isLoading = true))
    val state: StateFlow<SevenDaysViewState> = _state

    private var currentLocation = Constants.Default.LAT_LNG_DEFAULT

    init {
        getCurrentWeather()
    }

    fun getWeatherByAddressName(addressName: String) {
        retryViewModelScope {
            showLoading()

            _state.update {
                it.copy(address = addressName)
            }

            getLocationFromTextUseCase(GetLocationFromTextUseCase.Params(addressName)).collect { address ->
                val latLng = LatLng(address.latitude, address.longitude)
                getSevenDaysWeather(latLng)
            }
        }
    }

    fun getWeatherByLocation(latLng: LatLng) {
        retryViewModelScope {
            showLoading()

            getAddressFromLocationUseCase.invoke(GetAddressFromLocationUseCase.Params(latLng)).collect { address ->
                _state.update {
                    it.copy(
                        address = address.featureName
                    )
                }

                getSevenDaysWeather(latLng)
            }
        }
    }

    fun onClickExpandedItem(item: DayWeatherViewData) {
        retryViewModelScope {
            val listSevenDays = _state.value.listSevenDays.toMutableList()

            if (listSevenDays.isNotEmpty()) {
                val indexItem = listSevenDays.indexOfFirst {
                    item.dateTime == it.dateTime
                }

                if (indexItem != -1) {
                    listSevenDays[indexItem] = listSevenDays[indexItem].copy(
                        isExpanded = !listSevenDays[indexItem].isExpanded,
                    )
                    _state.update {
                        it.copy(listSevenDays = listSevenDays)
                    }
                }
            }
        }
    }

    private fun getCurrentWeather() {
        retryViewModelScope {
            showLoading()

            getCurrentAddressUseCase().collect { address ->
                _state.update {
                    it.copy(
                        address = address.featureName
                    )
                }

                val latLng = LatLng(address.latitude, address.longitude)
                getSevenDaysWeather(latLng)
            }
        }
    }

    private fun getSevenDaysWeather(latLng: LatLng) {
        retryViewModelScope {
            currentLocation = latLng

            getSevenDaysWeatherUseCase(GetSevenDaysWeatherUseCase.Params(latLng)).collect { response ->
                val listSevenDays = response.daily?.map { sevenWeatherViewDataMapper.mapToViewData(it) }
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefresh = false,
                        listSevenDays = listSevenDays ?: emptyList(),
                    )
                }
            }
        }
    }

    fun onNavigateToSearch() {
        _state.update {
            it.copy(navigateToSearchByText = currentLocation)
        }
    }

    fun onRefresh(isShowRefresh: Boolean = true) {
        retryViewModelScope {
            _state.update {
                it.copy(
                    isRefresh = isShowRefresh,
                    isLoading = false,
                )
            }

            getSevenDaysWeather(currentLocation)
        }
    }

    override fun showError(error: WeatherException) {
        if (_state.value.error == null) {
            _state.update {
                it.copy(isLoading = false, error = error)
            }
        }
    }

    private fun showLoading() {
        _state.update {
            it.copy(
                isLoading = true,
                listSevenDays = emptyList(),
            )
        }
    }

    override fun hideLoading() {
        _state.update {
            it.copy(isLoading = false)
        }
    }

    override fun hideError() {
        _state.update {
            it.copy(isLoading = false, error = null)
        }
    }

    fun cleanEvent() {
        _state.update {
            it.copy(
                navigateToSearchByText = null,
            )
        }
    }
}

data class SevenDaysViewState(
    override val isLoading: Boolean = false,
    override val error: WeatherException? = null,
    val isRefresh: Boolean = false,
    val address: String = "",
    val listSevenDays: List<DayWeatherViewData> = emptyList(),
    val navigateToSearchByText: LatLng? = null,
) : ViewState(isLoading, error)
