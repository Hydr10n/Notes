package com.hydr10n.notes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hydr10n.notes.R;
import com.hydr10n.notes.data.database.entities.NoteEntity;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private Context context;
    private List<NoteEntity> noteEntities;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView textViewTitle, textViewNote;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_note);
            textViewTitle = view.findViewById(R.id.text_title);
            textViewNote = view.findViewById(R.id.text_note);
        }
    }

    public interface OnClickListener {
        void onClick(View v, NoteEntity noteEntity);
    }

    public interface OnLongClickListener {
        boolean onLongClick(View v, NoteEntity noteEntity);
    }

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NoteEntity noteEntity = noteEntities == null ? null : noteEntities.get(position);
        holder.cardView.setOnClickListener(onClickListener == null ? null : v -> onClickListener.onClick(v, noteEntity));
        holder.cardView.setOnLongClickListener(onLongClickListener == null ? null : v -> onLongClickListener.onLongClick(v, noteEntity));
        if (noteEntity != null) {
            final String note = noteEntity.getNote();
            if (note.isEmpty())
                holder.textViewNote.setVisibility(View.GONE);
            else {
                holder.textViewNote.setText(note);
                holder.textViewNote.setVisibility(View.VISIBLE);
            }
            final String title = noteEntity.getTitle();
            if (!title.isEmpty()) {
                holder.textViewTitle.setText(title);
                holder.textViewTitle.setVisibility(View.VISIBLE);
            } else
                holder.textViewTitle.setVisibility(note.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return noteEntities == null ? 0 : noteEntities.size();
    }

    public void setNotes(List<NoteEntity> noteEntities) {
        this.noteEntities = noteEntities;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}