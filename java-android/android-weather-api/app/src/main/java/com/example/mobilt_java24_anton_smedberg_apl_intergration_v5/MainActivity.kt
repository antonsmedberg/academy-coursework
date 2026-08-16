package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.ActivityMainBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyStatusBarPadding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    private val askNotif = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val host = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = host.navController

        // Bara Home är top-level → back-ikon visas på Saved/Settings
        appBarConfig = AppBarConfiguration(setOf(R.id.homeFragment))
        setupActionBarWithNavController(navController, appBarConfig)

        applyInsets()
    }

    private fun applyInsets() {
        // Statusbar-padding på AppBarLayout → titel/back blir aldrig under statusfältet
        binding.appbar.applyStatusBarPadding()

        // NavHost: vänster/höger. Bottom hanteras i respektive fragment (listor/knappar).
        ViewCompat.setOnApplyWindowInsetsListener(binding.navHost) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = bars.left, right = bars.right)
            insets
        }

        // Trigga direkt
        ViewCompat.requestApplyInsets(binding.appbar)
        ViewCompat.requestApplyInsets(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()

    fun ensurePostNotifications() {
        if (Build.VERSION.SDK_INT >= 33) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) askNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}