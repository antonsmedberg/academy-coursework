package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String, onLogout: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            if (onLogout != null) {
                TextButton(onClick = onLogout) { Text("Logga ut") }
            }
        }
    )
}