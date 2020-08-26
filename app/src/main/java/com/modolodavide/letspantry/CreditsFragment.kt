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

    companion object {
        fun newInstance() = CreditsFragment()
    }

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
        val str =   "CALENDARIO MODIFICABILE:\nhttps://github.com/SpongeBobSun/mCalendarView\n" +
                    "STRING MATCHING K-APPROSSIMATO per RICERCA:\nhttps://stackoverflow.com/questions/955110/similarity-string-comparison-in-java\n"+
                    "TESTING:\nhttps://github.com/MiraxhTereziu"
        creditsTesto.text=str
    }

}