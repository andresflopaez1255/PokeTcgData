package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.databinding.FragmentSetCardListBinding
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.LastSetCardsListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import com.hefestsoft.poketcgdata.utils.LoadingAnimation
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetCardListFragment : Fragment() {

    companion object {
        fun newInstance() = SetCardListFragment()
    }

    private val viewModel: SetsViewModels by viewModels()
    private lateinit var binding: FragmentSetCardListBinding
    private lateinit var adapter: LastSetCardsListAdapter
    private lateinit var loadingManager: LoadingManager
    var setId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setId = arguments?.getString("setId")
        setId?.let { viewModel.getCardsSet(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        loadingManager = LoadingManager(
            lifecycleScope,
            binding.loading.txtLoadingSubtitle,
            binding.loading.loadingContainer,
            binding.loading.loadingView,
            LoadingAnimation.LoadingCards
        )
        
        adapter = LastSetCardsListAdapter { cardId ->
            val action = SetCardListFragmentDirections.actionSetCardListFragmentToDetailFragment(cardId)
            findNavController().navigate(action)
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.layoutSetList.rvCardsSet.apply {
            this.layoutManager = layoutManager
            this.adapter = this@SetCardListFragment.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()


                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 5) {
                            viewModel.getCardsSet(setId.toString(), isNextPage = true)
                        }
                    }
                }
            })
        }

        viewModel.cardsSetData.observe(viewLifecycleOwner) { cards ->
            if (cards.isNotEmpty()) {
                binding.layoutSetList.txtTitleSet.visibility = View.GONE
                binding.toolbar.txtToolbar.text = cards[0].set.name

                adapter.cardsSets = cards.toMutableList()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.loadingCardsSet.observe(viewLifecycleOwner) { loading ->
            val detailCardLoading = resources.getStringArray(R.array.home_loadings_txts).toList()
            if (loading) {
                binding.loading.loadingContainer.visibility = View.VISIBLE
                binding.layoutSetContainer.visibility = View.GONE
                loadingManager.startLoading(detailCardLoading)

            } else {
                binding.loading.loadingContainer.visibility = View.GONE
                binding.layoutSetContainer.visibility = View.VISIBLE
                loadingManager.stopLoading()
            }


        }
    }
}
