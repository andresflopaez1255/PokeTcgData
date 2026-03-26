package com.hefestsoft.poketcgdata.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hefestsoft.poketcgdata.core.LocaleManager
import com.hefestsoft.poketcgdata.databinding.FragmentSettingBinding
import com.hefestsoft.poketcgdata.presentation.viewsModels.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var localeManager: LocaleManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLanguagePicker()
        setupObservers()
        binding.saveLanguageButton.setOnClickListener {
            viewModel.saveLanguage()
        }
    }

    private fun setupLanguagePicker() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            viewModel.supportedLanguages
        )
        binding.languageDropdown.setAdapter(adapter)
        binding.languageDropdown.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectLanguage(viewModel.supportedLanguages[position])
        }
    }

    private fun setupObservers() {
        viewModel.selectedLanguage.observe(viewLifecycleOwner) { languageCode ->
            binding.languageDropdown.setText(languageCode, false)
        }

        viewModel.deviceId.observe(viewLifecycleOwner) { deviceId ->
            binding.deviceIdValue.text = deviceId
        }

        viewModel.isSaving.observe(viewLifecycleOwner) { isSaving ->
            binding.saveLanguageButton.isEnabled = !isSaving
            binding.savingIndicator.isVisible = isSaving
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            binding.errorMessage.isVisible = !errorMessage.isNullOrBlank()
            binding.errorMessage.text = errorMessage
        }

        viewModel.savedLanguageEvent.observe(viewLifecycleOwner) { languageCode ->
            if (languageCode.isNullOrBlank()) return@observe
            localeManager.applyLanguage(languageCode)
            viewModel.consumeSavedLanguageEvent()
            requireActivity().recreate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
