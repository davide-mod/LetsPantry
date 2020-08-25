package com.modolodavide.letspantry.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.modolodavide.letspantry.data.Ingrediente
import com.modolodavide.letspantry.data.IngredienteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.addAll

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = IngredienteRepository(app)
    var ingredienteList = MutableLiveData<List<Ingrediente>>()
    init{
        ingredienteList=db.ingredienteData
    }

    fun insertIngrediente(ingrediente: Ingrediente) {
        db.insertIngrediente(ingrediente)
    }
}