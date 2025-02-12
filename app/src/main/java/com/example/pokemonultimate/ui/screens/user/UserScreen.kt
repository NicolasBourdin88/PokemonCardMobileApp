package com.example.pokemonultimate.ui.screens.user


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.utils.calculateUserStats
import com.example.pokemonultimate.data.utils.getUserCards
import com.example.pokemonultimate.ui.screens.authentification.AuthViewModel
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir

@Composable
fun UserScreen(viewModel: AuthViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(viewModel)
        Row {
            StatsUser()
            Column {
                ScanQRCodeFriend(navController)
                ShowQRCode()
            }
        }
        ButtonLogOut(viewModel)
    }
}

@Composable
fun ProfileImage(viewModel: AuthViewModel) {
    val userProfileImage by viewModel.userProfileImage.collectAsState()
    Column(
        modifier = Modifier.padding(bottom = Padding.MASSIVE.dp),
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(y = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            userProfileImage?.let { profile ->
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(brush = profile.brush),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = profile.pokemonCellImage),
                        contentDescription = stringResource(id = R.string.selected_image),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun StatsUser() {
    var totalCards by remember { mutableStateOf(0) }
    var completedExpansions by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        getUserCards { userCards, _ ->
            val (cardsCount, setsCount) = calculateUserStats(userCards)
            totalCards = cardsCount
            completedExpansions = setsCount
        }
    }

    Column(
        modifier = Modifier.padding(end = Padding.MEDIUM.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 165.dp, height = 266.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(Padding.MINI.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.stats),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = fontFamilyAvenir,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
                Text(
                    text = "$totalCards cards",
                    fontSize = 12.sp,
                    fontFamily = fontFamilyAvenir,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
                Text(
                    text = "$completedExpansions completed expansion",
                    fontSize = 12.sp,
                    fontFamily = fontFamilyAvenir,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
    }
}


@Composable
fun ShowQRCode() {
    Box(
        modifier = Modifier
            .size(width = 165.dp, height = 125.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(Padding.MINI.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.show_qr_code),
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = fontFamilyAvenir,
                color = MaterialTheme.colorScheme.surfaceTint
            )
            Text(
                text = stringResource(R.string.show_qr_code_description),
                fontSize = 12.sp,
                fontFamily = fontFamilyAvenir,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.showqrcode),
                contentDescription = "QR Code Illustration",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(110.dp)
                    .graphicsLayer(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ScanQRCodeFriend(navController: NavController) {
    Column(
        modifier = Modifier.padding(bottom = Padding.MEDIUM.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 165.dp, height = 125.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(Padding.MINI.dp)
                .clickable {
                    //nav
                },
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.scan_collection),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = fontFamilyAvenir,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
                Text(
                    text = stringResource(R.string.scan_collection_description),
                    fontSize = 12.sp,
                    fontFamily = fontFamilyAvenir,
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.scanqrcode),
                    contentDescription = "Scan QR Code Illustration",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(110.dp)
                        .graphicsLayer(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun ButtonLogOut(viewModel: AuthViewModel) {
    Button(
        onClick = {

        },
        modifier = Modifier
            .width(160.dp)
            .padding(top = Padding.HUGE.dp),
    ) {
        Text(
            text = stringResource(R.string.logout),
            fontFamily = fontFamilyAvenir,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

    }
}
