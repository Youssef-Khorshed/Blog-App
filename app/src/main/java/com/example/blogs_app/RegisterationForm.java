package com.example.blogs_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterationForm extends AppCompatActivity {
    static int req = 1, img_code = 2;
    static Uri img_uri = null;
    static boolean check_user = false;
    CircleImageView imageView;
    EditText Name, Password, Phone, Email;
    CountryCodePicker cpp;
    Button Register;
    ProgressBar p;
    FirebaseAuth auth;

    void Registeration() {
        auth = FirebaseAuth.getInstance();
        Name = findViewById(R.id.Name);
        Password = findViewById(R.id.Password);
        p = findViewById(R.id.progressBar);
        p.setVisibility(View.INVISIBLE);
        Phone = findViewById(R.id.Phone);
        Email = findViewById(R.id.Email);
        cpp = findViewById(R.id.ccp);
        Register = findViewById(R.id.Reigster);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Name.getText().toString().trim().isEmpty()) {
                    Name.setError("Enter your Name");
                } else if (Email.getText().toString().trim().isEmpty()) {
                    Email.setError("Enter your Email");
                } else if (Phone.getText().toString().trim().isEmpty()) {
                    Phone.setError("Enter your Phone");
                } else if (Password.getText().toString().trim().isEmpty()) {
                    Password.setError("Enter your Password");
                } else if (Password.getText().toString().trim().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Please Enter 6 numbers at least", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    Email.setError("Enter correct email");
                } else if (Phone.getText().toString().replace(" ", "").trim().length() != 10) {
                    Phone.setError("please enter only 10 numbers");
                } else if (img_uri == null) {
                    Toast.makeText(getApplicationContext(), "Please select your image", Toast.LENGTH_SHORT).show();
                } else {
                    Create_User_Account(Email.getText().toString(), Name.getText().toString(), Password.getText().toString());
                    cpp.registerCarrierNumberEditText(Phone);
                    Push_data(new data(Name.getText().toString(), Password.getText().toString(), cpp.getFullNumberWithPlus().replace(" ", ""), Email.getText().toString()));
                }


            }


        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        image_action();
        Registeration();
    }


    void go_to_next_page() {
        startActivity(new Intent(RegisterationForm.this, LoginForm.class));
        finish();

    }

    void image_action() {
        imageView = findViewById(R.id.userimg_editcomment1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission();
            }
        });

    }

    void pick_image() {
        startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), img_code);
    }

    void permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req);
        } else {

            pick_image();
        }
    }

    void Push_data(data data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("user_info").push().setValue(data);
    }

    void User_information(String name, Uri img_uri, FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User_photos");
        StorageReference reference = storageReference.child(img_uri.getLastPathSegment());
        reference.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // imgae uploaded succesuffully
                // we collect uri image
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    go_to_next_page();
                                      Toast.makeText(getApplicationContext(), "register comleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
                    }

                });

            }
        });
    }

    void Create_User_Account(String Email, String Name, String Password) {


        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "please..wait", Toast.LENGTH_SHORT).show();
                    Please_wait();
                    User_information(Name, img_uri, auth.getCurrentUser());

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == req) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, req);

            } else {
                pick_image();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == img_code && data != null) {
            //user picked the image successuflly....!!!!!!
            img_uri = data.getData();
            imageView.setImageURI(img_uri);

        }

    }

    void Please_wait()
    {
        p.setVisibility(View.VISIBLE);
        Register.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        p.setVisibility(View.INVISIBLE);
        Register.setVisibility(View.VISIBLE);
    }
}