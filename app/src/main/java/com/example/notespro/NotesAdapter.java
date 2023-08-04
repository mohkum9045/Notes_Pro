package com.example.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

public class NotesAdapter extends FirestoreRecyclerAdapter<NoteContainer, NotesAdapter.NoteViewHolder> {


    Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<NoteContainer> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull NoteContainer notes) {
        holder.txttitle.setText(notes.Title);
        holder.content.setText(notes.Content);
        holder.timestamp.setText(utility.timestamptostring(notes.Timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, Notes.class);
            intent.putExtra("Title",notes.Title);
            intent.putExtra("Content",notes.Content);
            String NoteId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("NoteId",NoteId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
        return new NoteViewHolder(view);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView txttitle,content,timestamp;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txttitle = itemView.findViewById(R.id.txtTitle);
            content = itemView.findViewById(R.id.txtContent);
            timestamp = itemView.findViewById(R.id.txtTimestamp);
        }
    }

}
