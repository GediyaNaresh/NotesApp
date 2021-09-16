package com.nareshgediya.firebasenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import android.app.Activity;
import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nareshgediya.firebasenotesapp.SplashLogin.LoginActivity;
import com.nareshgediya.firebasenotesapp.model.Note;
import com.nareshgediya.firebasenotesapp.note.AddNote;
import com.nareshgediya.firebasenotesapp.note.NoteAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import soup.neumorphism.NeumorphFloatingActionButton;


public class MainActivity extends AppCompatActivity implements SingalChoiceAlertDialog.SingleChoiceListner {
    private static final String FILE_NAME = "myFile";

    private AdView adView1;
    private InterstitialAd interstitialAd;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;


    NeumorphFloatingActionButton fab;
    TextView noteCount;
    RecyclerView noteLists;
    FirebaseFirestore fStore;
    // FirestoreRecyclerAdapter<Note,NoteViewHolder> noteAdapter;
    FirebaseUser user;
    FirebaseAuth fAuth;
    ArrayList<Note> note;
    NoteAdapter noteAdapter;
    String noteSize;

    private final String TAG = MainActivity.class.getSimpleName();


    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Note notes= new Note();
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.addNoteFloat);
        noteCount = findViewById(R.id.noteCount);
        AudienceNetworkAds.initialize(MainActivity.this);


        instance = this;
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userDisplayName);
        TextView navEmail = (TextView) headerView.findViewById(R.id.userDisplayEmail);
        navUsername.setText(user.getDisplayName());
        navEmail.setText(user.getEmail());


        setSupportActionBar(toolbar);
        noteLists = findViewById(R.id.notelist);

        note = new ArrayList<>();
        noteAdapter = new NoteAdapter(note);
        noteLists.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();

        interstitialAd = new InterstitialAd(this, "IMG_16_9_APP_INSTALL#339962074271690_339963117604919");
        adView1 = new AdView(this, "IMG_16_9_APP_INSTALL#339962074271690_339963117604919", AdSize.BANNER_HEIGHT_50);

        //     LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        //      adContainer.addView(adView1);

//        showBannerAd();
//        interstitialAdShow();

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        noteLists.setLayoutManager(linearLayoutManager);

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_addNote:
                    startActivity(new Intent(MainActivity.this, AddNote.class));
                    overridePendingTransition(R.anim.bottomin, R.anim.bottomout);
                    break;
                case R.id.nav_logout:
                    fAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    break;

                case R.id.nav_delet_all:
                    deleteAllNotes();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Coming soon..", Toast.LENGTH_LONG).show();
            }
            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNote.class));
                overridePendingTransition(R.anim.bottomin, R.anim.bottomout);

            }
        });

//        Note notes = new Note();
//        notes.setTitle("123");
//        notes.setContent("Content");
//
//        for (int i = 0;i<20;i++){
//            note.add(notes);
//        }
        if (fAuth.getCurrentUser() != null) {
//            SharedPreferences preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
//          String choicePrefrence =   preferences.getString("1","Data Note Found");
//          String old =   preferences.getString("2","Data Note Found");
//       //     Toast.makeText(instance, "Choice "+choicePrefrence, Toast.LENGTH_SHORT).show();
//            if (old.equals(preferences.getString("2","Data Note Found"))) {
//                recyclerUpdate(fStore, user,note,noteAdapter, MainActivity.this, noteCount);
//            }else if (choicePrefrence.equals( preferences.getString("1","Data Note Found"))){
//                recyclerUpdateByDESCENDING(fStore, user,note,noteAdapter, MainActivity.this, noteCount);
//            }
            recyclerUpdateByDESCENDING(fStore, user, note, noteAdapter, MainActivity.this, noteCount);
        }


    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void finishActivity() {
        finish();
    }

    public void recreateActivity() {
        recreate();
    }

    private void interstitialAdShow() {
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.d(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.d(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Toast.makeText(MainActivity.this, "Interstitial : " + adError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        MenuItem item = menu.findItem(R.id.search1);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search by Title");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchData(s);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchData(String s) {

        fStore.collection("notes").document(user.getUid()).collection("myNotes")
                .orderBy("search").startAt(s).endAt(s + "\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            note.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note notes = document.toObject(Note.class);
                                notes.setId(document.getId());
                                note.add(notes);
                            }
                            noteAdapter.notifyDataSetChanged();

                            noteCount.setText(note.size() + " Notes");

                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAll) {
            deleteAllNotes();
        }
        if (item.getItemId() == R.id.oldest) {
            DialogFragment singleChoice = new SingalChoiceAlertDialog();
            singleChoice.setCancelable(false);
            singleChoice.show(getSupportFragmentManager(), "Select");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnpositiveBtnClick(String[] list, int position) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        //    Toast.makeText(instance,list[position], Toast.LENGTH_SHORT).show();
        String choice = list[position];

        if (choice.equals("Sort by Latest")) {

            recyclerUpdateByDESCENDING(fStore, user, note, noteAdapter, MainActivity.this, noteCount);
            editor.putString("1", choice);
            editor.apply();

        } else {
            recyclerUpdate(fStore, user, note, noteAdapter, MainActivity.this, noteCount);
            editor.putString("2", choice);
            editor.apply();
        }


    }


    public int recyclerUpdate(FirebaseFirestore fStore, FirebaseUser user, ArrayList<Note> Anote, NoteAdapter noteAdapter, Context context, TextView noteCount) {
        fStore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("time", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Anote.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note notes = document.toObject(Note.class);
                                notes.setId(document.getId());
                                Anote.add(notes);
                            }
                            noteAdapter.notifyDataSetChanged();

                            noteCount.setText(Anote.size() + " Notes");

                        }
                    }
                });
        return Anote.size();
    }

    public int recyclerUpdateByDESCENDING(FirebaseFirestore fStore, FirebaseUser user, ArrayList<Note> Anote, NoteAdapter noteAdapter, Context context, TextView noteCount) {
        fStore.collection("notes").document(user.getUid()).collection("myNotes").orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Anote.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note notes = document.toObject(Note.class);
                                notes.setId(document.getId());
                                Anote.add(notes);
                            }
                            noteAdapter.notifyDataSetChanged();

                            noteCount.setText(Anote.size() + " Notes");

                        }
                    }
                });
        return Anote.size();
    }

    private void deleteAllNotes() {
        fStore.collection("notes").document(user.getUid()).collection("myNotes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fStore.collection("notes").document(user.getUid()).collection("myNotes").document(document.getId()).delete();
                            }
                            note.clear();
                            noteAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Deleted All notes", Toast.LENGTH_SHORT).show();
                            recreate();
                        }

                    }
                });
    }

    private void showBannerAd() {
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
// Ad error callback

                Toast.makeText(
                        MainActivity.this,
                        "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG)
                        .show();
                Log.d(TAG, adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {


            }

            @Override
            public void onAdClicked(Ad ad) {
// Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
// Ad impression logged callback
            }
        };
// Request an ad

        adView1.loadAd(adView1.buildLoadAdConfig().withAdListener(adListener).build());
    }

}