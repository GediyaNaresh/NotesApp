package com.nareshgediya.firebasenotesapp.note;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nareshgediya.firebasenotesapp.MainActivity;
import com.nareshgediya.firebasenotesapp.R;
import com.nareshgediya.firebasenotesapp.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import soup.neumorphism.NeumorphButton;

public class AddNote extends AppCompatActivity {
    FirebaseFirestore fStore;
    EditText noteTitle, noteContent;
    ProgressBar progressBarSave;
    FirebaseUser user;
    NoteAdapter noteAdapter;
    ArrayList<Note> note;
    Context context;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fStore = FirebaseFirestore.getInstance();
        noteContent = findViewById(R.id.addNoteContent);
        noteTitle = findViewById(R.id.addNoteTitle);
        progressBarSave = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        note = new ArrayList<>();
        noteAdapter = new NoteAdapter(note);

        noteTitle.requestFocus();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date time = new Date();
        String timeT = sdf.format(time).toString();

        Date currentTime = Calendar.getInstance().getTime();
        String formatedTime = DateFormat.getDateInstance().format(currentTime);
        String[] spliteDate = formatedTime.split(",");

        String month = spliteDate[0].trim();

        NeumorphButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = noteTitle.getText().toString();
                String nContent = noteContent.getText().toString();

                if (nTitle.isEmpty() || nContent.isEmpty()) {
                    Toast.makeText(AddNote.this, "Can not Save note with Empty Field.", Toast.LENGTH_SHORT).show();
                    return;
                }


                Date date = new Date();
                long l = date.getTime();

                progressBarSave.setVisibility(View.VISIBLE);

                Map<String, Object> noteMap = new HashMap<>();
                noteMap.put("title", nTitle);
                noteMap.put("content", nContent);
                noteMap.put("search", nTitle.toLowerCase());
                noteMap.put("timeStamp", timeT + " " + month);
                noteMap.put("time", String.valueOf(l));


                fStore.collection("notes").document(user.getUid()).collection("myNotes")
                        .add(noteMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                fStore.collection("notes").document(user.getUid()).collection("myNotes")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Note notes = document.toObject(Note.class);
                                                        notes.setId(document.getId());

                                                        note.add(notes);
                                                    }
                                                    noteAdapter.notifyDataSetChanged();

                                                }
                                            }
                                        });
                                Toast.makeText(AddNote.this, "Note Added.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNote.this, MainActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNote.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.close) {
            Toast.makeText(this, "Not Saved.", Toast.LENGTH_SHORT).show();
            onBackPressed();
            overridePendingTransition(R.anim.bottomin, R.anim.bottomout);
        }
        return super.onOptionsItemSelected(item);
    }
    // save note

    //      DocumentReference docref = fStore.collection("notes").document(user.getUid()).collection("myNotes").document();
//                DocumentReference docref = fStore.collection("notes").document();
//                Map<String,Object> note = new HashMap<>();
//                note.put("title",nTitle);
//                note.put("content",nContent);
//                docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(AddNote.this, "Note Added.", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AddNote.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
//                        progressBarSave.setVisibility(View.VISIBLE);
//                    }
//                });
}