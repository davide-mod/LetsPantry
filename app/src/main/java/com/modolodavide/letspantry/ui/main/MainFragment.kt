package com.modolodavide.letspantry.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.MainActivity
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Ingrediente
import kotlinx.android.synthetic.main.main_fragment.*
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.listeners.OnMonthChangeListener
import sun.bob.mcalendarview.views.ExpCalendarView
import sun.bob.mcalendarview.vo.DateData
import java.util.*


class MainFragment : Fragment(), IngredienteAdapter.IngredienteListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainVM: MainViewModel
    private lateinit var listaIngredienti: RecyclerView
    private lateinit var calendario: ExpCalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        mainVM = ViewModelProvider(this).get(MainViewModel::class.java)
        listaIngredienti = view.findViewById(R.id.viewIngredienti)
        val btnAdd = view.findViewById<TextView>(R.id.btnAggiungi)
        calendario = view.findViewById(R.id.calendar_exp)
        calendario.isVisible = false
        calendario.bringToFront()
        val txtCalendario: TextView = view.findViewById(R.id.txtCalendario)
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        //evidenzio il giorno attuale
        calendario.markDate(
            DateData(year, month, day).setMarkStyle(
                MarkStyle.BACKGROUND, getColor(
                    requireContext(),
                    R.color.colorPrimary
                )
            )
        )
        //quando cambia il mese visualizzato cambio valore della TextView per indicarlo
        calendario.setOnMonthChangeListener(object : OnMonthChangeListener() {
            override fun onMonthChange(year: Int, month: Int) {
                val testo = "$month/$year"
                txtCalendario.text = testo
            }
        })
        //tengo "sotto osservazione" il database degli ingredienti e nel caso ci fosse una qualche modifica aggiorno la lista
        mainVM.ingredienteList.observe(viewLifecycleOwner, {
            val adapterI = IngredienteAdapter(requireContext(), it, this)
            listaIngredienti.adapter = adapterI
            it.forEach { ingrediente ->
                //metto un pallino colorato sotto il giorno relativo alla scadenza
                calendario.markDate(
                    DateData(
                        ingrediente.scadenzaAnno,
                        ingrediente.scadenzaMese,
                        ingrediente.scadenzaGiorno
                    ).setMarkStyle(MarkStyle.DOT, getColor(requireContext(), R.color.colorReddo))
                )
            }
        })
        //listener sulla data che permette di filtrare gli ingredienti e vedere solo quelli che scadono in quell'esatto giorno
        calendario.setOnDateClickListener(object : OnDateClickListener() {
            override fun onDateClick(view: View, date: DateData) {
                val listaFiltrata = mutableListOf<Ingrediente>()
                mainVM.ingredienteList.value?.forEach {
                    if (it.scadenzaGiorno == date.day && it.scadenzaMese == date.month && it.scadenzaAnno == date.year) {
                        listaFiltrata.add(it)
                    }
                }
                refreshLista(listaFiltrata)
            }
        })

        //mostro/nascondo il calendario cliccando sulla TextView relativa
        txtCalendario.setOnClickListener {
            if (calendario.isVisible) {
                calendario.isVisible = false
                txtCalendario.text = "calendario"
                val adapterI = IngredienteAdapter(
                    requireContext(),
                    mainVM.ingredienteList.value!!,
                    this
                )
                listaIngredienti.adapter = adapterI
            } else {
                val c1 = Calendar.getInstance()
                val day1 = c1.get(Calendar.DAY_OF_MONTH)
                val month1 = c1.get(Calendar.MONTH) + 1
                val year1 = c1.get(Calendar.YEAR)
                calendario.travelTo( //imposto come mese di partenza quello attuale
                    DateData(year1, month1, day1).setMarkStyle(
                        MarkStyle.BACKGROUND, getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                )
                calendario.isVisible = true
                txtCalendario.text = "$month/$year"
            }
        }

        btnAdd.setOnClickListener {//aggiunta di un ingrediente
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.nuovo_ingrediente)
            val newData = dialog.findViewById<TextView>(R.id.txtNewData)
            newData.text = "$day-$month-$year"
            newData.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        newData.text = "$dayOfMonth-${month + 1}-$year"
                    },
                    year,
                    month-1,
                    day
                ).show()
            }
            val newNome = dialog.findViewById<EditText>(R.id.editNome)
            val newQuantita = dialog.findViewById<EditText>(R.id.editQuantita)
            val btnAdd2 = dialog.findViewById<TextView>(R.id.btnAggiungiElemento)
            btnAdd2.setOnClickListener {
                var nome = "Ingrediente senza nome"
                var quantita = "0"
                val (scadenzaAnno, scadenzaMese, scadenzaGiorno) = parseData(newData.text.toString())
                if (newNome.text.toString() != "") nome = newNome.text.toString()
                if (newQuantita.text.toString() != "") quantita = newQuantita.text.toString()
                mainVM.insertIngrediente(
                    Ingrediente(
                        0,
                        nome,
                        scadenzaAnno,
                        scadenzaMese,
                        scadenzaGiorno,
                        quantita.toDouble()
                    )
                )
                dialog.dismiss()
            }
            dialog.show()
        }

        val ricerca = view.findViewById<EditText>(R.id.ricerca)
        ricerca.addTextChangedListener {//ogni singola modifica alla EditText triggera questo evento
            var maxSF = 0.0
            val listaFiltrata = mutableListOf<Ingrediente>()
            mainVM.ingredienteList.value?.forEach {//scorro la lista la prima volta per trovare la massima similarità
                val sim = similarity(it.nome, ricerca.text.toString())
                if (sim > maxSF) maxSF = sim
            }
            //mostro gli ingredienti che hanno almeno metà similarità massima
            mainVM.ingredienteList.value?.forEach {
                val sim = similarity(it.nome, ricerca.text.toString())
                if (sim > maxSF / 2) {
                    listaFiltrata.add(it)
                }
            }
            //se si cancella completamente torno a frase completa
            if (ricerca.text.toString() != "") {
                val adapterI = IngredienteAdapter(requireContext(), listaFiltrata, this)
                listaIngredienti.adapter = adapterI
            } else {
                val adapterI =
                    IngredienteAdapter(requireContext(), mainVM.ingredienteList.value!!, this)
                listaIngredienti.adapter = adapterI
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnMenuLaterale.setOnClickListener{
            (activity as MainActivity?)?.openDrawer()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    //funzione per aggiornare la lista di ingredienti anche dal dialog
    private fun refreshLista(lista: MutableList<Ingrediente>) {
        val adapterI = IngredienteAdapter(requireContext(), lista, this)
        listaIngredienti.adapter = adapterI
    }

    //trasformo la data da stringa dd-mm-yyyy in tre valori Interi
    private fun parseData(data: String): Triple<Int, Int, Int> {
        var year = ""
        var month = ""
        var day = ""
        var boolday = true
        var boolmonth = true
        data.forEach { c ->
            if (c != '-' && boolday)
                day += c
            else {
                if (c == '-' && boolday)
                    boolday = false
                else {
                    if (c != '-' && !boolday && boolmonth)
                        month += c
                    else {
                        if (c == '-' && !boolday && boolmonth)
                            boolmonth = false
                        else {
                            if (c != '-' && !boolday && !boolmonth)
                                year += c
                        }

                    }
                }
            }
        }
        return Triple(year.toInt(), month.toInt(), day.toInt())
    }

    //modifica ingrediente
    override fun onIngredienteListener(ingrediente: Ingrediente, position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_ingrediente)
        calendario.unMarkDate(
            ingrediente.scadenzaAnno,
            ingrediente.scadenzaMese,
            ingrediente.scadenzaGiorno
        )
        val newData = dialog.findViewById<TextView>(R.id.txtNewData)
        newData.text =
            "${ingrediente.scadenzaGiorno}-${ingrediente.scadenzaMese}-${ingrediente.scadenzaAnno}"
        newData.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    newData.text = "$dayOfMonth-${month + 1}-$year"
                },
                ingrediente.scadenzaAnno,
                ingrediente.scadenzaMese - 1,
                ingrediente.scadenzaGiorno
            ).show()
        }
        val newNome = dialog.findViewById<EditText>(R.id.editNome)
        val newQuantita = dialog.findViewById<EditText>(R.id.editQuantita)
        val btnAdd2 = dialog.findViewById<TextView>(R.id.btnAggiungiElemento)
        val elimina = dialog.findViewById<TextView>(R.id.elimina)
        newNome.setText(ingrediente.nome)
        newQuantita.setText(ingrediente.quantita.toString())
        btnAdd2.setOnClickListener {
            var nome = "Ingrediente senza nome"
            var quantita = "0"
            val (scadenzaAnno, scadenzaMese, scadenzaGiorno) = parseData(newData.text.toString())
            if (newNome.text.toString() != "") nome = newNome.text.toString()
            if (newQuantita.text.toString() != "") quantita = newQuantita.text.toString()
            mainVM.updateIngrediente(
                Ingrediente(
                    ingrediente.id,
                    nome,
                    scadenzaAnno,
                    scadenzaMese,
                    scadenzaGiorno,
                    quantita.toDouble()
                )
            )
            dialog.dismiss()
        }
        elimina.setOnClickListener {
            calendario.unMarkDate(
                ingrediente.scadenzaAnno,
                ingrediente.scadenzaMese,
                ingrediente.scadenzaGiorno
            )
            mainVM.deleteIngrediente(ingrediente)
            dialog.dismiss()
        }
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