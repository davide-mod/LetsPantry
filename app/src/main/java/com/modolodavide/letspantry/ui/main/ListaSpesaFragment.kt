package com.modolodavide.letspantry.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.MainActivity
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Elemento
import com.modolodavide.letspantry.data.Ingrediente
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class ListaSpesaFragment : Fragment(), ElementoAdapter.ElementoListener {

    private lateinit var elemVM: ElementoViewModel
    private lateinit var dispensaVM: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.listaspesa_fragment, container, false)
        activity?.title = "Let's Pantry! - Lista della Spesa"


        //istanzio la viewmodel per poter comunicare facilmente col database della Lista della spesa
        elemVM = ViewModelProvider(this).get(ElementoViewModel::class.java)
        val listaElementi = view.findViewById<RecyclerView>(R.id.viewListaSpesa)
        //scorro la lista ricevuta dal database in modo da ordinare gli elementi presi e non e renderli di conseguenza differenti
        elemVM.elementoList.observe(viewLifecycleOwner, {
            val presi = mutableListOf<Elemento>()
            val nonpresi = mutableListOf<Elemento>()
            it.forEach { elemento ->
                if(elemento.preso)
                    presi.add(elemento)
                else
                    nonpresi.add(elemento)
            }
            //lista orfidata che tiene gli elementi in ordine alfabetico e poi li ordina come non presi e poi presi
            val adapter = ElementoAdapter(requireContext(), nonpresi+presi, this)
            listaElementi.adapter = adapter
        })

        //bottone per un nuovo elemento per la lista della spesa
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
        dispensaVM = ViewModelProvider(this).get(MainViewModel::class.java)
        btnMenuLaterale.setOnClickListener{
            (activity as MainActivity?)?.openDrawer()
        }
        (activity as MainActivity?)?.actionBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimaryBlue
            )
        )
        elemVM = ViewModelProvider(this).get(ElementoViewModel::class.java)
    }

    override fun onElementoListener(elemento: Elemento, position: Int, longpress: Boolean) {
        if(!longpress){ //controllo il tipo di pressione avvenuta sull'elemento singolo
            elemVM.prendiElemento(elemento) //se singolo "tap" cambio solamente lo stato
        }
        else{//se pressione prolungata permetto la modifica
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.edit_elemento)
            val newNome = dialog.findViewById<EditText>(R.id.editNome)
            val newQuantita = dialog.findViewById<EditText>(R.id.editQuantita)
            val btnAdd2 = dialog.findViewById<TextView>(R.id.btnAggiungiElemento)
            val elimina = dialog.findViewById<TextView>(R.id.elimina)
            val btnToDispensa = dialog.findViewById<TextView>(R.id.btnToDispensa)
            val c = Calendar.getInstance()
            val day = c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH) + 1
            val year = c.get(Calendar.YEAR)
            btnToDispensa.setOnClickListener {
                dispensaVM.insertIngrediente(Ingrediente(0, newNome.text.toString(), year, month, day, newQuantita.text.toString().toDouble()))
                elemVM.deleteElemento(elemento)
                dialog.dismiss()
            }
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
            //l'utente può anche eliminare totalmente un elemento
            elimina.setOnClickListener {
                elemVM.deleteElemento(elemento)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}