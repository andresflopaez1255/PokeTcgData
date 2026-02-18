package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hefestsoft.poketcgdata.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.LastSetCardsListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetsViewModels by viewModels()
    private lateinit var adapter: LastSetCardsListAdapter
    private lateinit var loadingManager: LoadingManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = LastSetCardsListAdapter { cardId ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(cardId)
            findNavController().navigate(action)
        }



        loadingManager = LoadingManager(
            lifecycleScope,
            binding.txtLoadingSubtitle,
            binding.loadingContainer
        )

        binding.searchInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchInput.clearFocus()
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
            }
        }


        binding.rvCardsSet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@HomeFragment.adapter
        }

        viewModel.loadingSetCards.observe(viewLifecycleOwner){ loading ->
            val cardsStringsLoading = resources.getStringArray(R.array.home_loadings_txts).toList()
            if (loading){
                    binding.loadingContainer.visibility = View.VISIBLE
                    binding.rvCardsSet.visibility = View.GONE
                    binding.txtTitleSet.visibility = View.GONE
                    loadingManager.startLoading(cardsStringsLoading)
            }else{
                binding.rvCardsSet.visibility = View.VISIBLE
                binding.txtTitleSet.visibility = View.VISIBLE
                binding.loadingContainer.visibility = View.GONE
                loadingManager.stopLoading()
            }
        }

        viewModel.setsCardsData.observe(viewLifecycleOwner) { sets ->
            if (sets.isNotEmpty()) {
                binding.txtTitleSet.text = sets[0].set.name


                adapter.cardsSets = sets.toMutableList()
                adapter.notifyDataSetChanged()

            }
        }
    }



    }
