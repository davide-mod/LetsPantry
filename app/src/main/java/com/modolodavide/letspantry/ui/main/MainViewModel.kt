package com.modolodavide.letspantry.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.modolodavide.letspantry.data.Ingrediente
import com.modolodavide.letspantry.data.IngredienteRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = IngredienteRepository(app)
    var ingredienteList = MutableLiveData<List<Ingrediente>>()
    init{
        ingredienteList=db.ingredienteData
    }

    fun insertIngrediente(ingrediente: Ingrediente) {
        db.insertIngrediente(ingrediente)
    }
    fun deleteIngrediente(ingrediente: Ingrediente) {
        db.deleteIngrediente(ingrediente)
    }
    fun updateIngrediente(ingrediente: Ingrediente) {
        db.updateIngrediente(ingrediente.nome, ingrediente.scadenzaAnno, ingrediente.scadenzaMese, ingrediente.scadenzaGiorno, ingrediente.quantita, ingrediente.id)
    }
}