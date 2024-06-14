package com.dvt.weather.interview.presentation.ui.home

import android.Manifest
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.sharp.LocationOn
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.NEXUS_5
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.dvt.weather.interview.R
import com.dvt.weather.interview.domain.enums.ActionType
import com.dvt.weather.interview.presentation.component.WeatherScaffold
import com.dvt.weather.interview.presentation.model.CurrentWeatherViewData
import com.dvt.weather.interview.presentation.model.HourWeatherViewData
import com.dvt.weather.interview.presentation.model.factory.previewCurrentWeatherViewData
import com.dvt.weather.interview.presentation.theme.WeaposeTheme
import com.dvt.weather.interview.presentation.ui.Screen
import com.dvt.weather.interview.presentation.ui.WeatherAppState
import com.dvt.weather.interview.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun CurrentWeather(
    appState: WeatherAppState,
    viewModel: CurrentWeatherViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefresh,
        onRefresh = {
            viewModel.onRefreshCurrentWeather()
        },
    )

    val context = LocalContext.current

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    ) { permissions ->
        when {
            permissions.all { it.value } -> viewModel.getCurrentLocation()
            else -> viewModel.permissionIsNotGranted()
        }
    }

    // Get data from back
    LaunchedEffect(true) {
        appState.getDataFromNextScreen(Constants.Key.ADDRESS_NAME, "")?.collect {
            if (it.isNotBlank()) {
                viewModel.getWeatherByAddressName(addressName = it)
                appState.removeDataFromNextScreen<LatLng>(Constants.Key.ADDRESS_NAME)
            }
        }
    }

    LaunchedEffect(true) {
        appState.getDataFromNextScreen(Constants.Key.LAT_LNG, Constants.Default.LAT_LNG_DEFAULT)?.collect {
            if (it != LatLng(0.0, 0.0)) {
                viewModel.getWeatherByLocation(it)
                appState.removeDataFromNextScreen<LatLng>(Constants.Key.LAT_LNG)
            }
        }
    }

    // Locale change
    LaunchedEffect(true) {
        appState.localChange.collectLatest {
            viewModel.onRefreshCurrentWeather(false)
        }
    }

    // Get event
    LaunchedEffect(state) {
        val navigateToSearch = state.navigateSearch
        val requestPermission = state.isRequestPermission

        when {
            requestPermission -> {
                when {
                    locationPermissionState.allPermissionsGranted -> {
                        viewModel.getCurrentLocation()
                    }

                    locationPermissionState.shouldShowRationale -> {
                        viewModel.permissionIsNotGranted()
                    }

                    else -> {
                        locationPermissionState.launchMultiplePermissionRequest()
                    }
                }
            }

            navigateToSearch != null -> {
                appState.navigateToSearchByText(Screen.CurrentWeather, navigateToSearch)
            }

            else -> return@LaunchedEffect
        }
        viewModel.cleanEvent()
    }

    CurrentWeatherScreen(
        state = state,
        pullRefreshState = pullRefreshState,
        snackbarHostState = appState.snackbarHost,
        onDrawer = {
            appState.openDrawer()
        },
        onShowSnackbar = {
            appState.showSnackbar(it)
        },
        onDismissErrorDialog = {
            viewModel.hideError()
        },
        onNavigateSearch = {
            viewModel.navigateToSearchByMap()
        },
        onErrorPositiveAction = { action, _ ->
            action?.let {
                when (it) {
                    ActionType.RETRY_API -> {
                        viewModel.retry()
                    }

                    ActionType.OPEN_PERMISSION -> {
                        appState.openAppSetting(context)
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrentWeatherScreen(
    state: CurrentWeatherViewState,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onDrawer: () -> Unit = {},
    onDismissErrorDialog: () -> Unit = {},
    onShowSnackbar: (message: String) -> Unit = {},
    onNavigateSearch: () -> Unit = {},
    onErrorPositiveAction: (action: ActionType?, value: Any?) -> Unit = { _, _ -> },
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background
        state.currentWeather?.let { currentWeather ->
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Transparent)
            )
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                painter = painterResource(id = currentWeather.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
            CurrentWeatherAppBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .zIndex(1f),
                city = state.currentWeather?.city,
                onDrawer = onDrawer,
                onNavigateSearch = onNavigateSearch,
            )

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { snackbarHostState },
            ) { innerPadding ->

                WeatherScaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(top = AppBarHeight),
                    state = state,
                    snackbarHostState = snackbarHostState,
                    onDismissErrorDialog = onDismissErrorDialog,
                    onShowSnackbar = onShowSnackbar,
                    onErrorPositiveAction = onErrorPositiveAction,
                ) { _, viewState ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        viewState.currentWeather?.let {
                            HomeContent(
                                currentWeather = it,
                                listHourly = viewState.listHourlyWeatherToday,
                            )
                        }
                        PullRefreshIndicator(
                            refreshing = state.isRefresh,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }

    }


}
 private val AppBarHeight = 56.dp
@Composable
fun HomeContent(
    currentWeather: CurrentWeatherViewData,
    listHourly: List<HourWeatherViewData>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
            NowWeather(
                modifier = Modifier
                    .fillMaxSize()
                    .height(400.dp),
                currentWeather = currentWeather,
            )

            Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = "Today's Hourly Forecast",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(vertical = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        items(
                            count = listHourly.size,
                            key = {
                                listHourly[it].timeStamp
                            },
                        )
                        { index ->
                            WeatherHour(
                                hour = listHourly[index].time,
                                weather = listHourly[index].weatherIcon,
                            )
                        }
                    }
    }
}

@Composable
fun NowWeather(
    modifier: Modifier,
    currentWeather: CurrentWeatherViewData,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            painter = painterResource(id = currentWeather.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .align(Alignment.TopCenter)
                .background(Color.Transparent),
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Degrees(
                    currentWeather = currentWeather.weather,
                    currentTemp = currentWeather.temp,
                )
            }
        }
    }
}



