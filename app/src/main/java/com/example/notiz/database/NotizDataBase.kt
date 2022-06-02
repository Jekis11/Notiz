package com.example.notiz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notiz.Ent.Notiz
import com.example.notiz.dao.NotizDao

@Database(entities = [Notiz::class], version = 1, exportSchema = false)
abstract class NotizDataBase: RoomDatabase() {


    companion object{
        var notizDataBase:NotizDataBase? = null

        @Synchronized
        fun getDatabase(context: Context): NotizDataBase{
            if(notizDataBase != null){
                notizDataBase = Room.databaseBuilder(
                    context,
                    NotizDataBase::class.java,
                    "notes.db"
                ).build()
            }
            return notizDataBase!!
        }
    }

    abstract fun notizDao():NotizDao
}