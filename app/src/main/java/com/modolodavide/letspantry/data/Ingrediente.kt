package com.modolodavide.letspantry.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dispensa")
data class Ingrediente(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome: String,
    val scadenzaAnno: Int,
    val scadenzaMese: Int,
    val scadenzaGiorno: Int,
    val quantita: Double
)