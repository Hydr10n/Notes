package com.hydr10n.notes.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.hydr10n.notes.data.database.daos.NoteDao;
import com.hydr10n.notes.data.database.entities.NoteEntity;

@Database(entities = {NoteEntity.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase {
    private static NoteRoomDatabase instance;

    public static final String DATABASE_NAME = "notes";

    public abstract NoteDao noteDao();

    static public NoteRoomDatabase getDatabase(Context context) {
        synchronized (NoteRoomDatabase.class) {
            if (instance == null)
                instance = Room.databaseBuilder(context.getApplicationContext(), NoteRoomDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
            return instance;
        }
    }
}