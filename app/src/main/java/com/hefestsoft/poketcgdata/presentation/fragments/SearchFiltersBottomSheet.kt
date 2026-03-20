package com.hefestsoft.poketcgdata.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.data.dtos.MetadataSetDto
import com.hefestsoft.poketcgdata.databinding.BottomSheetSearchFiltersBinding
import com.hefestsoft.poketcgdata.presentation.viewsModels.SearchViewModel

data class SearchFiltersUiState(
    val rarity: String? = null,
    val type: String? = null,
    val category: String? = null,
    val setId: String? = null,
    val setName: String? = null
) {
    fun hasApiFilters(): Boolean {
        return !rarity.isNullOrBlank() ||
            !type.isNullOrBlank() ||
            !category.isNullOrBlank() ||
            !setId.isNullOrBlank()
    }
}

class SearchFiltersBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSearchFiltersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private lateinit var currentState: SearchFiltersUiState
    private var availableSets: List<MetadataSetDto> = emptyList()

    override fun getTheme(): Int = R.style.ThemeOverlay_PokeTcgData_BottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentState = arguments?.toUiState() ?: SearchFiltersUiState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSearchFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeMetadata()
        renderSetSelection()
        viewModel.loadFiltersMetadata()
    }

    private fun setupListeners() {
        binding.actionReset.setOnClickListener {
            currentState = SearchFiltersUiState()
            viewModel.filtersMetadata.value?.metadata?.let { metadata ->
                renderRarities(metadata.rarities)
                renderTypes(metadata.types)
                renderCardTypes(metadata.cardTypes)
            }
            renderSetSelection()
        }

        binding.setDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedSet = availableSets.getOrNull(position) ?: return@setOnItemClickListener
            currentState = currentState.copy(
                setId = selectedSet.id,
                setName = selectedSet.name
            )
            renderSetSelection()
        }

        binding.applyFiltersButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(
                    RESULT_RARITY to currentState.rarity,
                    RESULT_TYPE to currentState.type,
                    RESULT_CATEGORY to currentState.category,
                    RESULT_SET_ID to currentState.setId,
                    RESULT_SET_NAME to currentState.setName
                )
            )
            dismiss()
        }
    }

    private fun observeMetadata() {
        viewModel.filtersMetadata.observe(viewLifecycleOwner) { uiState ->
            binding.metadataProgress.isVisible = uiState.isLoading && uiState.metadata == null
            binding.filtersContent.isVisible = uiState.metadata != null
            binding.metadataError.isVisible = !uiState.errorMessage.isNullOrBlank()
            binding.metadataError.text = uiState.errorMessage

            val metadata = uiState.metadata ?: return@observe
            renderRarities(metadata.rarities.sorted())
            renderTypes(metadata.types.sorted())
            renderCardTypes(metadata.cardTypes.sorted())
            renderSets(metadata.sets.sortedBy { it.name })
        }
    }

    private fun renderRarities(options: List<String>) {
        renderChipSection(
            chipGroup = binding.rarityChipGroup,
            options = options,
            selectedValue = currentState.rarity
        ) { selectedValue ->
            currentState = currentState.copy(
                rarity = if (currentState.rarity == selectedValue) null else selectedValue
            )
            renderRarities(options)
        }
    }

    private fun renderTypes(options: List<String>) {
        renderChipSection(
            chipGroup = binding.typeChipGroup,
            options = options,
            selectedValue = currentState.type
        ) { selectedValue ->
            currentState = currentState.copy(
                type = if (currentState.type == selectedValue) null else selectedValue
            )
            renderTypes(options)
        }
    }

    private fun renderCardTypes(options: List<String>) {
        renderChipSection(
            chipGroup = binding.cardTypeChipGroup,
            options = options,
            selectedValue = currentState.category
        ) { selectedValue ->
            currentState = currentState.copy(
                category = if (currentState.category == selectedValue) null else selectedValue
            )
            renderCardTypes(options)
        }
    }

    private fun renderSets(sets: List<MetadataSetDto>) {
        availableSets = sets
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            sets.map { it.name }
        )
        binding.setDropdown.setAdapter(adapter)
        renderSetSelection()
    }

    private fun renderSetSelection() {
        binding.setDropdown.setText(currentState.setName.orEmpty(), false)
    }

    private fun renderChipSection(
        chipGroup: ChipGroup,
        options: List<String>,
        selectedValue: String?,
        onClick: (String) -> Unit
    ) {
        chipGroup.removeAllViews()

        options.forEach { option ->
            val chip = Chip(requireContext()).apply {
                text = option
                isCheckable = false
                chipStrokeWidth = 0f
                chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(
                        if (option == selectedValue) R.color.filter_chip_selected else R.color.filter_chip_background
                    )
                )
                setTextColor(requireContext().getColor(R.color.filter_chip_text))
                chipMinHeight = resources.getDimension(R.dimen.filter_chip_min_height)
                chipCornerRadius = resources.getDimension(R.dimen.filter_chip_corner_radius)
                setEnsureMinTouchTargetSize(false)
                setOnClickListener { onClick(option) }
            }

            chipGroup.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY = "search_filters_request"

        private const val ARG_RARITY = "arg_rarity"
        private const val ARG_TYPE = "arg_type"
        private const val ARG_CATEGORY = "arg_category"
        private const val ARG_SET_ID = "arg_set_id"
        private const val ARG_SET_NAME = "arg_set_name"

        const val RESULT_RARITY = "result_rarity"
        const val RESULT_TYPE = "result_type"
        const val RESULT_CATEGORY = "result_category"
        const val RESULT_SET_ID = "result_set_id"
        const val RESULT_SET_NAME = "result_set_name"

        fun newInstance(state: SearchFiltersUiState): SearchFiltersBottomSheet {
            return SearchFiltersBottomSheet().apply {
                arguments = bundleOf(
                    ARG_RARITY to state.rarity,
                    ARG_TYPE to state.type,
                    ARG_CATEGORY to state.category,
                    ARG_SET_ID to state.setId,
                    ARG_SET_NAME to state.setName
                )
            }
        }

        private fun Bundle.toUiState(): SearchFiltersUiState {
            return SearchFiltersUiState(
                rarity = getString(ARG_RARITY),
                type = getString(ARG_TYPE),
                category = getString(ARG_CATEGORY),
                setId = getString(ARG_SET_ID),
                setName = getString(ARG_SET_NAME)
            )
        }
    }
}
