package com.example.pokemonultimate.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.pokemonultimate.MainActivity
import com.example.pokemonultimate.R
import kotlinx.coroutines.delay

const val SPLASH_SCREEN_DURATION: Long = 2000L

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = null) {
        delay(SPLASH_SCREEN_DURATION)
        navController.context.startActivity(Intent(navController.context, MainActivity::class.java))

        // Used to avoid user to return on SplashScreen
        (navController.context as? Activity)?.finish()
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        LogoPokemon()
        BottomAnimation()
    }
}

@Composable
private fun LogoPokemon() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.pokemon_logo),
            contentDescription = "Logo pokemon",
        )
    }
}

@Composable
private fun BottomAnimation() {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.animation_pikachu)
    )

    LottieAnimation(
        composition = preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        speed = 0.8F,
    )
}
