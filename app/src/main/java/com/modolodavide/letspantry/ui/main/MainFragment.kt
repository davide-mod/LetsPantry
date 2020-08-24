package com.modolodavide.letspantry.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.modolodavide.letspantry.R
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnMonthChangeListener
import sun.bob.mcalendarview.views.ExpCalendarView
import sun.bob.mcalendarview.vo.DateData


val TAG = "debuggalo"


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        /*
        TODO
        FUNZIONALITA'
        - Creare db ingredienti 30mins
        - Leggo il db per caricare la lista di ingredienti in casa 30mins
        - ciclo la lista per segnare nel calendario le date di scadenza
        - data.clickListener appare finestra con gli ingredienti che scadono in quel giorno 30mins
            -> query con ricerca per giorno
        - finestra per aggiunta ingredienti 1h
        - menu laterale per cambiare Fragment 30mins
        - order by ricerca (stile gmail)
        DESIGN:
        - pulsante per nascondere il calendario 5mins
        - nome del mese: prendo il mese attuale dal sistema, poi onMonthChangeListener 15mins

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
        val calendario: ExpCalendarView = view.findViewById(R.id.calendar_exp)

        calendario.markDate(DateData(2020, 8, 27).setMarkStyle(MarkStyle.BACKGROUND, getColor(context!!, R.color.colorPrimary)))

        calendario.setOnMonthChangeListener(object : OnMonthChangeListener() {
            override fun onMonthChange(year: Int, month: Int) {
                //cambio campo Mese con $month
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}