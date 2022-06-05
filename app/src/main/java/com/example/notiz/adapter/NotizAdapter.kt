package com.example.notiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiz.Ent.Notiz
import com.example.notiz.R
import kotlinx.android.synthetic.main.item_rv_notiz.view.*

class NotizAdapter(val arrList: List<Notiz>) :
    RecyclerView.Adapter<NotizAdapter.NotizViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotizViewHolder {
        return NotizViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notiz, parent,false)
                )
    }

    override fun onBindViewHolder(holder: NotizViewHolder, position: Int) {

        holder.itemView.tvtitel.text = arrList[position].title
        holder.itemView.tvdesc.text = arrList[position].notizText
        holder.itemView.tvDatumZeit.text = arrList[position].datatime

    }

    override fun getItemCount(): Int {

        return arrList.size

    }

    class NotizViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }

}