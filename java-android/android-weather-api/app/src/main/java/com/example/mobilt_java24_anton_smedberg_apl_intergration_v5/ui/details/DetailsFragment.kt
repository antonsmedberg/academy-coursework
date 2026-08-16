// ui/details/DetailsFragment.kt
package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.details

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
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.FragmentDetailsBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyBottomInsets
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailsViewModel> {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val repo = ServiceLocator.repository()
                val store = ServiceLocator.settingsStore()
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(repo, store) as T
            }
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // RecyclerView-setup
        binding.listHourly.clipToPadding = false
        binding.listHourly.applyBottomInsets()
        val forecastAdapter = ForecastAdapter()
        binding.listHourly.setHasFixedSize(true)
        binding.listHourly.adapter = forecastAdapter

        // Stad från arguments
        val city = City(
            id = requireArguments().getLong("cityId"),
            name = requireArguments().getString("name") ?: "Okänd",
            country = requireArguments().getString("country"),
            admin1 = requireArguments().getString("admin1"),
            latitude = requireArguments().getDouble("lat"),
            longitude = requireArguments().getDouble("lon")
        )
        binding.txtCity.text = listOfNotNull(city.name, city.admin1, city.country).joinToString(", ")

        // Observers
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { ui ->
                    when (ui) {
                        is DetailsUiState.Loading -> {
                            binding.progress.isVisible = true
                            binding.listHourly.isVisible = false
                            binding.emptyView.isVisible = false
                            binding.txtCurrent.text = "—"
                        }
                        is DetailsUiState.Data -> {
                            binding.progress.isVisible = false
                            binding.txtCurrent.text = ui.headerText
                            forecastAdapter.submitList(ui.days)
                            binding.listHourly.isVisible = ui.days.isNotEmpty()
                            binding.emptyView.isVisible = ui.days.isEmpty()
                        }
                        is DetailsUiState.Error -> {
                            binding.progress.isVisible = false
                            binding.txtCurrent.text = ui.message
                            forecastAdapter.submitList(emptyList())
                            binding.listHourly.isVisible = false
                            binding.emptyView.isVisible = true
                        }
                    }
                }
            }
        }

        viewModel.load(city)
    }

    override fun onDestroyView() {
        binding.listHourly.adapter = null
        _binding = null
        super.onDestroyView()
    }
}