package com.example.pokemonultimate.ui.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pokemonultimate.R


fun String.setFirstToUpperCase(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIconRes: Int,
    isPasswordField: Boolean = false
) {
    val visualTransformation =
        if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None

    TextField(
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = label,
                modifier = Modifier
                    .size(27.dp)
                    .offset(y = (-4).dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = Modifier
            .padding(horizontal = Padding.MEDIUM.dp, vertical = Padding.MINI.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = if (isPasswordField) {
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None
            )
        } else {
            KeyboardOptions.Default
        }
    )
}

@Composable
fun OrView() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 75.dp, vertical = 24.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(3.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = stringResource(R.string.or),
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.secondary

        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
        )
    }
}
