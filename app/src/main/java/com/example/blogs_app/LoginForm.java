package com.example.blogs_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class LoginForm extends AppCompatActivity {
    EditText new_email, new_password;
    Button register, Login;
    ProgressBar p;
    FirebaseAuth auth;

    void setRegister() {
        register = findViewById(R.id.new_register);
        p = findViewById(R.id.progressBar2);
        p.setVisibility(View.INVISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p.setVisibility(View.VISIBLE);
                register.setVisibility(View.INVISIBLE);
                Login.setVisibility(View.INVISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(2000);

                            startActivity(new Intent(LoginForm.this, RegisterationForm.class));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    void login() {
        auth = FirebaseAuth.getInstance();
        new_email = findViewById(R.id.My_email);
        new_password = findViewById(R.id.My_password);
        Login = findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                p.setVisibility(View.VISIBLE);
                register.setVisibility(View.INVISIBLE);
                Login.setVisibility(View.INVISIBLE);
                if (new_email.getText().toString().trim().isEmpty()) {
                    new_email.setError("Enter your Email");
                } else if (new_password.getText().toString().trim().isEmpty()) {
                    new_password.setError("Enter your Email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(new_email.getText().toString()).matches()) {
                    new_email.setError("Enter correct email");
                } else {
                    auth.signInWithEmailAndPassword(new_email.getText().toString(), new_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                p.setVisibility(View.VISIBLE);
                                register.setVisibility(View.INVISIBLE);
                                Login.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "successfull", Toast.LENGTH_SHORT).show();
                                go_to_homePage();
                            } else {
                                p.setVisibility(View.INVISIBLE);
                                register.setVisibility(View.VISIBLE);
                                Login.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login();
        setRegister();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            go_to_homePage();
        }
    }

    void go_to_homePage() {
        startActivity(new Intent(LoginForm.this, MyhomeActivity.class));
        finish();


    }

    @Override
    protected void onPause() {
        super.onPause();
        p.setVisibility(View.INVISIBLE);
        register.setVisibility(View.VISIBLE);
        Login.setVisibility(View.VISIBLE);
    }
}