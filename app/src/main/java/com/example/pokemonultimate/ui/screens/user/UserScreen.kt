package com.example.pokemonultimate.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.userModel.UserProfile
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel
import com.example.pokemonultimate.ui.screens.inscription.InscriptionViewModel

@Composable
fun UserScreen(userId: String, viewModel: AuthViewModel) {
    Column(

    ){
        Text("hello"+userId)
    }
}

@Composable
fun ProfilePicture(viewModel: InscriptionViewModel, onButtonClick: () -> Unit, ) {
    val selectedImage by viewModel.selectedImage

    Box(
        modifier = Modifier
            .size(150.dp)
            .offset(y = 20.dp)
    ) {
        if (selectedImage == null) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.Center)
                    .clipToBounds()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pikachu),
                    contentDescription = stringResource(id = R.string.content_logo_profil_empty),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = -1f
                            translationY = 90f
                            translationX = -22f
                        }
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(brush = selectedImage!!.brush)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = selectedImage!!.pokemonCellImage),
                    contentDescription = stringResource(id = R.string.selected_image),
                    modifier = Modifier.size(96.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-16).dp, y = (-12).dp)
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_plus),
                contentDescription = stringResource(id = R.string.plus_icon),
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable {
                        onButtonClick()
                    }
            )
        }
    }
}
/*
@Composable
fun ScanCollection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Scan collection",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Scan your friend's collection",
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.your_image),
                contentDescription = "Scan Illustration",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}*/

