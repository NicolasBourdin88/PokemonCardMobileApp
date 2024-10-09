package com.example.pokemonultimate.ui.screens.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.pokemonultimate.R
import com.example.pokemonultimate.ui.navigation.ButtonNavigation
import com.example.pokemonultimate.ui.navigation.MainNavigation
import com.example.pokemonultimate.ui.utils.ArrowStyle
import com.example.pokemonultimate.ui.utils.CustomTextField
import com.example.pokemonultimate.ui.utils.OrView
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir


@Composable
fun ConnectionScreen(
    navController: NavHostController,
    viewModel: ConnectionViewModel = viewModel()
) {
    val email by viewModel.email
    val password by viewModel.password
    val errorMessage by viewModel.errorConnectionMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ArrowStyle(navController)

        PikachuWithCircle()

        WelcomeBackText()

        // Utilisation de CustomTextField pour le mail utilisateur
        CustomTextField(
            label = stringResource(id = R.string.email),
            value = email,
            onValueChange = { viewModel.onEmailChangedChanged(it) },
            leadingIconRes = R.drawable.ic_user,
            isPasswordField = false
        )

        // Utilisation de CustomTextField pour le mot de passe
        CustomTextField(
            label = stringResource(id = R.string.password),
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            leadingIconRes = R.drawable.ic_password,
            isPasswordField = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth().height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Padding.HUGE.dp, vertical = 0.dp)
            )
        }

        ButtonSignin(navController, viewModel)
        OrView()
        GoogleSigninView()
        SignupView(navController)
    }

}


@Composable
fun PikachuWithCircle() {
    Box(
        modifier = Modifier
            .padding(top = Padding.HUGE.dp)
            .size(130.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clipToBounds()

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_pikachu),
            contentDescription = stringResource(id = R.string.content_logo_profil_empty),
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = -1f
                    translationY = 90f
                    translationX = -22f
                }
        )
    }
}

@Composable
fun WelcomeBackText() {
    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(Padding.MEDIUM.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome_back),
            fontSize = 36.sp, fontFamily = fontFamilyAvenir, fontWeight = FontWeight.Normal,
        )

        Text(
            text = stringResource(id = R.string.login_text),
            fontSize = 16.sp, fontFamily = fontFamilyAvenir,
            modifier = Modifier.padding(bottom = Padding.MEDIUM.dp)
        )
    }

}


@Composable
fun ButtonSignin(navController: NavHostController, viewModel: ConnectionViewModel) {

    Button(
        onClick = {
            viewModel.signInWithEmailAndPassword(viewModel.email.value,viewModel.password.value){ success ->
                if(success){
                    navController.navigate(MainNavigation.HomeDestination);
                }
            }
        },
        modifier = Modifier
            .padding(horizontal = 120.dp)
            .padding(top = 30.dp)
            .fillMaxWidth()

    ) {
        Text(
            text = stringResource(R.string.signin),
            fontFamily = fontFamilyAvenir,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun GoogleSigninView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Padding.HUGE.dp)
            .clickable {

            },
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = stringResource(id = R.string.google_icon),
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp),
            tint = Color.Unspecified
        )
        Text(
            text = stringResource(R.string.signin_google),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun SignupView(navController: NavHostController) {
    Row {
        Text(
            text = stringResource(R.string.dont_account),
            modifier = Modifier.padding(end = 8.dp),
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = stringResource(R.string.signup),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.clickable {
                navController.navigate(ButtonNavigation.InscriptionDestination.route)
            }
        )
    }
}