package com.hydr10n.notes.data.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hydr10n.notes.data.database.entities.NoteEntity;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(NoteEntity... noteEntities);

    @Update
    void update(NoteEntity... noteEntities);

    @Delete
    void delete(NoteEntity... noteEntities);

    @Query("select * from note_table")
    LiveData<List<NoteEntity>> getAll();
}