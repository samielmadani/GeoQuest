package com.example.geoquest.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.geoquest.R
import com.example.geoquest.ui.navigation.NavigationDestination
import com.example.geoquest.ui.theme.GeoQuestTheme

object SignUpDestination: NavigationDestination {
    override val route = "signup"
    override val titleRes = R.string.app_name
}

@Composable
fun SignUpScreen() {
    val textFieldValue = remember { mutableStateOf("") }
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.app_icon)
            )
            Text(
                text = stringResource(id = R.string.signup_text),
                fontSize = dimensionResource(id = R.dimen.signup_text_size).value.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = textFieldValue.value,
                onValueChange = { textFieldValue.value = it },
                label = { Text(stringResource(id = R.string.player_name)) },
                singleLine = true,
            )
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = stringResource(id = R.string.signup_button)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    GeoQuestTheme {
        SignUpScreen()
    }
}