package com.modolodavide.letspantry.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Elemento

class ElementoAdapter(private val context: Context, val listaElementi: List<Elemento>, val elementoListener: ElementoListener) :
    RecyclerView.Adapter<ElementoAdapter.ViewHolder>() {
    //recupero i vari elementi del singolo item Elemento
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testo: TextView = itemView.findViewById(R.id.listItem)
        val quantita: TextView = itemView.findViewById(R.id.listQuantita)
        val listCheck: TextView = itemView.findViewById(R.id.listCheck)
        val elementoView: ConstraintLayout = itemView.findViewById(R.id.elementoView)
    }

    override fun getItemCount(): Int = listaElementi.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_listaspesa, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elemento = listaElementi[position]
        with(holder) {
            //imposto i vari campi
            testo.text = elemento.testo
            quantita.text = elemento.quantita.toString()
            //in base alla situazione dell'elemento, l'aspetto sarà differente
            if(elemento.preso) {
                listCheck.text = "☑"
                elementoView.background = getDrawable(context, R.drawable.rettangolo_back_black)
                listCheck.setTextColor(getColor(context, R.color.colorText2))
                quantita.setTextColor(getColor(context, R.color.colorText2))
                testo.setTextColor(getColor(context, R.color.colorText2))
            }
            else {
                listCheck.text = "☐"
                elementoView.background = getDrawable(context, R.drawable.rettangolo_back_green)
                listCheck.setTextColor(getColor(context, R.color.colorText1))
                quantita.setTextColor(getColor(context, R.color.colorText1))
                testo.setTextColor(getColor(context, R.color.colorText1))
            }
            //parametro booleano longpress per distinguere e di conseguenza eseguire due azioni differenti nel fragment
            holder.itemView.setOnClickListener {
                elementoListener.onElementoListener(elemento, holder.layoutPosition, false)
            }
            holder.itemView.setOnLongClickListener {
                elementoListener.onElementoListener(elemento, holder.layoutPosition, true)
                true
            }

        }
    }

    //interfaccia del listener per poter interagire nel fragment
    interface ElementoListener{
        fun onElementoListener(elemento: Elemento, position: Int, longpress: Boolean)
    }

}