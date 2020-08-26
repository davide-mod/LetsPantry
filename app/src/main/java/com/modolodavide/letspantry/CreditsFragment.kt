package com.modolodavide.letspantry

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.credits_fragment.*

class CreditsFragment : Fragment() {
    private lateinit var viewModel: CreditsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.credits_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreditsViewModel::class.java)
        creditsTesto.movementMethod = ScrollingMovementMethod()
        //tutti gli "aiuti" che ho ricevuto
        val str =   "LIBRERIA ESTERNA CALENDARIO MODIFICABILE:\nhttps://github.com/SpongeBobSun/mCalendarView\n" +
                    "ALGORITMO di STRING MATCHING K-APPROSSIMATO per RICERCA:\nhttps://stackoverflow.com/questions/955110/similarity-string-comparison-in-java\n"+
                    "TESTING:\nhttps://github.com/MiraxhTereziu\n"+
                    "PORZIONE DEK DATABASE RICETTE:\nhttps://www.dbricette.it/index.htm"
        creditsTesto.text=str
    }

}