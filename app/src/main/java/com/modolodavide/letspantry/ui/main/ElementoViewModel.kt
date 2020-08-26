package com.modolodavide.letspantry.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.modolodavide.letspantry.data.Elemento
import com.modolodavide.letspantry.data.ElementoRepository

class ElementoViewModel(app: Application) : AndroidViewModel(app){
    private val db = ElementoRepository(app)
    var elementoList = MutableLiveData<List<Elemento>>()
    init{
        elementoList=db.elementoData
    }

    fun insertElemento(elemento: Elemento)
    {
        db.insertElemento(elemento)
    }
    fun updateElemento(elemento: Elemento)
    {
        db.updateElemento(elemento.id, elemento.testo, elemento.preso, elemento.quantita)
    }
    fun prendiElemento(elemento: Elemento)
    {
        db.updateElemento(elemento.id, elemento.testo, !elemento.preso, elemento.quantita)
    }
    fun deleteElemento(elemento: Elemento)
    {
        db.deleteElemento(elemento)
    }
}