package com.modolodavide.letspantry.ui.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.FileHelper
import com.modolodavide.letspantry.MainActivity
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.Ricetta
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*


class RicetteFragment : Fragment(), RicettaAdapter.RicettaListener {
    val listTypeRicetta = Types.newParameterizedType(
        List::class.java, Ricetta::class.java
    )
    private lateinit var viewModel: RicetteViewModel
    private lateinit var listaRicette: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Let's Pantry! - Ricette"

        val view = inflater.inflate(R.layout.ricette_fragment, container, false)

        //leggo il json delle ricette
        val text = FileHelper.getData(requireContext(), "ricette.json")
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter: JsonAdapter<List<Ricetta>> = moshi.adapter(listTypeRicetta)
        var ricette: List<Ricetta>? = adapter.fromJson(text)
        //ordino la lista
        ricette = ricette?.sortedBy { it.Tipo_Piatto }
        var all = false
        val adapterR = RicettaAdapter(ricette!!, this)
        listaRicette = view.findViewById(R.id.listaRicette)
        listaRicette.adapter = adapterR
        //recupero gli ingredienti che ho in dispensa
        val mainVM = ViewModelProvider(this).get(MainViewModel::class.java)
        val btnFiltra = view.findViewById<TextView>(R.id.btnFiltra)
        btnFiltra.setOnClickListener {
            all = !all//attivo o disattivo il filtro
            if (all) {
                val ricetteSorted = mutableListOf<Ricetta>()
                ricette.forEach { ricetta ->
                    mainVM.ingredienteList.value?.forEachIndexed { _, it ->
                        //filtro se l'ingrediente principale è apiù del 50% simile ad un ingrediente in dispensa lo teniamo
                        if (similarity(
                                ricetta.Ing_Principale.toUpperCase(Locale.ROOT),
                                it.nome.toUpperCase(
                                    Locale.ROOT
                                )
                            ) > 0.5
                        ) {
                            ricetteSorted.add(ricetta)
                            return@forEachIndexed
                        }
                    }
                }
                val adapterR2 = RicettaAdapter(ricetteSorted, this)
                listaRicette.adapter = adapterR2
            } else {//se disattivo il filtro rimetto la lista totale
                val adapterR3 = RicettaAdapter(ricette, this)
                listaRicette.adapter = adapterR3
            }

        }

        //spinner per mostrare solo un determinato tipo di ricette
        val spinner = view.findViewById<Spinner>(R.id.spinnerRicette)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                if (spinner.selectedItem.toString().toUpperCase(Locale.ROOT) == "TUTTI")
                    updateRicette(ricette)
                else {
                    val ricetteSorted = mutableListOf<Ricetta>()
                    ricette.forEach { ricetta ->
                        if (ricetta.Tipo_Piatto.toUpperCase(Locale.ROOT) == spinner.selectedItem.toString()
                                .toUpperCase(Locale.ROOT)
                        )
                            ricetteSorted.add(ricetta)
                    }
                    updateRicette(ricetteSorted)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                return
            }
        }

        return view
    }

    private fun updateRicette(ricetteSorted: List<Ricetta>) {
        val adapterR = RicettaAdapter(ricetteSorted, this)
        listaRicette.adapter = adapterR
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnMenuLaterale.setOnClickListener{
            (activity as MainActivity?)?.openDrawer()
        }
        (activity as MainActivity?)?.actionBarColor(getColor(
            requireContext(),
            R.color.colorPrimaryOrange
        ))
        viewModel = ViewModelProvider(this).get(RicetteViewModel::class.java)
    }

    //se si clicca su una ricetta appare il dialog con i dettagli
    override fun onRicettaListener(ricetta: Ricetta, position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dettagli_ricetta)
        val nomeRicetta = dialog.findViewById<TextView>(R.id.nomeRicetta)
        val ingredientePrincipale = dialog.findViewById<TextView>(R.id.nomeIngredientePrincipale)
        val numeroPersone = dialog.findViewById<TextView>(R.id.numeroPersone)
        val listaIngredienti = dialog.findViewById<TextView>(R.id.listaIngredienti)
        val procedimento = dialog.findViewById<TextView>(R.id.procedimento)
        listaIngredienti.movementMethod = ScrollingMovementMethod()
        procedimento.movementMethod = ScrollingMovementMethod()
        nomeRicetta.text = ricetta.Nome
        ingredientePrincipale.text = "Principale: " + ricetta.Ing_Principale
        numeroPersone.text = "per " + ricetta.Persone + " persone"
        listaIngredienti.text = "Ingredienti:\n" + ricetta.Ingredienti
        procedimento.text = "Procedimento:\n" + ricetta.Preparazione
        dialog.show()
    }

    //funzioni per la similarità da https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
    private fun similarity(s1: String, s2: String): Double {
        var longer = s1
        var shorter = s2
        if (s1.length < s2.length) { // longer should always have greater length
            longer = s2
            shorter = s1
        }
        val longerLength = longer.length
        return if (longerLength == 0) {
            1.0 /* both strings are zero length */
        } else (longerLength - editDistance(longer, shorter)) / longerLength.toDouble()
    }

    private fun editDistance(s1: String, s2: String): Int {
        var s1 = s1
        var s2 = s2
        s1 = s1.toLowerCase(Locale.ROOT)
        s2 = s2.toLowerCase(Locale.ROOT)
        val costs = IntArray(s2.length + 1)
        for (i in 0..s1.length) {
            var lastValue = i
            for (j in 0..s2.length) {
                if (i == 0) costs[j] = j else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s1[i - 1] != s2[j - 1]) newValue = Math.min(
                            Math.min(newValue, lastValue),
                            costs[j]
                        ) + 1
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) costs[s2.length] = lastValue
        }
        return costs[s2.length]
    }

}