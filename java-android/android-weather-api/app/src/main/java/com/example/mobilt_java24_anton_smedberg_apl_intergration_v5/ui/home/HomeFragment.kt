package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.home

import android.os.Bundle
import android.os.LocaleList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.R
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.databinding.FragmentHomeBinding
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.domain.model.City
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.CityAdapter
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyBottomInsets
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.ui.common.applyBottomInsetsToMargin
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.NetworkMonitor
import com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.util.ServiceLocator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cityAdapter: CityAdapter

    private val viewModel by viewModels<HomeViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val online = NetworkMonitor.observe(requireContext())
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(ServiceLocator.repository(), online) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Inset-hantering
        binding.listResults.clipToPadding = false
        binding.listResults.applyBottomInsets()
        binding.homeActions.applyBottomInsetsToMargin()

        // IME: visa svenska + engelska som hint-locale
        binding.inputSearch.setImeHintLocales(LocaleList.forLanguageTags("sv-SE,en-US"))
        binding.inputSearch.setTextLocales(LocaleList.forLanguageTags("sv-SE,en-US"))

        // Starta utan fokus på fältet
        binding.inputSearch.clearFocus()
        binding.listResults.requestFocus()

        // Göm keyboard när listan scrollas
        binding.listResults.setOnScrollChangeListener { v, _, _, _, _ ->
            if (binding.inputSearch.hasFocus()) {
                binding.inputSearch.clearFocus()
                v.hideKeyboard()
            }
        }

        // Lista
        cityAdapter = CityAdapter(
            onClick = ::openDetails,
            onSave  = { city -> viewModel.toggleFavorite(city) }
        )
        binding.listResults.setHasFixedSize(true)
        binding.listResults.adapter = cityAdapter

        // Sökfält
        binding.inputSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.updateQuery(text?.toString().orEmpty())
        }
        binding.inputSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus(); v.hideKeyboard(); true
            } else false
        }

        // Lyssna på UI-state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest(::render)
            }
        }

        // Nav
        binding.btnSaved.setOnClickListener { findNavController().navigate(R.id.savedFragment) }
        binding.btnSettings.setOnClickListener { findNavController().navigate(R.id.settingsFragment) }
    }

    private fun render(state: HomeUiState) {
        binding.progress.isGone = state !is HomeUiState.Loading
        when (state) {
            HomeUiState.Idle -> showEmpty(R.string.type_to_search)
            HomeUiState.Loading -> {
                binding.emptyView.isGone = true
                binding.listResults.isGone = true
            }
            is HomeUiState.Data -> {
                cityAdapter.savedKeys = state.savedKeys
                if (state.items.isEmpty()) {
                    showEmpty(R.string.no_results)
                } else {
                    binding.emptyView.isGone = true
                    binding.listResults.isVisible = true
                    cityAdapter.submitList(state.items)
                }
            }
            is HomeUiState.Error -> showEmpty(R.string.error_generic)
        }
    }

    private fun showEmpty(msgRes: Int) {
        binding.listResults.isGone = true
        binding.emptyView.isVisible = true
        binding.emptyView.setText(msgRes)
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
        binding.listResults.adapter = null
        _binding = null
        super.onDestroyView()
    }
}

// IME helper
private fun View.hideKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
            as android.view.inputmethod.InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}