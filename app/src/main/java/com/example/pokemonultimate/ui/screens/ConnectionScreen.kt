package com.example.pokemonultimate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemonultimate.R

@Composable
fun ConnectionScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrowleft),
                contentDescription = stringResource(id = R.string.user_icon),
                modifier = Modifier
                    .padding(all = 16.dp)
                    .size(30.dp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_logo_user_pkm),
            contentDescription = stringResource(id = R.string.content_logo_profil_empty),
            modifier = Modifier
                .fillMaxWidth()
                .size(160.dp)
        )

        Text(text = stringResource(id = R.string.welcome_back), fontSize = 36.sp)
        Text(text = stringResource(id = R.string.login_text), fontSize = 14.sp)

        TextField(
            singleLine = true,
            value = username,
            onValueChange = { username = it },
            label = { Text(text = stringResource(id = R.string.username)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = stringResource(id = R.string.user_icon),
                    modifier = Modifier.size(32.dp)
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )


        TextField(
            singleLine = true,
            value = password,
            onValueChange = { password = it },
            label = { Text(text = stringResource(id = R.string.password)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = stringResource(id = R.string.password_icon),
                    modifier = Modifier.size(32.dp)
                )
            }
        )


        Button(onClick = { }) {
            Text(text = stringResource(R.string.signin))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 75.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp),
                color = MaterialTheme.colorScheme.inverseSurface
            )

            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier
                    .weight(1f)
                    .height(3.dp)
            )
        }

        Button(onClick = { }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = stringResource(id = R.string.google_icon),
                modifier = Modifier.size(105.dp)
            )
            Text(
                text = stringResource(R.string.signin_google),
            )
        }


        Row() {
            Text(
                text = stringResource(R.string.dont_account),
            )
            Text(
                text = stringResource(R.string.signup),
            )
        }


    }

}