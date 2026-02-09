package com.hefestsoft.poketcgdata.utils

import java.text.Normalizer
import java.util.Locale

/**
 * Convierte un string a un formato slug amigable para URLs,
 * convirtiendo el apóstrofe (') en %27.
 */
fun slugify(value: String): String {
    return Normalizer.normalize(value, Normalizer.Form.NFD)
        .replace("'", "%27") // Cambia el apóstrofe por %27
        .replace(Regex("[\\u0300-\\u036f]"), "") // Elimina acentos
        .lowercase(Locale.ROOT)
        .replace(Regex("[^a-z0-9%]+"), "-") // Reemplaza lo que no sea letra, número o % por guion
        .replace(Regex("(^-|-$)"), "") // Elimina guiones sobrantes al inicio o fin
}

fun normalizeLocalId(localId: String): String {
    return if (localId.matches(Regex("^\\d+$"))) {
        localId.toIntOrNull()?.toString() ?: localId
    } else {
        localId
    }
}
