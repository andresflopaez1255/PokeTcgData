package com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.databinding.ItemSetCardBinding
import com.hefestsoft.poketcgdata.presentation.fragments.HomeFragmentDirections
import com.squareup.picasso.Picasso

class SetCardsListAdapter: RecyclerView.Adapter<SetCardsListAdapter.ViewHolder>() {
    var cardsSets: MutableList<CardResumeDto>  = ArrayList()
    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {


        val item = cardsSets[position]
        holder.binding.imageView.load(item.image) {
            crossfade(true)
            crossfade(1000)


        }

        holder.binding.txtViewNameCard.text = item.name
        holder.binding.txtViewRarityCard.text = item.rarity
        holder.binding.txtViewNumberCard.text = "set: ${item.localID}/${item.set.total}"

        holder.binding.CVCard.setOnClickListener {
           findNavController( holder.binding.root).navigate( HomeFragmentDirections.actionHomeFragmentToDetailFragment(item.id))

        }


    }


    override fun getItemCount(): Int = cardsSets.size


    inner class ViewHolder(val binding: ItemSetCardBinding) : RecyclerView.ViewHolder(binding.root)




}