@Composable
fun DetailWeather(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    title: String,
    description: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 5.dp),
        )

        Text(
            text = description,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    city: String? = null,
    onDrawer: () -> Unit = {},
    onNavigateSearch: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (title.isNotBlank()) {
                Text(text = title, maxLines = 1, overflow = TextOverflow.Visible)
            }
        },
        navigationIcon = {
            IconButton(onClick = onDrawer) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null,
                )
            }
        },
        actions = {
            Card(
                onClick = onNavigateSearch,
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Sharp.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 5.dp),
                    )

                    Text(
                        text = city ?: stringResource(id = R.string.unknown_address),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        },
    )
}

@Composable
fun Degrees(
    modifier: Modifier = Modifier,
    currentTemp: String,
    currentWeather: String,
    isBold: Boolean = false,
    ) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = currentTemp,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
                ),
                modifier = Modifier.alignBy(LastBaseline),
            )

            Column(modifier = Modifier.alignBy(LastBaseline)) {
                Text(text = "o", modifier = Modifier.padding(bottom = 10.dp))

                Text(text = "c")
            }
        }

        Text(
            text = currentWeather,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 22.sp),
        )
    }
}

@Composable
fun WeatherHour(
    modifier: Modifier = Modifier,
    hour: String,
    @DrawableRes weather: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hour,
            style = MaterialTheme.typography.titleMedium,
        )

        Image(
            painter = painterResource(id = weather),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(top = 15.dp),
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NowWeatherPreview() {
    WeaposeTheme {
        NowWeather(
            modifier = Modifier.size(500.dp),
            currentWeather = previewCurrentWeatherViewData(),
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DegreesPreview() {
    WeaposeTheme {
        Degrees(
            currentWeather = previewCurrentWeatherViewData().weather,
            currentTemp = previewCurrentWeatherViewData().temp,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AppBarPreview() {
    WeaposeTheme {
        CurrentWeatherAppBar(city = previewCurrentWeatherViewData().city)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(name = "Light", showBackground = true, device = NEXUS_5)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = PIXEL_4_XL)
@Composable
private fun ScreenPreview() {
    WeaposeTheme {
        CurrentWeatherScreen(
            state = CurrentWeatherViewState(),
            pullRefreshState = rememberPullRefreshState(refreshing = true, onRefresh = {})
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeatherHourPreview() {
    WeaposeTheme {
        WeatherHour(
            hour = "10:00",
            weather = R.drawable.ic_clear_sky,
        )
    }
}
