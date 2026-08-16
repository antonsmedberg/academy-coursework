package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login.LoginActivity
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.nav.MainNavRoot
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavRoot(onLogout = { logout() })
        }

        //
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ServiceLocator.sessionPrefs.isLoggedInFlow.collect { loggedIn ->
                    if (!loggedIn) navigateToLoginClearTask()
                }
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            ServiceLocator.sessionPrefs.logout()
            ServiceLocator.sessionPrefs.clearDraft()
            navigateToLoginClearTask()
        }
    }

    private fun navigateToLoginClearTask() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }
}