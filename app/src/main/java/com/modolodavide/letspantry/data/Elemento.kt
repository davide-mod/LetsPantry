package com.modolodavide.letspantry.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "listaspesa")
data class Elemento(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val testo: String,
    val preso: Boolean,
    val quantita: Double
): Serializable