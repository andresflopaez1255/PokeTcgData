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
import androidx.recyclerview.widget.GridLayoutManager
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.data.dtos.CardQuery
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.databinding.FragmentSetsBinding
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.SetListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import com.hefestsoft.poketcgdata.utils.LoadingAnimation
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetsFragment : Fragment() {
    private lateinit var binding: FragmentSetsBinding

    private lateinit var adapter: SetListAdapter

    private val searchQuery = MutableStateFlow("")

    private lateinit var loadingManager: LoadingManager

    companion object {
        fun newInstance() = SetsFragment()
    }

    private val viewModel: SetsViewModels by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetsBinding.inflate(inflater, container, false)
        return binding.root

    }


    @OptIn(FlowPreview::class)
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            arrowBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            txtToolbar.text = getString(R.string.sets_toolbar_title)
        }

        loadingManager = LoadingManager(
            lifecycleScope,
            binding.loading.txtLoadingSubtitle,
            binding.loading.loadingContainer,
            binding.loading.loadingView,
            LoadingAnimation.LoadingInfo
        )

        adapter = SetListAdapter { set ->
            val action = SetsFragmentDirections.actionSetsFragmentToSetCardListFragment(set.id)
            findNavController().navigate(action)
        }

        viewModel.loadingSetList.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.loading.loadingContainer.visibility = View.VISIBLE
                binding.rvSet.visibility = View.GONE
                loadingManager.startLoading(listOf(getString(R.string.loading_sets)))

            } else {
                binding.loading.loadingContainer.visibility = View.GONE
                binding.rvSet.visibility = View.VISIBLE
                loadingManager.stopLoading()
            }
        }

        lifecycleScope.launch {
            searchQuery
                .debounce(500)
                .filter { it.length >= 3 }
                .collectLatest { query ->

                    viewModel.searchSets(query)
                }
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               if (s.isNullOrEmpty()){
                   viewModel.getSets()
               }else{
                   searchQuery.value = s.toString()
               }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        viewModel.setsListData.observe(viewLifecycleOwner) { sets ->
            adapter.sets = sets.reversed().filter { set ->
                set.logo != null && set.logo != ""
            }.toMutableList()

            adapter.notifyDataSetChanged()

        }

        binding.rvSet.apply {
            adapter = this@SetsFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }



    }
}
