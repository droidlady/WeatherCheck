package com.weathercheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.weathercheck.presentation.WeatherScreen
import com.weathercheck.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: WeatherViewModel = hiltViewModel()
            WeatherScreen(
                state = viewModel.state.value,
                onRetry = { viewModel.loadWeatherInfo() }
            )
        }
    }
}
