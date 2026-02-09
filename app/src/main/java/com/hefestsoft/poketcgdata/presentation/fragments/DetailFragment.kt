package com.hefestsoft.poketcgdata.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import coil.load
import com.hefestsoft.poketcgdata.data.dtos.CardDetailDTO
import com.hefestsoft.poketcgdata.data.dtos.PriceChartingDto
import com.hefestsoft.poketcgdata.databinding.CardDetailsBinding
import com.hefestsoft.poketcgdata.databinding.CardSupporterDetailsBinding
import com.hefestsoft.poketcgdata.databinding.FragmentDetailBinding
import com.hefestsoft.poketcgdata.databinding.ItemAttackBinding
import com.hefestsoft.poketcgdata.databinding.CardPrincingBinding
import com.hefestsoft.poketcgdata.presentation.viewsModels.DetailViewModel
import com.hefestsoft.poketcgdata.utils.mapEnergy
import com.hefestsoft.poketcgdata.utils.normalizeLocalId
import com.hefestsoft.poketcgdata.utils.slugify
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    val priceMap = mutableMapOf<String, String>()


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
            val  cardSlug = slugify("${card.name}-${normalizeLocalId(card.localId)}")
            val setSlug = slugify("pokemon-${card.set.name}")
            val slug = "$setSlug/$cardSlug"
            Log.d("slug", slug)
            viewModel.getPriceChartingData(cardSlug, setSlug)
            viewModel.priceChartingData.observe(viewLifecycleOwner) { response ->
                validatePriceState("cardmarket", card, response)
                when(card.category) {

                    "Trainer" -> {
                        mapCardSupporter(card, response)
                    }

                    "Pokemon" -> {
                        mapCard(card, response)
                    }

                    else -> {
                        mapCardSupporter(card, response)
                    }

                }


            }

        }
    }


    fun validatePriceState(preferredPrice: String, card: CardDetailDTO, response: PriceChartingDto?): Map<String, String> {

        when (preferredPrice) {
            "tcgplayer" -> {
                run {
                    priceMap["name"] = "TCGPlayer"
                    priceMap["price"] = if (card.pricing.tcgplayer?.holofoil?.market != null) card.pricing.tcgplayer.holofoil.market.toString() else "N/A"
                    priceMap["previous"] = card.pricing.tcgplayer?.holofoil?.low.toString()
                    priceMap["unit"] = card.pricing.tcgplayer?.unit.toString()

                }
            }
            "cardmarket" -> {
                run {
                    priceMap["name"] = "CardMarket"
                    priceMap["price"] = if (card.pricing.cardmarket?.avg != null) card.pricing.cardmarket.avg.toString() else "N/A"
                    priceMap["previous"] = card.pricing.cardmarket?.low.toString()
                    priceMap["unit"] = card.pricing.cardmarket?.unit.toString()


                }
            }

            "pricecharting" -> {


                    run {
                        priceMap["name"] = "PriceCharting"
                        priceMap["price"] = if(response?.currentPrice != null) response.currentPrice.toString() else "N/A"
                        priceMap["previous"] = if(response?.previousPrice != null) response.previousPrice.toString() else "N/A"
                        priceMap["unit"] = "USD"


                    }




            }



        }
        return priceMap

    }


    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    fun mapCard(card: CardDetailDTO, priceChartingResponse: PriceChartingDto?) {
        val cardDetailsBinding = CardDetailsBinding.inflate(layoutInflater, binding.detailFrame, false)
        binding.txtToolbar.text= card.name
        cardDetailsBinding.imgCard.load("${card.image}/high.webp")

        cardDetailsBinding.txtTitlePrice.text = priceMap["name"]
        cardDetailsBinding.txtPrice.text = if (priceMap["price"] == "N/A") "N/A" else "${priceMap["price"]} ${priceMap["unit"]}"

        setupPricingContainer(cardDetailsBinding.containerPrices, card, priceChartingResponse)
        
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
              if (attack.effect.isNullOrEmpty()){
                  attackBinding.txtDescAttack.visibility = View.GONE
              } else {
                  attackBinding.txtDescAttack.text = attack.effect
              }
            attackBinding.txtAttacksScore.text = attack.damage
            val imagesEnergyList: MutableList<ImageView> = mutableListOf()
            attack.cost.forEach { energy ->
                val imageView = ImageView(requireContext())
                imageView.layoutParams = ViewGroup.LayoutParams(90, 90)
                imageView.cropToPadding = true
                imageView.setPadding(12)
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.adjustViewBounds = true

                imageView.load(mapEnergy(energy))
                imagesEnergyList.add(imageView)
                attackBinding.energyContainer.addView(imageView)
            }




            cardDetailsBinding.containerAttacks.addView(attackBinding.root)
        }
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

        cardDetailsBinding.txtStatsSet.text = card.set.name
        cardDetailsBinding.txtSetNumber.text = "${card.localId}/${card.set.cardCount.official}"
        cardDetailsBinding.imgSymbolSet.load("${card.set.symbol}.webp")

        binding.detailFrame.removeAllViews()
        binding.detailFrame.addView(cardDetailsBinding.root)
    }

    @SuppressLint("SetTextI18n")
    fun mapCardSupporter(card: CardDetailDTO, priceChartingResponse: PriceChartingDto?) {
        val cardDetailsBinding = CardSupporterDetailsBinding.inflate(layoutInflater, binding.detailFrame, false)
        val codeCard = "${card.localId}/${card.set.cardCount.official}"
        binding.txtToolbar.text= "${card.name} #${codeCard}"
        cardDetailsBinding.imgCard.load("${card.image}/high.webp")
        cardDetailsBinding.txtTitlePrice.text = priceMap["name"]
        cardDetailsBinding.txtPrice.text = if (priceMap["price"] == "N/A") "N/A" else "${priceMap["price"]} ${priceMap["unit"]}"

        setupPricingContainer(cardDetailsBinding.containerPrices, card, priceChartingResponse)

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

    @SuppressLint("SetTextI18n")
    private fun setupPricingContainer(container: ViewGroup, card: CardDetailDTO, priceChartingResponse: PriceChartingDto?) {
        container.removeAllViews()

        // CardMarket
        card.pricing.cardmarket?.let { cm ->
            val cmBinding = CardPrincingBinding.inflate(layoutInflater, container, false)
            cmBinding.txtPriceSection.text = card.variants.toString()
            if(card.variants.normal){
                cmBinding.txtPriceValueSection.text = if(cm.avg != 0.0) "${cm.avg} ${cm.unit}" else "N/A"
                cmBinding.txtPriceChange.text = "Trend: ${cm.avg7}"
            } else{
                cmBinding.txtPriceValueSection.text = if(cm.avgHolo != 0.0) "${cm.avgHolo} ${cm.unit}" else "N/A"
                cmBinding.txtPriceChange.text = "okiii: ${cm.avg}"
            }
            container.addView(cmBinding.root)
        }

        // TCGPlayer
        card.pricing.tcgplayer?.let { tp ->
            val tpBinding = CardPrincingBinding.inflate(layoutInflater, container, false)
            tpBinding.txtPriceSection.text = "TCGPlayer"
            tpBinding.txtPriceValueSection.text = if(tp.holofoil.market != null) "${tp.holofoil.market} USD" else "N/A"

            tpBinding.txtPriceChange.text = "Low: ${tp.holofoil.low ?: "N/A"}"
            container.addView(tpBinding.root)
        }

        // PriceCharting
        priceChartingResponse?.let { pc ->
            val pcBinding = CardPrincingBinding.inflate(layoutInflater, container, false)
            pcBinding.txtPriceSection.text = "PriceCharting"
            pcBinding.txtPriceValueSection.text = if(pc.currentPrice != null) "${pc.currentPrice} USD" else "N/A"
            pcBinding.txtPriceChange.text = "Prev: ${pc.previousPrice ?: "N/A"}"
            container.addView(pcBinding.root)
        }
    }
}
