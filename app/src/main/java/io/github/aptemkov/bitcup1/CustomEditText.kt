package io.github.aptemkov.bitcup1

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aptemkov.bitcup1.shaker.ShakeConfig
import io.github.aptemkov.bitcup1.shaker.rememberShakeController
import io.github.aptemkov.bitcup1.shaker.shake
import io.github.aptemkov.bitcup1.ui.theme.Green
import io.github.aptemkov.bitcup1.ui.theme.Red
import io.github.aptemkov.bitcup1.ui.theme.White

const val PASSWORD = "12345"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CustomEditTextWithButton() {

    val errorShakeConfig = ShakeConfig(
        iterations = 4,
        intensity = 2000f,
        rotateY = 15f,
        translateX = 40f,
    )

    val successShakeConfig = ShakeConfig(
        iterations = 4,
        intensity = 1000f,
        rotateX = -20f,
        translateY = 20f,
    )

    var loginState: LoginState by remember { mutableStateOf(LoginState.Loading) }
    var password by remember { mutableStateOf("") }
    val color: Color by animateColorAsState(
        when (loginState) {
            LoginState.Success -> Green
            LoginState.Error -> Red
            else -> White
        },
        label = stringResource(R.string.animated_button_color_description),
    )
    val shakeController = rememberShakeController()

    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = password,
            onValueChange = {
                loginState = LoginState.Loading
                password = it
            },
            isError = loginState == LoginState.Error,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = color,
                unfocusedIndicatorColor = color,
                disabledIndicatorColor = color,
                errorIndicatorColor = color,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .shake(shakeController = shakeController)
                .padding(8.dp)
                .clip(RoundedCornerShape(6.dp))
                .border(2.dp, color, RoundedCornerShape(6.dp))
                .background(color = color.copy(alpha = 0.1f))
                .clickable {
                    loginState = when (password) {
                        PASSWORD -> LoginState.Success
                        "" -> LoginState.Loading
                        else -> LoginState.Error
                    }
                    when (loginState) {
                        LoginState.Error -> {
                            shakeController.shake(errorShakeConfig)
                        }

                        LoginState.Success -> {
                            shakeController.shake(successShakeConfig)
                        }

                        LoginState.Loading -> {}
                    }
                }
        ) {
            AnimatedContent(
                targetState = loginState,
                label = stringResource(R.string.login_button_description),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    text = when (loginState) {
                        LoginState.Success -> stringResource(R.string.success)
                        LoginState.Error -> stringResource(R.string.try_again)
                        else -> stringResource(R.string.login)
                    },
                    color = White,
                )
            }
        }
    }
}

@Composable
@Preview
fun CustomEditTextPreview() {
    CustomEditTextWithButton()
}