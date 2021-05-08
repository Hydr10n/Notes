package com.hydr10n.notes.data.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hydr10n.notes.data.database.NoteRoomDatabase;
import com.hydr10n.notes.data.database.daos.NoteDao;
import com.hydr10n.notes.data.database.entities.NoteEntity;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> noteEntities;

    private static class insertAsyncTask extends android.os.AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        insertAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.insert(noteEntities);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        updateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.update(noteEntities);
            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        deleteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.delete(noteEntities);
            return null;
        }
    }

    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        noteDao = db.noteDao();
        noteEntities = noteDao.getAll();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return noteEntities;
    }

    public void insert(NoteEntity... noteEntities) {
        new insertAsyncTask(noteDao).execute(noteEntities);
    }

    public void update(NoteEntity... noteEntities) {
        new updateAsyncTask(noteDao).execute(noteEntities);
    }

    public void delete(NoteEntity... noteEntities) {
        new deleteAsyncTask(noteDao).execute(noteEntities);
    }
}