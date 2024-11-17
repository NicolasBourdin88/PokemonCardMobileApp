package com.example.pokemonultimate.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.data.model.pokemonCard.ColorType
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.setFirstToUpperCase

@Composable
fun ListFilters(
    filtersTypes: List<String>,
    filtersSubTypes: List<String>,
    filtersSuperTypes: List<String>,
    onRemoveFilter: (String) -> Unit
) {
    val filters = filtersTypes + filtersSubTypes + filtersSuperTypes
    LazyRow {
        itemsIndexed(filters) { position, filterText ->
            val modifier = if (position == 0) {
                Modifier.padding(start = Padding.GIANT.dp)
            } else {
                Modifier
            }

            GradientInputChip(
                text = filterText,
                onDismiss = {
                    onRemoveFilter.invoke(it)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun GradientInputChip(text: String, onDismiss: (String) -> Unit, modifier: Modifier) {

    val background = ColorType.fromTypeName(text)?.brush ?: Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
        )
    )

    Card(shape = RoundedCornerShape(20.dp), modifier = modifier
        .clickable {
            onDismiss.invoke(text)
        }
        .padding(all = Padding.MICRO.dp)) {
        Row(
            Modifier
                .background(brush = background)
                .padding(
                    vertical = Padding.MICRO.dp, horizontal = Padding.MEDIUM.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text.setFirstToUpperCase()
            )
            Icon(
                Icons.Default.Close,
                contentDescription = "Close button",
                Modifier
                    .size(InputChipDefaults.AvatarSize)
                    .padding(start = Padding.MINI.dp),
            )
        }
    }
}