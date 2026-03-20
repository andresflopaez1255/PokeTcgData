package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.data.dtos.CardQuery
import com.hefestsoft.poketcgdata.presentation.viewsModels.SearchViewModel
import com.hefestsoft.poketcgdata.databinding.FragmentSearchBinding
import com.hefestsoft.poketcgdata.presentation.views.lists.searchList.SearchListAdapter
import com.hefestsoft.poketcgdata.utils.LoadingAnimation
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private val searchQuery = MutableStateFlow("")

    private lateinit var adapter: SearchListAdapter
    private lateinit var loadingManager: LoadingManager
    private var bottomNav: View? = null
    private var currentFilters = SearchFiltersUiState()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchListAdapter { cardId ->
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(cardId)
            findNavController().navigate(action)
        }


        loadingManager = LoadingManager(
            lifecycleScope,
            binding.loading.txtLoadingSubtitle,
            binding.loading.loadingContainer,
            binding.loading.loadingView,
            LoadingAnimation.LoadingInfo
        )

        bottomNav = activity?.findViewById(R.id.bottomNav)
        bottomNav?.visibility = View.GONE

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        childFragmentManager.setFragmentResultListener(
            SearchFiltersBottomSheet.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            currentFilters = currentFilters.copy(
                rarity = bundle.getString(SearchFiltersBottomSheet.RESULT_RARITY),
                type = bundle.getString(SearchFiltersBottomSheet.RESULT_TYPE),
                category = bundle.getString(SearchFiltersBottomSheet.RESULT_CATEGORY),
                setId = bundle.getString(SearchFiltersBottomSheet.RESULT_SET_ID),
                setName = bundle.getString(SearchFiltersBottomSheet.RESULT_SET_NAME)
            )
            triggerSearch(binding.searchInput.text?.toString().orEmpty())
        }

        binding.filterIcon.setOnClickListener {
            SearchFiltersBottomSheet.newInstance(currentFilters)
                .show(childFragmentManager, "search_filters")
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery.value = s?.toString() ?: ""
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        lifecycleScope.launch {
            searchQuery
                .debounce(500)
                .collectLatest { query ->
                    triggerSearch(query)
                }
        }

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@SearchFragment.adapter
        }
        viewModel.resultsSearchCards.observe(viewLifecycleOwner) {
            adapter.cardsResults = it.toMutableList()
            adapter.notifyDataSetChanged()
        }

        viewModel.loadingSearchCard.observe(viewLifecycleOwner) { loading ->
            val loadingMessages = resources.getStringArray(R.array.search_loadings_txts).toList()

            if (loading) {
                binding.loading.loadingContainer.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
                loadingManager.startLoading(loadingMessages)
            } else {
                binding.loading.loadingContainer.visibility = View.GONE
                binding.rvSearch.visibility = View.VISIBLE
                loadingManager.stopLoading()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun triggerSearch(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank() && !currentFilters.hasApiFilters()) {
            adapter.cardsResults = mutableListOf()
            adapter.notifyDataSetChanged()
            return
        }

        if (trimmedQuery.length < 3 && !currentFilters.hasApiFilters()) {
            adapter.cardsResults = mutableListOf()
            adapter.notifyDataSetChanged()
            return
        }

        lifecycleScope.launch {
            val cardQuery = CardQuery(
                name = trimmedQuery,
                type = currentFilters.type,
                category = currentFilters.category,
                set = currentFilters.setId,
                rarity = currentFilters.rarity,
                page = 1,
                pageSize = 50,
            )

            Log.d("SearchFragment", "triggerSearch: $cardQuery")
            viewModel.searchCard(cardQuery)
        }
    }
}
