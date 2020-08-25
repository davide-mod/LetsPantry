package com.modolodavide.letspantry.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredienteRepository(app: Application) {
    var ingredienteData = MutableLiveData<List<Ingrediente>>()
    private val ingredienteDAO = IngredienteDatabase.getDatabase(app)
        .ingredienteDAO()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            ingredienteDAO.deleteAll()
            var data: List<Ingrediente>? = ingredienteDAO.getAll()
            if (data.isNullOrEmpty()) {
                for(i in 1..3)
                    ingredienteDAO.insertIngrediente(randomIngrediente())
                data = ingredienteDAO.getAll()
                ingredienteData.postValue(data)
            } else {
                ingredienteData.postValue(data)
            }
        }
    }

    fun insertIngrediente(ingrediente: Ingrediente) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredienteDAO.insertIngrediente(ingrediente)
            ingredienteData.postValue(ingredienteDAO.getAll())
        }
    }

    fun deleteIngrediente(ingrediente: Ingrediente) {
        CoroutineScope(Dispatchers.IO).launch {
            ingredienteDAO.deleteIngrediente(ingrediente)
            ingredienteData.postValue(ingredienteDAO.getAll())
        }
    }

    fun updateIngrediente(nome: String, scadenzaAnno: Int, scadenzaMese: Int, scadenzaGiorno: Int, quantita: Double, id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            ingredienteDAO.updateIngrediente(nome, scadenzaAnno, scadenzaMese, scadenzaGiorno, quantita, id)
            ingredienteData.postValue(ingredienteDAO.getAll())
        }
    }
    private fun randomIngrediente(): Ingrediente{
        return (Ingrediente(0, "Ingrediente"+(1..30).random(), 2020, (8..9).random(), (1..30).random(), (1..1000).random().toDouble()))

    }

    fun refresh(){
        CoroutineScope(Dispatchers.IO).launch {
            ingredienteData.postValue(ingredienteDAO.getAll())
        }
    }

}