package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.databinding.CardDetailsBinding
import com.hefestsoft.poketcgdata.databinding.CardSupporterDetailsBinding
import com.hefestsoft.poketcgdata.databinding.FragmentDetailBinding
import com.hefestsoft.poketcgdata.databinding.ItemAttackBinding
import com.hefestsoft.poketcgdata.presentation.viewsModels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.getString("id")
        id?.let { viewModel.getCardById(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        viewModel.detailCardData.observe(viewLifecycleOwner) { card ->
           when(card.category) {

               "Trainer" -> {
                   mapCardSupporter(card)
               }

               "Pokemon" -> {
                   mapCard(card)
               }

               else -> {
                   mapCardSupporter(card)
               }

           }
        }
    }

    fun mapCard(card: CardDetailDTO) {
        val cardDetailsBinding = CardDetailsBinding.inflate(layoutInflater, binding.detailFrame, false)
        binding.txtToolbar.text= card.name
        cardDetailsBinding.imgCard.load("${card.image}/high.webp")
        cardDetailsBinding.txtPrice.text = card.pricing.tcgplayer?.holofoil?.market.toString()
        
        if (card.abilities.isNullOrEmpty()) {
            cardDetailsBinding.cardAbility.visibility = View.GONE
        } else {
            cardDetailsBinding.cardAbility.visibility = View.VISIBLE
            cardDetailsBinding.txtAbility.text = card.abilities[0].name
            cardDetailsBinding.txtAbilityresume.text = card.abilities[0].effect
        }

        cardDetailsBinding.containerAttacks.removeAllViews()
        card.attacks?.forEach { attack ->
            val attackBinding = ItemAttackBinding.inflate(layoutInflater, cardDetailsBinding.containerAttacks, false)
            attackBinding.txtNameAttack.text = attack.name
            attackBinding.txtDescAttack.text = attack.effect
            cardDetailsBinding.containerAttacks.addView(attackBinding.root)
        }

        binding.detailFrame.removeAllViews()
        binding.detailFrame.addView(cardDetailsBinding.root)
    }

    @SuppressLint("SetTextI18n")
    fun mapCardSupporter(card: CardDetailDTO) {
        val cardDetailsBinding = CardSupporterDetailsBinding.inflate(layoutInflater, binding.detailFrame, false)
        val codeCard = "${card.localId}/${card.set.cardCount.official}"
        binding.txtToolbar.text= "${card.name} #${codeCard}"
        cardDetailsBinding.imgCard.load("${card.image}/high.webp")
        cardDetailsBinding.txtPrice.text = card.pricing.cardmarket?.avg.toString()

        cardDetailsBinding.txtDescriptionResume.text = card.effect
        
        cardDetailsBinding.txtStatsSet.text = card.set.name
        cardDetailsBinding.txtSetNumber.text = codeCard
        cardDetailsBinding.txtRarity.text = card.rarity
        cardDetailsBinding.txtRmark.text = card.regulationMark
        cardDetailsBinding.txtCardFormat.text = if (card.legal.standard) "Standard" else "Expanded"
        
        if (!card.illustrator.isNullOrEmpty()) {
            cardDetailsBinding.txtIllustration.text = card.illustrator
            cardDetailsBinding.txtIllustration.visibility = View.VISIBLE
            cardDetailsBinding.txtTitleIllustration.visibility = View.VISIBLE
        } else {
            cardDetailsBinding.txtIllustration.visibility = View.GONE
            cardDetailsBinding.txtTitleIllustration.visibility = View.GONE
        }

        cardDetailsBinding.imgSymbolSet.load("${card.set.symbol}.webp")

        binding.detailFrame.removeAllViews()
        binding.detailFrame.addView(cardDetailsBinding.root)
    }
}