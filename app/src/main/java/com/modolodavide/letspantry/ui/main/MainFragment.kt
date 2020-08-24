package com.modolodavide.letspantry.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.github.sundeepk.compactcalendarview.domain.Event
import com.modolodavide.letspantry.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.time.YearMonth
import java.util.*

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



        val txtCalendario: TextView = view.findViewById(R.id.txtCalendario)
        val calendario: CompactCalendarView = view.findViewById(R.id.compactcalendar_view)
        calendario.setLocale(Calendar.getInstance().timeZone, Locale.ITALY)
        calendario.setUseThreeLetterAbbreviation(true)
        calendario.setFirstDayOfWeek(Calendar.MONDAY)
        calendario.isVisible = false

        val ev1 = Event(Color.GREEN, 1598392800000, "Some extra data that I want to store.")
        calendario.addEvent(ev1)
        val ev2 = Event(Color.GREEN, 1598392800000)
        calendario.addEvent(ev2)
        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        val events: List<Event> =
            calendario.getEvents(1598392800000) // can also take a Date object
        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: $events")
        // define a listener to receive callbacks when certain events happen.
        calendario.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                calendario.setCurrentSelectedDayBackgroundColor(Color.parseColor("#B2FF59"))
                val events: List<Event> = calendario.getEvents(dateClicked)
                Log.d(TAG, "Day was clicked: $dateClicked with events $events")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                calendario.setCurrentSelectedDayBackgroundColor(Color.parseColor("#00E676"))
                txtCalendario.text = firstDayOfNewMonth.toString()
                Log.d(TAG, "Month was scrolled to: $firstDayOfNewMonth")
            }
        })

        txtCalendario.setOnClickListener {
            if(calendario.isVisible && !calendario.isAnimating){
                calendario.hideCalendarWithAnimation()
                calendario.isVisible=false
            }
            else {
                calendario.showCalendarWithAnimation()
                calendario.isVisible = true
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}