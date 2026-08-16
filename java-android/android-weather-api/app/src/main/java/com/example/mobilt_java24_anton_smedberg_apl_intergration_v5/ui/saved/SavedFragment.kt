package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.saved

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
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.R
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.FragmentSavedBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.CityAdapter
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyBottomInsets
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SavedViewModel> {
        object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SavedViewModel(ServiceLocator.repository()) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Behåll systemets botteninsets (nav bar) så listan inte döljs
        binding.listSaved.clipToPadding = false
        binding.listSaved.applyBottomInsets()

        // “Sparad lista”: klick öppnar detaljer, bookmark-knappen tar bort
        val adapter = CityAdapter(
            onClick = ::openDetails,
            onSave = { city -> viewModel.delete(city) }
        )
        binding.listSaved.setHasFixedSize(true)
        binding.listSaved.adapter = adapter

        // Reagera på förändringar i sparade städer
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saved.collectLatest { items ->
                    adapter.savedKeys = items.map { it.stableKey() }.toSet()
                    adapter.submitList(items)
                    binding.emptyView.isVisible = items.isEmpty()
                    binding.listSaved.isVisible = items.isNotEmpty()
                }
            }
        }
    }

    private fun openDetails(city: City) {
        val args = Bundle().apply {
            putLong("cityId", city.id)
            putString("name", city.name)
            putString("country", city.country)
            putString("admin1", city.admin1)
            putDouble("lat", city.latitude)
            putDouble("lon", city.longitude)
        }
        findNavController().navigate(R.id.detailsFragment, args)
    }

    override fun onDestroyView() {
        binding.listSaved.adapter = null
        _binding = null
        super.onDestroyView()
    }

    // Samma stabila nyckel du använder i Home – för att markera sparade i adaptern
    private fun City.stableKey() = "$name|$latitude|$longitude"
}