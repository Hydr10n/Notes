package com.hydr10n.notes.data.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hydr10n.notes.data.database.entities.NoteEntity;
import com.hydr10n.notes.data.repositories.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<NoteEntity>> noteEntities;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        noteEntities = noteRepository.getAllNotes();
    }

    public void insert(NoteEntity... noteEntities) {
        noteRepository.insert(noteEntities);
    }

    public void update(NoteEntity... noteEntities) {
        noteRepository.update(noteEntities);
    }

    public void delete(NoteEntity... noteEntities) {
        noteRepository.delete(noteEntities);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return noteEntities;
    }
}