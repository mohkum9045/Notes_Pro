package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Notes extends AppCompatActivity {

    ImageButton SaveNote;
    EditText Title, Content;
    TextView PageTitile;
    String newTitle, newContent, NoteId;
    Boolean isEdit = false;
    Button Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        SaveNote = findViewById(R.id.btnSaveNote);
        Title = findViewById(R.id.edtTitle);
        Content = findViewById(R.id.edtContent);
        PageTitile = findViewById(R.id.txtPageTitile);
        Delete = findViewById(R.id.btnDelete);
        Delete.setVisibility(View.GONE);


        //received data
        newTitle = getIntent().getStringExtra("Title");
        newContent = getIntent().getStringExtra("Content");
        NoteId = getIntent().getStringExtra("NoteId");

        Title.setText(newTitle);
        Content.setText(newContent);

        if(NoteId!=null && !NoteId.isEmpty()){
            isEdit=true;
        }

        if(isEdit){
            PageTitile.setText("Please edit your Note");
            Delete.setVisibility(View.VISIBLE);
        }

        SaveNote.setOnClickListener(v-> funsaveNote());
        Delete.setOnClickListener(v-> funDeleteNoteFromFirebase());

    }

    void funsaveNote(){
        String title = Title.getText().toString();
        String content = Content.getText().toString();
        if (title==null || title.isEmpty()){
            Title.setError("Please add Title");
            return;
        }
        NoteContainer noteContainer = new NoteContainer();
        noteContainer.setTitle(title);
        noteContainer.setContent(content);
        noteContainer.setTimestamp(Timestamp.now());

        savenotetofirebase(noteContainer);
        finish();
    }

    void savenotetofirebase(NoteContainer noteContainer){
        DocumentReference documentReference;
        if(isEdit){
            documentReference = utility.collectionreferencefornotes().document(NoteId);
        }else {
            documentReference = utility.collectionreferencefornotes().document();
        }
        documentReference.set(noteContainer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    utility.showtoast(Notes.this,"Notes saved successfully");
                }else{
                    utility.showtoast(Notes.this,"Notes not saved");
                }
            }
        });
    }

    void funDeleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = utility.collectionreferencefornotes().document(NoteId);
        finish();
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    utility.showtoast(Notes.this,"Notes Deleted successfully");
                }else{
                    utility.showtoast(Notes.this,"Notes not Deleted");
                }
            }
        });
    }

}