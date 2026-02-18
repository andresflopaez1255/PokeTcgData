package com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.SetDTO
import com.hefestsoft.poketcgdata.databinding.ItemSetCardBinding

class SetListAdapter() : RecyclerView.Adapter<SetListAdapter.ViewHolder>() {
    var sets: MutableList<SetDTO> = ArrayList()
    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemSetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = sets[position]
        holder.binding.txtSetTitle.text = item.name
        holder.binding.imgSetCard.load("${item.logo}.webp") {
            crossfade(true)
            crossfade(1000)

        }


    }

    override fun getItemCount() = sets.size



    inner class ViewHolder(val binding: ItemSetCardBinding) : RecyclerView.ViewHolder(binding.root)

}