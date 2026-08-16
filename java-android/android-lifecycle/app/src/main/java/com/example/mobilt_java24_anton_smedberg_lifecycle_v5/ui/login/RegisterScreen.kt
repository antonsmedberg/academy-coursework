package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.repo.AuthRepository
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.util.Validators
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.components.LabeledTextField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- Screen: binder VM och callbacks, ingen tung UI här ---
@Composable
fun RegisterScreen(onRegistered: () -> Unit) {
    val vm: RegisterViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(ServiceLocator.authRepo) as T
        }
    })
    val ui by vm.ui.collectAsState()

    RegisterScreenContent(
        ui = ui,
        onUsername = vm::onUsername,
        onEmail = vm::onEmail,
        onPnr = vm::onPnr,
        onPassword = vm::onPassword,
        onSubmit = { vm.register(onRegistered) }
    )
}

// --- Content: ren UI, preview-vänlig, inga ServiceLocator/VM/IO ---
@Composable
fun RegisterScreenContent(
    ui: RegisterViewModel.UiState,
    onUsername: (String) -> Unit,
    onEmail: (String) -> Unit,
    onPnr: (String) -> Unit,
    onPassword: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(Modifier.padding(24.dp)) {
        Text("Registrera", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        // Jag ger lite input-hints via IME/keyboardType för bättre UX.
        LabeledTextField(
            value = ui.username,
            onValueChange = onUsername,
            label = "Användarnamn",
            isError = ui.username.isNotBlank() && !Validators.username(ui.username),
            supportingText = "Min 3 tecken",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        LabeledTextField(
            value = ui.email,
            onValueChange = onEmail,
            label = "E-post",
            isError = ui.email.isNotBlank() && !Validators.email(ui.email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        LabeledTextField(
            value = ui.pnr,
            onValueChange = onPnr,
            label = "Personnummer",
            isError = ui.pnr.isNotBlank() && !Validators.personnummer(ui.pnr),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        LabeledTextField(
            value = ui.password,
            onValueChange = onPassword,
            label = "Lösenord",
            isError = ui.password.isNotBlank() && !Validators.password(ui.password),
            supportingText = "Min 6 tecken",
            password = true,
            modifier = Modifier.fillMaxWidth()
        )

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
            Text(if (ui.loading) "Skapar konto…" else "Skapa konto")
        }
    }
}

// --- ViewModel: enkel validering + dubbelklick-skydd ---
class RegisterViewModel(private val auth: AuthRepository) : ViewModel() {
    data class UiState(
        val username: String = "",
        val email: String = "",
        val pnr: String = "",
        val password: String = "",
        val canSubmit: Boolean = false,
        val loading: Boolean = false,
        val error: String? = null
    )

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui

    fun onUsername(v: String) = update { it.copy(username = v).validate() }
    fun onEmail(v: String) = update { it.copy(email = v).validate() }
    fun onPnr(v: String) = update { it.copy(pnr = v).validate() }
    fun onPassword(v: String) = update { it.copy(password = v).validate() }

    fun register(onOk: () -> Unit) {
        val s = _ui.value
        if (!s.canSubmit || s.loading) return  // jag skyddar mot dubbeltryck
        update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val u = _ui.value // hämta uppdaterad state
            auth.register(
                username = u.username.trim(),
                email = u.email.trim(),
                pnr = u.pnr.trim(),
                password = u.password
            ).onSuccess {
                update { it.copy(loading = false) }
                onOk()
            }.onFailure { e ->
                update { it.copy(loading = false, error = e.message ?: "Kunde inte skapa konto") }
            }
        }
    }

    private fun UiState.validate(): UiState {
        val ok = Validators.username(username.trim())
                && Validators.email(email.trim())
                && Validators.personnummer(pnr.trim())
                && Validators.password(password)
        return copy(canSubmit = ok, error = null)
    }

    private fun update(f: (UiState) -> UiState) {
        _ui.value = f(_ui.value)
    }
}

// --- Previews: inga repos, bara dummy-state in i Content ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreen_Preview_Empty() {
    RegisterScreenContent(
        ui = RegisterViewModel.UiState(),
        onUsername = {}, onEmail = {}, onPnr = {}, onPassword = {}, onSubmit = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreen_Preview_Filled() {
    RegisterScreenContent(
        ui = RegisterViewModel.UiState(
            username = "anton",
            email = "anton@example.com",
            pnr = "19900101-1234",
            password = "secret123",
            canSubmit = true
        ),
        onUsername = {}, onEmail = {}, onPnr = {}, onPassword = {}, onSubmit = {}
    )
}