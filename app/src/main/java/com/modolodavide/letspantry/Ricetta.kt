package com.modolodavide.letspantry

data class Ricetta(
    val Nome: String,
    val Tipo_Piatto: String,
    val Ing_Principale: String,
    val Persone: Int,
    val Note: String,
    val Ingredienti: String,
    val Preparazione: String
)