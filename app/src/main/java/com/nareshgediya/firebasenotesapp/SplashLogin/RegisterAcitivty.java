package com.nareshgediya.firebasenotesapp.SplashLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.nareshgediya.firebasenotesapp.MainActivity;
import com.nareshgediya.firebasenotesapp.R;

import soup.neumorphism.NeumorphButton;

public class RegisterAcitivty extends AppCompatActivity {
    TextView already;
    EditText rUserName, rUserEmail, rUserPass, rUserConfPass;

    NeumorphButton singUp;
    TextView loginAct;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivty);

        rUserName = findViewById(R.id.user_name);
        rUserEmail = findViewById(R.id.email);
        rUserPass = findViewById(R.id.pass);
        rUserConfPass = findViewById(R.id.confirm_pass);
        progressBar = findViewById(R.id.progressBar);

        singUp = findViewById(R.id.signup);

        fAuth = FirebaseAuth.getInstance();


        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uUsername = rUserName.getText().toString();
                String uUserEmail = rUserEmail.getText().toString();
                String uUserPass = rUserPass.getText().toString();
                String uConfPass = rUserConfPass.getText().toString();

                if (uUserEmail.isEmpty() || uUsername.isEmpty() || uUserPass.isEmpty() || uConfPass.isEmpty()) {
                    Toast.makeText(RegisterAcitivty.this, "All Fields Are Required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!uUserPass.equals(uConfPass)) {
                    rUserConfPass.setError("Password Missmatch");
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(uUserEmail, uUserPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterAcitivty.this, "Your Account has Created...", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = fAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uUsername).build();

                        user.updateProfile(request);

                        startActivity(new Intent(RegisterAcitivty.this, LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterAcitivty.this, "Failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("abcd", e.getLocalizedMessage().toString());
                    }
                });


            }
        });

        Util.blackIconStatusBar(RegisterAcitivty.this, R.color.dark_background);

        already = findViewById(R.id.already);

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterAcitivty.this, LoginActivity.class));
                finish();
            }
        });

    }
}