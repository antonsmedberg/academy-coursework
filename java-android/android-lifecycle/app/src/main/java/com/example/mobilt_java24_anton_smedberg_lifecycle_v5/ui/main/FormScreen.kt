package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FormScreen(
    vm: MainViewModel,
    onSaved: () -> Unit
) {
    val ui by vm.ui.collectAsState()
    val snack = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current

    LaunchedEffect(ui.message) {
        ui.message?.let {
            snack.showSnackbar(it)
            vm.messageShown()
        }
    }
    LaunchedEffect(Unit) {
        vm.savedEvent.collect { onSaved() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            focus.clearFocus()
                            vm.submit()
                        },
                        enabled = ui.canSubmit && !ui.saving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (ui.saving) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text(if (ui.saving) "Sparar…" else "Spara")
                    }
                }
            }
        }
    ) { padding ->
        FormScreenContent(
            ui = ui,
            onAge = { v -> vm.onAge(v.filter { it.isDigit() }.take(3)) },
            onHasDL = vm::onHasDL,
            onGender = vm::onGender,
            onPhone = vm::onPhone,
            onEmail = vm::onEmail,
            onSubmit = {
                focus.clearFocus()
                vm.submit()
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormScreenContent(
    ui: MainViewModel.UiState,
    onAge: (String) -> Unit,
    onHasDL: (Boolean) -> Unit,
    onGender: (String) -> Unit,
    onPhone: (String) -> Unit,
    onEmail: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focus = LocalFocusManager.current
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Profil", style = MaterialTheme.typography.headlineMedium)

        ElevatedCard {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = ui.age,
                    onValueChange = onAge,
                    label = { Text("Ålder") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(FocusDirection.Down) }
                    ),
                    isError = ui.errors.containsKey("age"),
                    supportingText = { ui.errors["age"]?.let { Text(it) } },
                    trailingIcon = {
                        if (ui.age.isNotEmpty())
                            TextButton(onClick = { onAge("") }) { Text("Rensa") }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = ui.hasDL, onCheckedChange = onHasDL)
                    Spacer(Modifier.width(8.dp))
                    Text("Har körkort")
                }

                Text("Kön", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = ui.gender == "kvinna",
                        onClick = { onGender("kvinna") },
                        label = { Text("Kvinna") }
                    )
                    FilterChip(
                        selected = ui.gender == "man",
                        onClick = { onGender("man") },
                        label = { Text("Man") }
                    )
                    FilterChip(
                        selected = ui.gender == "annat",
                        onClick = { onGender("annat") },
                        label = { Text("Annat") }
                    )
                }
                ui.errors["gender"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                OutlinedTextField(
                    value = ui.phone,
                    onValueChange = onPhone,
                    label = { Text("Telefon (+46… eller 07…)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focus.moveFocus(FocusDirection.Down) }
                    ),
                    isError = ui.errors.containsKey("phone"),
                    supportingText = { ui.errors["phone"]?.let { Text(it) } },
                    trailingIcon = {
                        if (ui.phone.isNotEmpty())
                            TextButton(onClick = { onPhone("") }) { Text("Rensa") }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ui.email,
                    onValueChange = onEmail,
                    label = { Text("E-post") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focus.clearFocus()
                            onSubmit()
                        }
                    ),
                    isError = ui.errors.containsKey("email"),
                    supportingText = { ui.errors["email"]?.let { Text(it) } },
                    trailingIcon = {
                        if (ui.email.isNotEmpty())
                            TextButton(onClick = { onEmail("") }) { Text("Rensa") }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(64.dp)) // plats över bottomBar-knappen
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FormScreen_Preview() {
    val previewState = MainViewModel.UiState(
        userId = 1L,
        age = "28",
        hasDL = true,
        gender = "man",
        phone = "+46701234567",
        email = "demo@example.com",
        errors = emptyMap(),
        canSubmit = true,
        message = "Sparat!"
    )
    FormScreenContent(
        ui = previewState,
        onAge = {}, onHasDL = {}, onGender = {}, onPhone = {}, onEmail = {}, onSubmit = {}
    )
}