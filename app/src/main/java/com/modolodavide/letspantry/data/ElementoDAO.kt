package com.modolodavide.letspantry.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ElementoDAO {

    @Query("SELECT * FROM listaspesa")
    suspend fun getAll(): List<Elemento>

    @Insert
    suspend fun insertElemento(elemento: Elemento)

    @Delete
    suspend fun deleteElemento(elemento: Elemento)

    @Query("UPDATE listaspesa SET testo=:testo, preso=:preso, quantita=:quantita WHERE id=:id")
    suspend fun updateElemento(id: Int, testo: String, preso: Boolean, quantita: Double)

    @Query("DELETE FROM listaspesa")
    suspend fun deleteAll()

}