package com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.di.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_lifecycle_v5.ui.main.MainActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Jag hoppar över login om autoinlogg finns.
        lifecycleScope.launch {
            ServiceLocator.authRepo.ensureSeedUser()
            val uid = ServiceLocator.sessionPrefs.loggedInUserIdFlow.first()
            if (uid != null) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        setContent { LoginNavRoot(onLoggedIn = { goMain() }) }
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}