package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.hefestsoft.poketcgdata.databinding.FragmentHomeBinding
import androidx.core.graphics.toColorInt
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import coil.load
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import com.hefestsoft.poketcgdata.R
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.SetCardsListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetsViewModels by viewModels()
    private lateinit var adapter: SetCardsListAdapter


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


        adapter = SetCardsListAdapter()

        viewModel.getSetsCards()

        binding.searchInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.searchInput.clearFocus()
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
            }
        }

        // 2. NOW SET UP THE RECYCLERVIEW
        binding.rvCardsSet.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@HomeFragment.adapter // Now it is initialized!
        }

        viewModel.loadingSetCards.observe(viewLifecycleOwner){ loading ->
            if (loading){
                    binding.loadingContainer.visibility = View.VISIBLE
                    binding.rvCardsSet.visibility = View.GONE
                    binding.txtTitleSet.visibility = View.GONE


            }else{
                binding.rvCardsSet.visibility = View.VISIBLE
                binding.txtTitleSet.visibility = View.VISIBLE
                binding.loadingContainer.visibility = View.GONE
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


