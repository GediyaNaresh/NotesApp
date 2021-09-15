package com.nareshgediya.firebasenotesapp.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;
import com.nareshgediya.firebasenotesapp.MainActivity;
import com.nareshgediya.firebasenotesapp.R;
import com.nareshgediya.firebasenotesapp.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    ArrayList<Note> notes;
    FirebaseFirestore fStore;
    FirebaseUser user;
    Context context;



    public NoteAdapter(ArrayList<Note> notes){
     this.notes =  notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //Note note = new Note();

        user = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        holder.noteTitle.setText(notes.get(position).getTitle());
        holder.noteContent.setText(notes.get(position).getContent());
        holder.timeStmap.setText(notes.get(position).getTimeStamp());
        final int code = getRandomColor();
        holder.mCardView.setCardBackgroundColor(holder.view.getResources().getColor(code,null));


        holder.iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                PopupMenu menu = new PopupMenu(v.getContext(),v);
                menu.setGravity(Gravity.END);
                menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent i = new Intent(v.getContext(), EditNote.class);
                        i.putExtra("title",notes.get(position).getTitle());
                        i.putExtra("content",notes.get(position).getContent());
                        i.putExtra("noteId",notes.get(position).getId());

                        v.getContext().startActivity(i);
                        return false;
                    }
                });


                menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        DocumentReference docRef = fStore.collection("notes").
                                document(user.getUid()).collection("myNotes").document(notes.get(position).getId());
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();

            getUpdateRecycler(notes, v.getContext());
                              //  v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Error in Deleting Note.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        return false;
                    }
                });

                menu.show();

            }
        });



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NoteDetail.class);
                i.putExtra("title",notes.get(position).getTitle());
                i.putExtra("content",notes.get(position).getContent());
                i.putExtra("code",code);
                i.putExtra("noteId",notes.get(position).getId());
                i.putExtra("timeStamp",notes.get(position).getTimeStamp());
                v.getContext().startActivity(i);
            }
        });

    }
    public int getUpdateRecycler(ArrayList<Note> notes, Context context){
        this.context = context;

        notes.clear();
        fStore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note noteList = document.toObject(Note.class);
                                noteList.setId(document.getId());
                                notes.add(noteList);
                            }
                            notifyDataSetChanged();
                        }

                        MainActivity.getInstance().recreateActivity();

                    }
                });
        return notes.size();
    }


    private int getRandomColor() {

        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle,noteContent, timeStmap;
        View view;
        CardView mCardView;
        ImageView iconMenu;
        TextView noteCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.titles);
            noteContent = itemView.findViewById(R.id.content);
            mCardView = itemView.findViewById(R.id.noteCard);
            iconMenu = itemView.findViewById(R.id.menuIcon);
            timeStmap = itemView.findViewById(R.id.timeStamp);
            view = itemView;
        }
    }


}
