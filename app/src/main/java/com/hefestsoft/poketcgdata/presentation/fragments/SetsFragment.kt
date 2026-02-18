package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.databinding.FragmentSetsBinding
import com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters.SetListAdapter
import com.hefestsoft.poketcgdata.presentation.viewsModels.SetsViewModels
import com.hefestsoft.poketcgdata.utils.LoadingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetsFragment : Fragment() {
    private lateinit var binding: FragmentSetsBinding

    private lateinit var adapter: SetListAdapter
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


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SetListAdapter()

        viewModel.loadingSetList.observe(viewLifecycleOwner) { loading ->
            if (loading) {

            }
        }


        viewModel.setsListData.observe(viewLifecycleOwner) { sets ->
            adapter.sets = sets.reversed().filter { set ->
                set.logo != null
            }.toMutableList()

            adapter.notifyDataSetChanged()

        }

        binding.rvSet.apply {
            adapter = this@SetsFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }



    }
}