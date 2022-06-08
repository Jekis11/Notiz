package com.example.notiz.Ent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Notiz")
 class Notiz: Serializable{

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name ="Titel")
    var title: String? = null

    @ColumnInfo(name ="Untertitel")
    var subtitle: String? = null

    @ColumnInfo(name ="data_time")
    var datatime: String? = null

    @ColumnInfo(name ="notiz_text")
    var notizText: String? = null

    @ColumnInfo(name ="img_url")
    var imgurl: String? = null

    @ColumnInfo(name ="web_link")
    var weblink: String? = null


    @ColumnInfo(name ="color")
    var color: String? = null

    override fun toString(): String {


        return "$title : $datatime"
    }
}

