package com.modolodavide.letspantry.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredienteDAO {

    @Query("SELECT * FROM dispensa ORDER BY nome")
    suspend fun getAll(): List<Ingrediente>

    @Insert
    suspend fun insertIngrediente(ingrediente: Ingrediente)

    @Delete
    suspend fun deleteIngrediente(ingrediente: Ingrediente)

    @Query("UPDATE dispensa SET nome=:nome, scadenzaAnno=:scadenzaAnno, scadenzaMese=:scadenzaMese, scadenzaGiorno=:scadenzaGiorno, quantita=:quantita WHERE id=:id")
    suspend fun updateIngrediente(nome: String, scadenzaAnno: Int, scadenzaMese: Int, scadenzaGiorno: Int, quantita: Double, id: Int)

    //Query per trovare gli ingredienti che scadono in un determinato giorno, non viene usata in favore di un metodo eventualmente più lento (su un grande numero di ingredienti) ma più intuitivo
    @Query("SELECT * FROM dispensa WHERE scadenzaAnno=:scadenzaAnno AND scadenzaMese=:scadenzaMese AND scadenzaGiorno=:scadenzaGiorno")
    suspend fun thatDayList(scadenzaAnno: Int, scadenzaMese: Int, scadenzaGiorno: Int): List<Ingrediente>

    @Query("DELETE FROM dispensa")
    suspend fun deleteAll()
}