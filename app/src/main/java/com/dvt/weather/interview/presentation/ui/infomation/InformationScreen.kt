package com.dvt.weather.interview.presentation.ui.infomation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dvt.weather.interview.R
import com.dvt.weather.interview.presentation.component.WeatherScaffold
import com.dvt.weather.interview.presentation.ui.WeatherAppState

@Composable
fun Information(
    appState: WeatherAppState,
    viewModel: InformationViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    InformationScreen(
        state = state,
        snackBarHostState = appState.snackbarHost,
        onDrawer = {
            appState.openDrawer()
        },
        onShowSnackBar = {
            appState.showSnackbar(it)
        },
        onDismissDialog = {
            viewModel.hideError()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    isDarkMode: Boolean = false,
    state: InformationViewState,
    snackBarHostState: SnackbarHostState,
    onShowSnackBar: (message: String) -> Unit = {},
    onDrawer: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
) {
    WeatherScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        snackbarHostState = snackBarHostState,
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(text = "Information screen")
                },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "",
                        )
                    }
                },
            )
        },
        onShowSnackbar = onShowSnackBar,
        onDismissErrorDialog = onDismissDialog,
    ) { _, _ ->
        Column {
            Button(onClick = { /*TODO*/ }) {
                
            }

        }
        val icon = rememberSaveable(isDarkMode) {
            if (isDarkMode) {
                R.drawable.ic_dark_mode
            } else {
                R.drawable.ic_light_mode
            }
        }

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
        )

    }
}

@Composable
fun Test() {
    Surface {

    }
}