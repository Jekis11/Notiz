package com.example.notiz.Ent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notiz")
data class Notiz (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name ="Titel")
    var title: String,

    @ColumnInfo(name ="Untertitel")
    var subtitle: String,

    @ColumnInfo(name ="data_time")
    var datatime: String,

    @ColumnInfo(name ="notiz_text")
    var notizText: String,

    @ColumnInfo(name ="img_url")
    var img_url: String,

    @ColumnInfo(name ="web_link")
    var weblink: String,


    @ColumnInfo(name ="color")
    var color: String

) {
    override fun toString(): String {


        return "$title : $datatime"
    }
}

