package com.modolodavide.letspantry.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Elemento
import com.modolodavide.letspantry.data.Ingrediente

class ListaSpesaFragment : Fragment(), ElementoAdapter.ElementoListener {

    companion object {
        fun newInstance() = ListaSpesaFragment()
    }

    private lateinit var elemVM: ElementoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        elemVM = ViewModelProvider(this).get(ElementoViewModel::class.java)
        val view = inflater.inflate(R.layout.listaspesa_fragment, container, false)
        val listaElementi = view.findViewById<RecyclerView>(R.id.viewListaSpesa)
        elemVM.elementoList.observe(viewLifecycleOwner, {
            val presi = mutableListOf<Elemento>()
            val nonpresi = mutableListOf<Elemento>()
            it.forEach { elemento ->
                if(elemento.preso)
                    presi.add(elemento)
                else
                    nonpresi.add(elemento)
            }
            //https://stackoverflow.com/questions/53076904/android-kotlin-recyclerview-find-out-what-exactly-inside-item-was-clicked-in-a
            val adapter = ElementoAdapter(requireContext(), nonpresi+presi, this)
            listaElementi.adapter = adapter
        })
        val btnNuovo: TextView = view.findViewById(R.id.btnAggiungi)
        btnNuovo.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.nuovo_elemento)
            val newNome = dialog.findViewById<EditText>(R.id.editNome)
            val newQuantita = dialog.findViewById<EditText>(R.id.editQuantita)
            val btnAdd2 = dialog.findViewById<TextView>(R.id.btnAggiungiElemento)
            btnAdd2.setOnClickListener {
                var nome = "Elemento senza nome"
                var quantita = "0"
                if (newNome.text.toString() != "") nome = newNome.text.toString()
                if (newQuantita.text.toString() != "") quantita = newQuantita.text.toString()
                elemVM.insertElemento(
                    Elemento(
                        0,
                        nome,
                        false,
                        quantita.toDouble()
                    )
                )
                dialog.dismiss()
            }
            dialog.show()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        elemVM = ViewModelProvider(this).get(ElementoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onElementoListener(elemento: Elemento, position: Int, longpress: Boolean) {
        if(!longpress){
            elemVM.prendiElemento(elemento)
        }
        else{
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.edit_elemento)
            val newNome = dialog.findViewById<EditText>(R.id.editNome)
            val newQuantita = dialog.findViewById<EditText>(R.id.editQuantita)
            val btnAdd2 = dialog.findViewById<TextView>(R.id.btnAggiungiElemento)
            val elimina = dialog.findViewById<TextView>(R.id.elimina)
            newNome.setText(elemento.testo)
            newQuantita.setText(elemento.quantita.toString())
            btnAdd2.setOnClickListener {
                var nome = "Elemento senza nome"
                var quantita = "0"
                if (newNome.text.toString() != "") nome = newNome.text.toString()
                if (newQuantita.text.toString() != "") quantita = newQuantita.text.toString()
                elemVM.updateElemento(
                    Elemento(
                        elemento.id,
                        nome,
                        false,
                        quantita.toDouble()
                    )
                )
                dialog.dismiss()
            }
            elimina.setOnClickListener {
                elemVM.deleteElemento(elemento)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}