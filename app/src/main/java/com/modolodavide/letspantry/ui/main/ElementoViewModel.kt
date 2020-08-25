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
}