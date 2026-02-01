package com.weathercheck.presentation

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weathercheck.data.model.MyWeather
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    state: WeatherState,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
            }
        }
        textToSpeech.language = Locale.getDefault()
        tts = textToSpeech

        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    val speakWeather: () -> Unit = {
        state.weatherData?.let { weather ->
            val location = weather.location?.name ?: "Unknown location"
            val temp = weather.current?.temperature ?: 0
            val desc = weather.current?.weatherDescriptions?.getOrNull(0) ?: ""
            val text = "Wishing you good vibes always!! Current weather in $location is $desc with a temperature of $temp degrees Celsius."
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Weather Check") },
                actions = {
                    if (state.weatherData != null && isTtsReady) {
                        IconButton(onClick = speakWeather) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Read weather"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.error, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text(text = "Retry")
                    }
                }
            } else {
                state.weatherData?.let { weather ->
                    Column(modifier = Modifier.fillMaxSize()) {
                        CurrentWeatherHeader(weather)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherHeader(weather: MyWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.location?.name ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = weather.current?.observationTime ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            val iconUrl = weather.current?.weatherIcons?.getOrNull(0)
            AsyncImage(
                model = iconUrl,
                contentDescription = "Weather Icon",
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "${weather.current?.temperature ?: 0}°C",
                fontSize = 48.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = weather.current?.weatherDescriptions?.getOrNull(0) ?: "",
                fontSize = 18.sp
            )
            weather.current?.feelslike?.let {
                Text(
                    text = "Feels Like $it°C"
                )
            }
        }
    }
}
