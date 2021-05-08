package com.hydr10n.notes.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.MaterialElevationScale;
import com.hydr10n.notes.BuildConfig;
import com.hydr10n.notes.R;
import com.hydr10n.notes.data.database.entities.NoteEntity;
import com.hydr10n.notes.data.viewmodels.NoteViewModel;
import com.hydr10n.notes.ui.adapters.NoteAdapter;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteViewModel noteViewModel;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, noteEntities -> noteAdapter.setNotes(noteEntities));
        noteAdapter = new NoteAdapter(getContext());
        noteAdapter.setOnClickListener((v, noteEntity) -> {
            final Bundle bundle = new Bundle();
            bundle.putParcelable(NoteEntity.class.getSimpleName(), noteEntity);
            NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_NoteFragment, bundle);
        });
        setExitTransition(new MaterialElevationScale(false));
        setReenterTransition(new MaterialElevationScale(true));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(view.findViewById(R.id.toolbar));
        recyclerView = view.findViewById(R.id.notes);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        view.findViewById(R.id.add_note).setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_HomeFragment_to_NoteFragment));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about: {
                final Context context = getContext();
                final SpannableString spannableString = new SpannableString(getString(R.string.version) + ": " + BuildConfig.VERSION_NAME + '\n' + getString(R.string.copyright));
                Linkify.addLinks(spannableString, Linkify.WEB_URLS);
                final TextView textView = new TextView(context);
                textView.setText(spannableString);
                textView.setPadding(0, 10, 0, 0);
                textView.setGravity(Gravity.CENTER);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                new MaterialAlertDialogBuilder(context)
                        .setTitle(getString(R.string.about))
                        .setView(textView)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, null)
                        .create().show();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}