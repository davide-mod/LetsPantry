package com.modolodavide.letspantry.ui.main

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.modolodavide.letspantry.R
import com.modolodavide.letspantry.data.Elemento
import com.modolodavide.letspantry.data.Ingrediente
import java.util.*

class ElementoAdapter(private val context: Context, val listaElementi: List<Elemento>, val elementoListener: ElementoListener) :
    RecyclerView.Adapter<ElementoAdapter.ViewHolder>() {

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
            testo.text = elemento.testo
            quantita.text = elemento.quantita.toString()
            if(elemento.preso) {
                listCheck.text = "X"
                elementoView.background = getDrawable(context, R.drawable.rettangolo_back_black)
                listCheck.setTextColor(getColor(context, R.color.colorText2))
                quantita.setTextColor(getColor(context, R.color.colorText2))
                testo.setTextColor(getColor(context, R.color.colorText2))
            }
            else {
                listCheck.text = "O"
                elementoView.background = getDrawable(context, R.drawable.rettangolo_back_green)
                listCheck.setTextColor(getColor(context, R.color.colorText1))
                quantita.setTextColor(getColor(context, R.color.colorText1))
                testo.setTextColor(getColor(context, R.color.colorText1))
            }
            holder.itemView.setOnClickListener {
                elementoListener.onElementoListener(elemento, holder.layoutPosition, false)
            }
            holder.itemView.setOnLongClickListener {
                elementoListener.onElementoListener(elemento, holder.layoutPosition, true)
                true
            }

        }
    }

    interface ElementoListener{
        fun onElementoListener(elemento: Elemento, position: Int, longpress: Boolean)
    }

}