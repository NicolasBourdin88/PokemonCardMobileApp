package com.example.pokemonultimate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.theme.cardElectricFirstColor
import com.example.pokemonultimate.ui.theme.cardElectricSecondColor
import com.example.pokemonultimate.ui.theme.cardFireFirstColor
import com.example.pokemonultimate.ui.theme.cardFireSecondColor
import com.example.pokemonultimate.ui.theme.cardPlantFirstColor
import com.example.pokemonultimate.ui.theme.cardPlantSecondColor
import com.example.pokemonultimate.ui.theme.cardWaterFirstColor
import com.example.pokemonultimate.ui.theme.cardWaterSecondColor
import com.example.pokemonultimate.ui.utils.TitleText
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Column {
        var text by remember {
            mutableStateOf("")
        }
        var expanded by rememberSaveable { mutableStateOf(false) }
        TitleText("What Are You Looking For", modifier = Modifier.padding(start = 8.dp))

        SearchBar(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .semantics { traversalIndex = 0f }
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search card") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            colors = SearchBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                dividerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {}

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(CELL.entries.size) { position ->
                val cell = CELL.entries[position]
                Box {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(196.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = cell.brush)
                                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                cell.name.lowercase().replaceFirstChar { it.uppercase() },
                                modifier = Modifier.weight(1F),
                                color = Color.White,
                                fontFamily = fontFamilyAvenir,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                            )
                            Icon(
                                painter = painterResource(R.drawable.arrow_right_card),
                                contentDescription = "Direction arrow",
                                tint = Color.White
                            )
                        }
                    }

                }
            }
        }
    }
}

private enum class CELL(val color: Color, val brush: Brush) {
    FIRE(
        color = Color.Red,
        brush = Brush.linearGradient(listOf(cardFireFirstColor, cardFireSecondColor)),
    ),
    PLANT(
        color = Color.Red,
        brush = Brush.linearGradient(listOf(cardPlantFirstColor, cardPlantSecondColor)),
    ),
    ELECTRIC(
        color = Color.Red,
        brush = Brush.linearGradient(listOf(cardElectricFirstColor, cardElectricSecondColor)),
    ),
    WATER(
        color = Color.Red,
        brush = Brush.linearGradient(listOf(cardWaterFirstColor, cardWaterSecondColor)),
    ),
}
