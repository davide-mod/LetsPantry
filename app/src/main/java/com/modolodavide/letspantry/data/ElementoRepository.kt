package com.modolodavide.letspantry.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElementoRepository(app: Application) {
    var elementoData = MutableLiveData<List<Elemento>>()
    private val elementoDAO = ElementoDatabase.getDatabase(app).elementoDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            //elementoDAO.deleteAll()
            val data: List<Elemento>? = elementoDAO.getAll()
            if (data.isNullOrEmpty()) {
                /*for(i in 1..4)
                    elementoDAO.insertElemento(randomElemento())
                data = elementoDAO.getAll()
                elementoData.postValue(data)*/
            } else {
                elementoData.postValue(data)
            }
        }
    }

    fun insertElemento(elemento: Elemento) {
        CoroutineScope(Dispatchers.IO).launch {
            elementoDAO.insertElemento(elemento)
            elementoData.postValue(elementoDAO.getAll())
        }
    }

    fun deleteElemento(elemento: Elemento) {
        CoroutineScope(Dispatchers.IO).launch {
            elementoDAO.deleteElemento(elemento)
            elementoData.postValue(elementoDAO.getAll())
        }
    }

    fun updateElemento(id: Int, testo: String, preso: Boolean, quantita: Double){
        CoroutineScope(Dispatchers.IO).launch {
            elementoDAO.updateElemento(id, testo, preso, quantita)
            elementoData.postValue(elementoDAO.getAll())
        }
    }
    private fun randomElemento(): Elemento{
        val valore = (1..30).random()
        return (Elemento(0, "Ingrediente$valore lista", valore%2==0, 20.0))

    }

}