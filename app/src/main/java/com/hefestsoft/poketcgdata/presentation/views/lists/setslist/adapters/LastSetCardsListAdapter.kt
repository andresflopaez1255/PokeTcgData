package com.hefestsoft.poketcgdata.presentation.views.lists.setslist.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.databinding.ItemCardBinding

class LastSetCardsListAdapter(private val onCardClick: (String) -> Unit) : RecyclerView.Adapter<LastSetCardsListAdapter.ViewHolder>() {
    var cardsSets: MutableList<CardResumeDto>  = ArrayList()
    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.binding.txtViewNumberCard.text = "# ${item.localID}/${item.set.total}"

        holder.binding.CVCard.setOnClickListener {
            onCardClick(item.id)
        }


    }


    override fun getItemCount(): Int = cardsSets.size


    inner class ViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)




}
