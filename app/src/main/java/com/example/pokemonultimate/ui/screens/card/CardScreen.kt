package com.example.pokemonultimate.ui.screens.card

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.pokemonCard.ColorType
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.ui.navigation.CollectionNavigation
import com.example.pokemonultimate.ui.navigation.MainNavigation
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.formatDate
import com.example.pokemonultimate.ui.utils.setFirstToUpperCase
import kotlin.math.abs


@Composable
fun CardScreen(
    jsonCard: String,
    withButtonCollection: Boolean,
    navController: NavHostController,
    viewModel: CardViewModel = viewModel()
) {
    val cardPokemon = viewModel.getCardFromJson(jsonCard)
    val checkedRotation by viewModel.checkedRotation

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Padding.MEDIUM.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageCard(cardPokemon, checkedRotation)
        LockRotationCard(checkedRotation) { isChecked ->
            viewModel.setCheckedRotation(isChecked)
        }
        TitleCard(cardPokemon)
        PriceUpdateCard(cardPokemon, viewModel)
        Column(
            modifier = Modifier.padding(
                horizontal = Padding.GIANT.dp,
                vertical = Padding.MINI.dp
            )
        ) {
            InformationCard(cardPokemon)
        }

        Row(
            modifier = Modifier
                .padding(top = Padding.MINI.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (withButtonCollection) ButtonCollection(cardPokemon,navController)
            ButtonMarketLink(cardPokemon, withButtonCollection)
        }
    }

}


