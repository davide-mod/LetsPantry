package com.modolodavide.letspantry.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Elemento

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
            val adapter = ElementoAdapter(requireContext(), it, this)
            listaElementi.adapter = adapter
        })


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        elemVM = ViewModelProvider(this).get(ElementoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onElementoListener(elemento: Elemento, position: Int) {
        TODO("Not yet implemented")
    }

}