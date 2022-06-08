package com.example.notiz.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiz.Ent.Notiz
import com.example.notiz.R
import kotlinx.android.synthetic.main.fragment_create_notiz.view.*
import kotlinx.android.synthetic.main.item_rv_notiz.view.*
import kotlinx.android.synthetic.main.item_rv_notiz.view.imgNote

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

        if (arrList[position].color != null){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }
        else
        {
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#171C26"))
        }

        if(arrList[position].imgurl != null){
            holder.itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgurl))
            holder.itemView.imgNote.visibility = View.VISIBLE
        }
        else{
            holder.itemView.imgNote.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {

        return arrList.size

    }

    class NotizViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }

}