@Composable
fun ImageCard(cardPokemon: PokemonCardEntity, checkedRotation: Boolean) {
    val context = LocalContext.current
    val sensorManager =
        remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    val targetPitchRotation = remember { mutableFloatStateOf(0f) } // Tilt top-bottom
    val targetRollRotation = remember { mutableFloatStateOf(0f) }  // Tilt left-right

    val pitchRotation by animateFloatAsState(
        targetValue = targetPitchRotation.floatValue * 1.2f,
        animationSpec = tween(durationMillis = 500), label = ""
    )
    val rollRotation by animateFloatAsState(
        targetValue = targetRollRotation.floatValue * 1.2f,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    DisposableEffect(checkedRotation) {
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                        val rotationMatrix = FloatArray(9)
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(rotationMatrix, orientation)

                        val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                        val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()

                        val threshold = 15f

                        targetPitchRotation.floatValue =
                            if (abs(roll) < threshold) pitch.coerceIn(-15f, 15f) else 0f
                        targetRollRotation.floatValue = roll.coerceIn(-15f, 15f)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (!checkedRotation) {
            val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            sensorManager.registerListener(
                sensorEventListener,
                rotationVectorSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            sensorManager.unregisterListener(sensorEventListener)
            targetPitchRotation.floatValue = 0f
            targetRollRotation.floatValue = 0f
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    AsyncImage(
        model = cardPokemon.images.large,
        contentDescription = cardPokemon.name,
        modifier = Modifier
            .graphicsLayer(
                rotationX = pitchRotation,
                rotationY = rollRotation,
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                cameraDistance = 48.0f
            )
    )
}


@Composable
fun LockRotationCard(checkedRotation: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = checkedRotation,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        )
        Text(
            text = stringResource(R.string.lock_card_rotation),
            modifier = Modifier.padding(start = Padding.MINI.dp)
        )
    }
}

@Composable
fun TitleCard(cardPokemon: PokemonCardEntity) {
    val colorType = ColorType.fromTypeName(cardPokemon.types?.firstOrNull()?.name ?: "")
    val gradientBrush =
        colorType?.brush ?: Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary))

    Column(
        modifier = Modifier
            .padding(bottom = Padding.MINI.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = cardPokemon.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(Padding.MINI.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(gradientBrush)
                    .padding(horizontal = Padding.MINI.dp, vertical = Padding.MICRO.dp)
            ) {
                Text(
                    text = cardPokemon.types?.firstOrNull()?.name?.setFirstToUpperCase() ?: "",
                    color = Color.White
                )
            }
        }
        Row {
            Text(text = stringResource(R.string.from) + " ")
            Text(text = cardPokemon.artist.toString(), fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun PriceUpdateCard(cardPokemon: PokemonCardEntity, viewModel: CardViewModel) {
    val englishDateFormat = cardPokemon.cardMarket?.updatedAt.toString().formatDate()
    val trendPrice = cardPokemon.cardMarket?.prices?.trendPrice
    val averageSellPrice = cardPokemon.cardMarket?.prices?.averageSellPrice
    val lowPrice = cardPokemon.cardMarket?.prices?.lowPrice
    val avg30 = cardPokemon.cardMarket?.prices?.avg30

    Box(
        modifier = Modifier
            .padding(horizontal = Padding.GIANT.dp)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding.MINI.dp, start = Padding.MINI.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = stringResource(R.string.price_last_update) + " ",
                    fontWeight = FontWeight.Bold
                )
                Text(text = englishDateFormat)
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Padding.MINI.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.trend_price),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = trendPrice.toString() + "€",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(
                            viewModel.getPriceIconAndTint(trendPrice, avg30).first
                        ),
                        tint = viewModel.getPriceIconAndTint(trendPrice, avg30).second,
                        contentDescription = stringResource(R.string.icon_price),
                        modifier = Modifier.weight(0.5f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Padding.MINI.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.average_price),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = averageSellPrice.toString() + "€",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(
                            viewModel.getPriceIconAndTint(averageSellPrice, avg30).first
                        ),
                        tint = viewModel.getPriceIconAndTint(averageSellPrice, avg30).second,
                        contentDescription = stringResource(R.string.icon_price),
                        modifier = Modifier.weight(0.5f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Padding.MINI.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.low_price),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = lowPrice.toString() + "€",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(
                            viewModel.getPriceIconAndTint(lowPrice, avg30).first
                        ),
                        tint = viewModel.getPriceIconAndTint(lowPrice, avg30).second,
                        contentDescription = stringResource(R.string.icon_price),
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun InformationCard(cardPokemon: PokemonCardEntity) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Padding.SMALL.dp, top = Padding.MINI.dp)
        ) {
            Text(
                text = stringResource(R.string.set_information),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = Padding.MICRO.dp)
            )
            Text(
                text = stringResource(R.string.number_of_cards) + " : " + cardPokemon.set.total.toString(),
                modifier = Modifier.padding(vertical = Padding.MICRO.dp)
            )
            Text(
                text = stringResource(R.string.position_in_set) + " : " + cardPokemon.number,
                modifier = Modifier.padding(vertical = Padding.MICRO.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.logo_collection) + " : ",
                    modifier = Modifier.padding(vertical = Padding.MICRO.dp)
                )
                AsyncImage(
                    model = cardPokemon.set.images.logo,
                    contentDescription = stringResource(R.string.logo_collection),
                    modifier = Modifier
                        .size(64.dp)
                )
            }
        }
    }
}

@Composable
fun ButtonCollection(cardPokemon: PokemonCardEntity, navController: NavHostController) {
    Button(
        onClick = {
            navController.navigate(MainNavigation.CardListDestination(setImage = cardPokemon.set.images.logo, setId = cardPokemon.set.id, cardFromHome = cardPokemon.id))
        }
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_catalog_unselected),
                contentDescription = stringResource(R.string.icon_collection),
            )
            Text(
                text = stringResource(R.string.button_collection),
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun ButtonMarketLink(cardPokemon: PokemonCardEntity, withButtonCollection: Boolean) {
    val context = LocalContext.current
    val marketUrl = cardPokemon.cardMarket?.url

    Button(
        onClick = {
            marketUrl.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        },
        modifier = Modifier.then(
            if (!withButtonCollection) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
            }

        )
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_storefront),
                contentDescription = stringResource(R.string.icon_store),
            )
            Text(
                text = stringResource(R.string.button_market_link),
                textAlign = TextAlign.Center,
                modifier = Modifier.then(
                    if (!withButtonCollection) {
                        Modifier.weight(5f)
                    } else {
                        Modifier
                    }

                )
            )
        }
    }
}