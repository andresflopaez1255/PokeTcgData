package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private val searchQuery = MutableStateFlow("")

    private lateinit var adapter: SearchListAdapter
    private lateinit var loadingManager: LoadingManager



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
            binding.txtLoadingSubtitle,
            binding.loadingContainer
        )

        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
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
                .filter { it.length >= 3 }
                .collectLatest { query ->
                    val cardQuery: CardQuery = CardQuery(query,
                        null,
                        null,
                        null,
                        null,
                        null,
                     )
                     viewModel.searchCard(cardQuery)
                }
        }

        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@SearchFragment.adapter
        }
        viewModel.resultsSearchCards.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.cardsResults = it.toMutableList()
                adapter.notifyDataSetChanged()
            }




        }

        viewModel.loadingSearchCard.observe(viewLifecycleOwner) { loading ->
            val loadingMessages = resources.getStringArray(R.array.search_loadings_txts).toList()

            if (loading) {
                binding.loadingContainer.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
                loadingManager.startLoading(loadingMessages)
            } else {
                binding.loadingContainer.visibility = View.GONE
                binding.rvSearch.visibility = View.VISIBLE
                loadingManager.stopLoading()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
