package com.example.notiz.dao

import androidx.room.*
import com.example.notiz.Ent.Notiz


@Dao
interface NotizDao {

    @get:Query("SELECT * FROM notiz ORDER BY id DESC")
    val allNotes: List<Notiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotiz(note: Notiz)

    @Delete
    fun  deleteNote(note: Notiz)
}