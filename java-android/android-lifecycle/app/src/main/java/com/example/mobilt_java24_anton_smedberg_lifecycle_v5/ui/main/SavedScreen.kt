package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.data.model.Profile

@Composable
fun SavedScreen(
    vm: MainViewModel,
    onGoToForm: () -> Unit
) {
    val ui by vm.ui.collectAsState()
    SavedScreenContent(profile = ui.saved, onGoToForm = onGoToForm)
}

@Composable
private fun RowKV(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SavedScreenContent(
    profile: Profile?,
    onGoToForm: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Sparad profil", style = MaterialTheme.typography.headlineMedium)

        if (profile == null) {
            ElevatedCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ingen sparad data ännu.", style = MaterialTheme.typography.titleMedium)
                    Text("Gå till formuläret och spara din profil så visas den här.")
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(onClick = onGoToForm) { Text("Gå till formuläret") }
                    }
                }
            }
        } else {
            ElevatedCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    RowKV("Ålder", "${profile.age}")
                    RowKV("Körkort", if (profile.hasDriverLicense) "Ja" else "Nej")
                    RowKV("Kön", profile.gender)
                    RowKV("Telefon", profile.phone)
                    RowKV("E-post", profile.email)

                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        FilledTonalButton(onClick = onGoToForm) { Text("Redigera") }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SavedScreen_Preview_Empty() {
    SavedScreenContent(profile = null, onGoToForm = {})
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SavedScreen_Preview_Filled() {
    SavedScreenContent(
        profile = Profile(
            userId = 1L,
            age = 29,
            hasDriverLicense = true,
            gender = "man",
            phone = "+46701234567",
            email = "demo@example.com"
        ),
        onGoToForm = {}
    )
}