package com.hefestsoft.poketcgdata.utils

import com.hefestsoft.poketcgdata.R

enum class EnergyType {
    Colorless,
    Darkness,
    Dragon,
    Fairy,
    Fighting,
    Fire,
    Grass,
    Lightning,
    Metal,
    Psychic,
    Water
}


fun mapEnergy(energy: EnergyType): Int {
    return when (energy) {
        EnergyType.Colorless -> R.drawable.incolorus_energy
        EnergyType.Darkness -> R.drawable.dark_energy
        EnergyType.Dragon -> R.drawable.dragon_energy
        EnergyType.Fairy -> R.drawable.fairy_energy
        EnergyType.Fighting -> R.drawable.figth_energy
        EnergyType.Fire -> R.drawable.fire_energy
        EnergyType.Grass -> R.drawable.grass_energy
        EnergyType.Lightning -> R.drawable.lightning_energy
        EnergyType.Metal -> R.drawable.metal_energy
        EnergyType.Psychic -> R.drawable.psychic_energy
        EnergyType.Water -> R.drawable.incolorus_energy







    }
}
