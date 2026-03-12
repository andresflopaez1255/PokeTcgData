package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hefestsoft.poketcgdata.databinding.FragmentHomeBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.LastSetCardsListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import com.hefestsoft.poketcgdata.utils.LoadingAnimation
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
            binding.loading.txtLoadingSubtitle,
            binding.loading.loadingContainer,
            binding.loading.loadingView,
            LoadingAnimation.LoadingCards

        )

        binding.searchInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchInput.clearFocus()
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
            }
        }


        val layoutManager = LinearLayoutManager(requireContext())
        binding.layoutSetList.rvCardsSet.apply {
            this.layoutManager = layoutManager
            this.adapter = this@HomeFragment.adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 5) {
                            viewModel.getLastSetsCardsList(isNextPage = true)
                        }
                    }
                }
            })
        }

        viewModel.loadingSetCards.observe(viewLifecycleOwner){ loading ->
            val cardsStringsLoading = resources.getStringArray(R.array.home_loadings_txts).toList()
            if (loading){
                    binding.loading.loadingContainer.visibility = View.VISIBLE
                    binding.layoutSetList.root.visibility = View.GONE
                    loadingManager.startLoading(cardsStringsLoading)
            }else{
                binding.layoutSetList.root.visibility = View.VISIBLE
                binding.loading.loadingContainer.visibility = View.GONE
                loadingManager.stopLoading()
            }
        }

        viewModel.setsCardsData.observe(viewLifecycleOwner) { sets ->
            if (sets.isNotEmpty()) {
                binding.layoutSetList.txtTitleSet.text = sets[0].set.name




                adapter.cardsSets = sets.toMutableList()
                adapter.notifyDataSetChanged()

            }
        }
    }



    }
