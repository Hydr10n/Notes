package com.hydr10n.notes.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hydr10n.notes.R;
import com.hydr10n.notes.data.database.entities.NoteEntity;
import com.hydr10n.notes.data.viewmodels.NoteViewModel;

public class NoteFragment extends Fragment {
    private boolean deleteNote;
    private NoteViewModel noteViewModel;
    private NoteEntity noteEntity;
    private EditText editTextTitle, editTextNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        final Bundle arguments = getArguments();
        if (arguments != null)
            noteEntity = arguments.getParcelable(NoteEntity.class.getSimpleName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setNavigationOnClickListener(v -> appCompatActivity.onBackPressed());
        editTextTitle = view.findViewById(R.id.edit_title);
        editTextNote = view.findViewById(R.id.edit_note);
        if (noteEntity != null) {
            editTextTitle.setText(noteEntity.getTitle());
            editTextNote.setText(noteEntity.getNote());
        }
        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();
                if (string.contains("\n")) {
                    string = string.replace('\n', '\0');
                    editTextTitle.setText(string);
                    editTextNote.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!deleteNote) {
            final String title = editTextTitle.getText().toString(), note = editTextNote.getText().toString();
            if (noteEntity != null) {
                noteEntity.setTitle(title);
                noteEntity.setNote(note);
                noteViewModel.update(noteEntity);
            } else if (!title.isEmpty() || !note.isEmpty())
                noteViewModel.insert(new NoteEntity(title, note));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                if (editTextTitle.getText().length() != 0 || editTextNote.getText().length() != 0 || noteEntity != null)
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(getString(R.string.delete_note) + "?")
                            .setPositiveButton(R.string.yes, (dialog, which) -> {
                                if (noteEntity != null)
                                    noteViewModel.delete(noteEntity);
                                deleteNote = true;
                                getActivity().onBackPressed();
                            })
                            .setNegativeButton(R.string.no, null)
                            .create().show();
                else
                    getActivity().onBackPressed();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}