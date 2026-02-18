package com.hefestsoft.poketcgdata.presentation.views.lists.searchList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.CardResumeDto
import com.hefestsoft.poketcgdata.databinding.ItemCardBinding


class SearchListAdapter(private val onCardClick: (String) -> Unit): RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    lateinit var context: Context
    var cardsResults: MutableList<CardResumeDto>  = ArrayList()
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
        val item = cardsResults[position]
        holder.binding.imageView.load(item.image)
        holder.binding.txtViewNameCard.text = item.name
        holder.binding.txtViewRarityCard.text = item.rarity
        holder.binding.txtViewNumberCard.text = "# ${item.localID}/" +
                "${item.set.total}"
        holder.binding.CVCard.setOnClickListener {
            onCardClick(item.id)
        }


    }

    override fun getItemCount() = cardsResults.size

    class ViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root)
}
