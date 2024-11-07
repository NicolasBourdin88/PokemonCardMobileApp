package com.example.pokemonultimate.ui.screens.card

import android.content.Intent
import android.graphics.ColorFilter
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.pokemonCard.PokemonCardEntity
import com.example.pokemonultimate.ui.utils.Padding


@Composable
fun CardScreen(
    jsonCard: String,
    withButtonCollection: Boolean,
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
        PriceUpdateCard(cardPokemon)
        Column(
            modifier = Modifier.padding(
                horizontal = Padding.GIANT.dp,
                vertical = Padding.MINI.dp
            )
        ) {
            InformationCard(cardPokemon)
        }

        Row {
            if (withButtonCollection) ButtonCollection()
            ButtonMarketLink(cardPokemon)
        }
    }

}


@Composable
fun ImageCard(cardPokemon: PokemonCardEntity, checkedRotation: Boolean) {
    AsyncImage(model = cardPokemon.images.large, contentDescription = cardPokemon.name)
}

@Composable
fun LockRotationCard(checkedRotation: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row {
        Switch(
            checked = checkedRotation,
            onCheckedChange = { onCheckedChange(it) }
        )
        Text(text = stringResource(R.string.lock_card_rotation))
    }
}

@Composable
fun TitleCard(cardPokemon: PokemonCardEntity) {
    Column {
        Row {
            Text(text = cardPokemon.name)
            Text(text = cardPokemon.types.toString())
        }
        Row {
            Text(text = stringResource(R.string.from))
            Text(text = cardPokemon.artist.toString())
        }
    }
}

@Composable
fun PriceUpdateCard(cardPokemon: PokemonCardEntity) {
    Column() {
        Row {
            Text(text = stringResource(R.string.price_last_update))
            Text(text = cardPokemon.tcgPlayer?.updatedAt.toString())
        }
        Row {
            Text(text = stringResource(R.string.market_price))
            Text(text = cardPokemon.tcgPlayer?.prices?.prices?.market.toString())
        }
        Row {
            Text(text = stringResource(R.string.mid_price))
            Text(text = cardPokemon.tcgPlayer?.prices?.prices?.mid.toString())
        }
        Row {
            Text(text = stringResource(R.string.low_price))
            Text(text = cardPokemon.tcgPlayer?.prices?.prices?.low.toString())
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
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(64.dp)
                )
            }
        }

    }
}

@Composable
fun ButtonCollection() {
    Button(
        onClick = {

        }
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.ic_catalog_unselected),
                contentDescription = "Icon Collection"
            )
            Text(text = stringResource(R.string.button_collection))
        }

    }
}

@Composable
fun ButtonMarketLink(cardPokemon: PokemonCardEntity) {
    val context = LocalContext.current
    val marketUrl = cardPokemon.tcgPlayer?.url

    Button(
        onClick = {
            marketUrl.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                context.startActivity(intent)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                painter = painterResource(R.drawable.ic_storefront),
                contentDescription = "Icon store",
            )
            Text(
                text = stringResource(R.string.button_market_link),
                textAlign = TextAlign.Center,
            )
        }
    }
}