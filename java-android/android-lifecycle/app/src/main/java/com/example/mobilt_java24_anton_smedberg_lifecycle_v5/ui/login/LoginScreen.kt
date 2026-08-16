package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.components.LabeledTextField

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    // “Wire-up”: VM + callbacks. Ingen UI-logik här inne.
    val vm: LoginViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(ServiceLocator.authRepo, ServiceLocator.sessionPrefs) as T
        }
    })
    val ui by vm.ui.collectAsState()

    LoginScreenContent(
        ui = ui,
        onUsername = vm::onUsername,
        onPassword = vm::onPassword,
        onSubmit = { vm.login(onLoginSuccess) },
        onGoRegister = onGoRegister
    )
}

// Ren UI → Preview-vänlig. Jag håller texterna och enabled/feil i props.
@Composable
fun LoginScreenContent(
    ui: LoginViewModel.UiState,
    onUsername: (String) -> Unit,
    onPassword: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoRegister: () -> Unit
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(Modifier.padding(24.dp).widthIn(max = 420.dp)) {
            Text("Logga in", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            LabeledTextField(ui.username, onUsername, label = "Användarnamn")
            Spacer(Modifier.height(8.dp))
            LabeledTextField(ui.password, onPassword, label = "Lösenord", password = true)

            ui.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSubmit,
                enabled = ui.canSubmit && !ui.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.loading) "Loggar in…" else "Logga in")
            }

            TextButton(onClick = onGoRegister, modifier = Modifier.align(Alignment.End)) {
                Text("Skapa konto")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreen_Preview() {
    val state = LoginViewModel.UiState(
        username = "demo",
        password = "password123",
        error = null,
        loading = false,
        canSubmit = true
    )
    LoginScreenContent(
        ui = state,
        onUsername = {},
        onPassword = {},
        onSubmit = {},
        onGoRegister = {}
    )
}