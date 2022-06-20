package com.example.notiz.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notiz.Ent.Notiz
import com.example.notiz.R
import kotlinx.android.synthetic.main.item_rv_notiz.view.*
import kotlinx.android.synthetic.main.item_rv_notiz.view.imgNote

class NotizAdapter() :
    RecyclerView.Adapter<NotizAdapter.NotizViewHolder>() {


    var arrList = ArrayList<Notiz>()
    var listener: OnItemClickListener? = null

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

        if(arrList[position].weblink != null){
            holder.itemView.tvWebLink.text = arrList[position].weblink
            holder.itemView.tvWebLink.visibility = View.VISIBLE
        }
        else{
            holder.itemView.tvWebLink.visibility = View.GONE
        }

            holder.itemView.cardView.setOnClickListener{
                listener!!.onClicked(arrList[position])
            }

    }

    override fun getItemCount(): Int {

        return arrList.size

    }

    fun setData(arrNotizList: List<Notiz>){

        arrList = arrNotizList as ArrayList<Notiz>
    }

    fun setOnClickListener(Listener1:OnItemClickListener){

        listener = Listener1

    }

    class NotizViewHolder(view: View) : RecyclerView.ViewHolder(view){

    }


    interface OnItemClickListener{
        fun onClicked(notesModel:Notiz)
    }

}