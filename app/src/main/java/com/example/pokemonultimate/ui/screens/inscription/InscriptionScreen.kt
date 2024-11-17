package com.example.pokemonultimate.ui.screens.inscription

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokemonultimate.R
import com.example.pokemonultimate.data.model.PokemonCellProfile
import com.example.pokemonultimate.ui.navigation.AuthenticationNavigation
import com.example.pokemonultimate.ui.utils.CustomTextField
import com.example.pokemonultimate.ui.utils.SeparatorAuthenticationOption
import com.example.pokemonultimate.ui.utils.Padding
import com.example.pokemonultimate.ui.utils.fontFamilyAvenir

@Composable
fun InscriptionScreen(
    navController: NavHostController,
    viewModel: InscriptionViewModel,
    onSuccess: () -> Unit,
) {
    val email by viewModel.email
    val password by viewModel.password
    val confirmPassword by viewModel.confirmPassword
    val errorMessage by viewModel.errorInscriptionMessage
    val showBottomSheet by viewModel.showBottomSheet

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileWithButtonPlus(viewModel) { viewModel.toggleBottomSheet() }
        WelcomeText()

        CustomTextField(
            label = stringResource(id = R.string.email),
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            leadingIconRes = R.drawable.ic_user,
            isPasswordField = false
        )

        CustomTextField(
            label = stringResource(id = R.string.password),
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            leadingIconRes = R.drawable.ic_password,
            isPasswordField = true
        )

        CustomTextField(
            label = stringResource(id = R.string.confirm_password),
            value = confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            leadingIconRes = R.drawable.ic_password,
            isPasswordField = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Padding.HUGE.dp)
            )
        }

        ButtonSignUp(viewModel = viewModel) { isSuccess ->
            if (isSuccess) onSuccess()
        }

        if (showBottomSheet) BottomSheet(viewModel)
        SeparatorAuthenticationOption()
        GoogleView()
        SignInView(navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(viewModel: InscriptionViewModel) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        modifier = Modifier,
        sheetState = sheetState,
        onDismissRequest = { viewModel.closeBottomSheet() },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Padding.MEDIUM.dp),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 75.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Padding.MEDIUM.dp),
                verticalArrangement = Arrangement.spacedBy(Padding.MEDIUM.dp),
                horizontalArrangement = Arrangement.spacedBy(Padding.MEDIUM.dp)
            ) {
                items(PokemonCellProfile.entries) { profile ->
                    ImagePokemon(
                        pokemonCellProfile = profile,
                        onClick = { viewModel.onImageSelected(profile) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ImagePokemon(pokemonCellProfile: PokemonCellProfile, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .aspectRatio(1f)
            .background(brush = pokemonCellProfile.brush)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(pokemonCellProfile.pokemonCellImage),
            contentDescription = pokemonCellProfile.name,
            modifier = Modifier.size(72.dp),
        )
    }
}

@Composable
fun ProfileWithButtonPlus(viewModel: InscriptionViewModel, onButtonClick: () -> Unit) {
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

@Composable
fun WelcomeText() {
    Column(
        verticalArrangement = Arrangement.spacedBy((-10).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(Padding.MEDIUM.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome),
            fontSize = 36.sp,
            fontFamily = fontFamilyAvenir,
            fontWeight = FontWeight.Normal,
        )

        Text(
            text = stringResource(id = R.string.create_account),
            fontSize = 16.sp,
            fontFamily = fontFamilyAvenir,
            modifier = Modifier.padding(bottom = Padding.MINI.dp)
        )
    }
}

@Composable
fun ButtonSignUp(viewModel: InscriptionViewModel, onResult: (Boolean) -> Unit) {
    val isLoading by viewModel.isLoading
    val context = LocalContext.current

    Button(
        onClick = {
            if (!isLoading) {
                viewModel.registerWithEmailAndPassword(
                    context = context,
                    onResult = onResult,
                    onFail = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        },
        modifier = Modifier
            .width(160.dp)
            .padding(top = Padding.HUGE.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            Text(
                text = stringResource(R.string.loading),
                fontFamily = fontFamilyAvenir,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        } else {
            Text(
                text = stringResource(R.string.signup),
                fontFamily = fontFamilyAvenir,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun GoogleView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Padding.HUGE.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = stringResource(id = R.string.google_icon),
            modifier = Modifier
                .padding(end = Padding.MEDIUM.dp)
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
fun SignInView(navController: NavHostController) {
    Row {
        Text(
            text = stringResource(R.string.have_account),
            modifier = Modifier.padding(end = Padding.MINI.dp),
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = stringResource(R.string.signin),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.clickable {
                navController.navigate(AuthenticationNavigation.SignInDestination)
            }
        )
    }
}