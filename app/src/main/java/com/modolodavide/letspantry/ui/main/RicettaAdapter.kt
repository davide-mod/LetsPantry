package com.modolodavide.letspantry.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.Ricetta

class RicettaAdapter(private val context: Context, val listaRicette: List<Ricetta>, val ricettaListener: RicettaListener) :
    RecyclerView.Adapter<RicettaAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.nomeRicetta)
        val tipo: TextView = itemView.findViewById(R.id.tipoPiatto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_ricetta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ricetta = listaRicette[position]
        with(holder) {
            nome.text = ricetta.Nome
            tipo.text = ricetta.Tipo_Piatto

            holder.itemView.setOnClickListener {
                ricettaListener.onRicettaListener(ricetta, holder.layoutPosition)
            }
        }
    }

    override fun getItemCount(): Int = listaRicette.size

    interface RicettaListener{
        fun onRicettaListener(ricetta: Ricetta, position: Int)
    }
}