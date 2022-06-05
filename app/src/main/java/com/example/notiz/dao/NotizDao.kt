package com.example.notiz.dao

import androidx.room.*
import com.example.notiz.Ent.Notiz


@Dao
interface NoteDao {

    @Query("SELECT * FROM notiz ORDER BY id DESC")
    suspend fun getAllNotes() : List<Notiz>

    @Query("SELECT * FROM notiz WHERE id =:id")
    suspend fun getSpecificNote(id:Int) : Notiz

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notiz)

    @Delete
    suspend fun deleteNote(note:Notiz)

    @Query("DELETE FROM notiz WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

    @Update
    suspend fun updateNote(note:Notiz)
}