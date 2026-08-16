package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.MainActivity
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.R
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.FragmentSettingsBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyBottomInsets
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.NotificationUtils
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.workers.WeatherCheckWorker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel> {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(ServiceLocator.settingsStore()) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        // Scroll + insets så inget krockar med system-UI
        settingsRoot.clipToPadding = false
        settingsRoot.applyBottomInsets()

        // Visa verktygsknappar bara i debug-builds
        debugGroup.isVisible

        // Testa notis (debug)
        btnTestNotif.setOnClickListener {
            it.isEnabled = false
            val appCtx = requireContext().applicationContext
            (requireActivity() as? MainActivity)?.ensurePostNotifications()
            NotificationUtils.createChannels(appCtx)
            WeatherCheckWorker.runOnceTest(
                context = appCtx,
                forcePct = 92,
                sendAll = true,
                bypassCooldown = true,
                bypassAlertsCheck = true
            )
            Snackbar.make(settingsRoot, getString(R.string.settings_test_sent), Snackbar.LENGTH_SHORT).show()
            it.postDelayed({ it.isEnabled = true }, 800)
        }

        // Rensa cooldown (debug)
        btnClearCooldown.setOnClickListener {
            it.isEnabled = false
            val had = WeatherCheckWorker.clearCooldown(requireContext().applicationContext)
            val msgRes = if (had) R.string.settings_cooldown_cleared else R.string.settings_cooldown_empty
            Snackbar.make(settingsRoot, getString(msgRes), Snackbar.LENGTH_SHORT).show()
            it.postDelayed({ it.isEnabled = true }, 400)
        }

        // Koppla ViewModel → UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    // Koppla ur listeners innan vi sätter switchar (undvik loop)
                    switchUnits.setOnCheckedChangeListener(null)
                    switchAlerts.setOnCheckedChangeListener(null)

                    switchUnits.isChecked = state.useFahrenheit
                    switchAlerts.isChecked = state.alertsEnabled

                    // Byt °C/°F
                    switchUnits.setOnCheckedChangeListener { _, useF ->
                        viewModel.setUnits(useF)
                    }

                    // Slå på/av vädervarningar
                    switchAlerts.setOnCheckedChangeListener { _, enabled ->
                        viewModel.setAlerts(enabled)
                        val appCtx = requireContext().applicationContext
                        if (enabled) {
                            (requireActivity() as? MainActivity)?.ensurePostNotifications()
                            NotificationUtils.createChannels(appCtx)
                            WeatherCheckWorker.schedule(appCtx)
                            WeatherCheckWorker.runOnce(appCtx)
                            Snackbar.make(settingsRoot, getString(R.string.settings_alerts_on), Snackbar.LENGTH_SHORT).show()
                        } else {
                            WeatherCheckWorker.cancel(appCtx)
                            Snackbar.make(settingsRoot, getString(R.string.settings_alerts_off), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // “Till startsidan (rensa stack)” – one-way back
        btnLogout.setOnClickListener { popToHome() }
    }

    private fun popToHome() {
        findNavController().navigate(
            R.id.homeFragment,
            null,
            navOptions {
                popUpTo(R.id.homeFragment) { inclusive = true }
                launchSingleTop = true
            }
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}