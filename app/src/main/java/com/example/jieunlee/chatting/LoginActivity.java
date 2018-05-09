package com.example.jieunlee.chatting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Button btnLogin, btnRegister;
    private EditText etEmail, etPwd;
    private ProgressBar pbLogin;
    String TAG = "LoginActivity";
    String stEmail, stPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LayoutInit();

    }

    public void LayoutInit() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPwd = (EditText) findViewById(R.id.etPwd);
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", user.getUid());
                    editor.putString("email", user.getEmail());
                    editor.apply();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stEmail = etEmail.getText().toString();
                stPwd = etPwd.getText().toString();

                if (stEmail.isEmpty() || stEmail.equals("") || stPwd.isEmpty() || stPwd.equals("")) {
                    Toast.makeText(LoginActivity.this, "이메일과 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(stEmail, stPwd);
                }

            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stEmail = etEmail.getText().toString();
                stPwd = etPwd.getText().toString();

                if (stEmail.isEmpty() || stEmail.equals("") || stPwd.isEmpty() || stPwd.equals("")) {
                    Toast.makeText(LoginActivity.this, "이메일과 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    userLogin(stEmail, stPwd);


                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void registerUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:OnComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            if (user != null) {
                                Hashtable<String, String> profile = new Hashtable<String, String>();
                                profile.put("email", user.getEmail());
                                profile.put("key", user.getUid());      //key를 가지고 child를 구분 참조편함
                                profile.put("photo", "");
                                myRef.child(user.getUid()).setValue(profile);
                            }
                        }
                    }
                });
    }

    private void userLogin(String email, String password) {
        pbLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        pbLogin.setVisibility(View.GONE);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }

                    }
                });
    }
}
