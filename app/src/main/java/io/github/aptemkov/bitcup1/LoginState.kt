package io.github.aptemkov.bitcup1

sealed class LoginState {
    object Success : LoginState()
    object Error : LoginState()
    object Loading : LoginState()
}
