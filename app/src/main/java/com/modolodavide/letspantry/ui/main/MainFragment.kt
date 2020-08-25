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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Ingrediente
import com.modolodavide.letspantry.data.IngredienteAdapter
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.listeners.OnMonthChangeListener
import sun.bob.mcalendarview.views.ExpCalendarView
import sun.bob.mcalendarview.vo.DateData
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


class MainFragment : Fragment(), IngredienteAdapter.IngredienteListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mainVM: MainViewModel
    private lateinit var listaIngredienti: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        mainVM = ViewModelProvider(this).get(MainViewModel::class.java)

        val calendario: ExpCalendarView = view.findViewById(R.id.calendar_exp)
        calendario.isVisible = false
        calendario.bringToFront()
        val txtCalendario: TextView = view.findViewById(R.id.txtCalendario)
        listaIngredienti = view.findViewById<RecyclerView>(R.id.viewIngredienti)
        val btnAdd = view.findViewById<TextView>(R.id.btnAggiungi)

        calendario.setOnMonthChangeListener(object : OnMonthChangeListener() {
            override fun onMonthChange(year: Int, month: Int) {
                val testo = "$month/$year"
                txtCalendario.text = testo
            }
        })
        mainVM.ingredienteList.observe(viewLifecycleOwner, {
            val adapterI = IngredienteAdapter(requireContext(), it, this)
            listaIngredienti.adapter = adapterI
            it.forEach { ingrediente ->
                calendario.markDate(
                    DateData(
                        ingrediente.scadenzaAnno,
                        ingrediente.scadenzaMese,
                        ingrediente.scadenzaGiorno
                    ).setMarkStyle(MarkStyle.DOT, getColor(context!!, R.color.colorPrimary))
                )
            }
        })


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

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)

        txtCalendario.setOnClickListener {
            if (calendario.isVisible) {
                calendario.isVisible = false
                txtCalendario.text = "calendario"
                val adapterI = IngredienteAdapter(requireContext(), mainVM.ingredienteList.value!!, this)
                listaIngredienti.adapter = adapterI
            } else {
                calendario.travelTo(
                    DateData(year, month, day).setMarkStyle(
                        MarkStyle.BACKGROUND, getColor(
                            context!!,
                            R.color.colorPrimary
                        )
                    )
                )
                calendario.isVisible = true
                txtCalendario.text = "$month/$year"
            }
        }

        btnAdd.setOnClickListener {
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
                    month,
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
        /*
        TODO
        FUNZIONALITA'
        - Creare db ingredienti 30mins [OK]
        - Leggo il db per caricare la lista di ingredienti in casa 30mins [OK]
        - ciclo la lista per segnare nel calendario le date di scadenza [OK]
        - data.clickListener appare finestra con gli ingredienti che scadono in quel giorno 30mins[OK]
            -> query con ricerca per giorno[OK]
        - finestra per aggiunta ingredienti 1h[OK]
        - menu laterale per cambiare Fragment 30mins
        - order by &ricerca (stile gmail) 1h
        - modifica ingrediente
        DESIGN:
        - pulsante per nascondere il calendario 5mins [OK]
        - nome del mese: prendo il mese attuale dal sistema, poi onMonthChangeListener 15mins [OK]

        PARTE 2(h):
        - db lista della spesa
        - aggiunta sequenziale
        - longpress: menu: elimina, manda a db ingredenti, rimetti in lista

        PARTE 3:
        - db ricette 30min
        - ricerca per ingredienti disponibili rimossi all'uso 4h

        TOT: 10h
         */

        //https://github.com/SpongeBobSun/mCalendarView

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun refreshLista(lista: MutableList<Ingrediente>) {
        val adapterI = IngredienteAdapter(requireContext(), lista, this)
        listaIngredienti.adapter = adapterI
    }

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
                else{
                    if (c != '-' && !boolday && boolmonth)
                        month += c
                    else {
                        if (c == '-' && !boolday && boolmonth)
                            boolmonth = false
                        else{
                            if(c != '-' && !boolday && !boolmonth)
                                year += c
                        }

                    }
                }
            }
        }
        return Triple(year.toInt(), month.toInt(), day.toInt())
    }

    override fun onIngredienteListener(ingrediente: Ingrediente, position: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_ingrediente)
        val newData = dialog.findViewById<TextView>(R.id.txtNewData)
        newData.text = "${ingrediente.scadenzaGiorno}-${ingrediente.scadenzaMese}-${ingrediente.scadenzaAnno}"
        newData.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    newData.text = "$dayOfMonth-${month + 1}-$year"
                },
                ingrediente.scadenzaAnno,
                ingrediente.scadenzaMese-1,
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
            mainVM.deleteIngrediente(ingrediente)
            dialog.dismiss()
        }
        dialog.show()
    }

}