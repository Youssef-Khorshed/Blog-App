package com.example.blogs_app;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.blogs_app.ui.logout.user_searchAdaptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.blogs_app.ui.logout.home;
import com.example.blogs_app.ui.logout.profile;
import com.example.blogs_app.ui.logout.sitting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyhomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,profile.Senddata    {
    static int req = 1, img_code = 2, fragemnt_number = 0;
    static Uri img_uri = null;
    FirebaseAuth auth;
    FirebaseUser user;
    Dialog set_message;
    ImageView user_message_img, user_message_img2 = null, user_message_send;
    TextView user_message_title, user_message_description;
    ProgressBar user_message_progressbar;
    DrawerLayout drawer;
    user_searchAdaptor searchAdaptor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhome);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //message_body_intialization
        message();
        upload_message();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        update();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framer, new home()).commit();
            navigationView.setCheckedItem(R.id.Home);
            fragemnt_number = 1;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    private void message() {
        set_message = new Dialog(this);
        set_message.setContentView(R.layout.message_description);
        set_message.getWindow().getAttributes().gravity = Gravity.TOP;
        user_message_img = set_message.findViewById(R.id.user_post_img);
        user_message_img2 = set_message.findViewById(R.id.user_message_img2);
        user_message_send = set_message.findViewById(R.id.send_post_com);
        user_message_title = set_message.findViewById(R.id.user_message_title);
        user_message_description = set_message.findViewById(R.id.user_message_description);
        user_message_progressbar = set_message.findViewById(R.id.progressbar_post_com);
        Glide.with(this).load(user.getPhotoUrl()).into(user_message_img);
        user_message_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user_message_title.getText().toString().trim().isEmpty()) {

                    user_message_title.setError("Enter your Name");
                } else if (user_message_description.getText().toString().trim().isEmpty()) {
                    user_message_description.setError("Enter your Description");

                } else if (img_uri == null) {
                    Toast.makeText(getApplicationContext(), "Select your image", Toast.LENGTH_SHORT).show();

                } else {
                    user_message_progressbar.setVisibility(View.VISIBLE);
                    user_message_send.setVisibility(View.INVISIBLE);

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User_message_post_photos");
                    StorageReference referenc = storageReference.child(img_uri.getLastPathSegment());
                    referenc.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            referenc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String userimg = user.getPhotoUrl().toString();
                                    Date currenttime = Calendar.getInstance().getTime();
                                    String post_date = DateFormat.getInstance().format(currenttime);
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("message").push();
                                    //     String key = databaseReference.getKey();
                                    //     data.setPost_key(key);

                                    databaseReference.setValue(new message_data(
                                            user_message_title.getText().toString()
                                            , user_message_description.getText().toString()
                                            , user.getDisplayName()
                                            , user.getUid()
                                            , uri.toString()
                                            , userimg
                                            , user.getEmail()
                                            , databaseReference.getKey()
                                            , post_date


                                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            user_message_progressbar.setVisibility(View.INVISIBLE);
                                            user_message_send.setVisibility(View.VISIBLE);
                                            set_message.dismiss();
                                            Toast.makeText(getApplicationContext(), "post..added..successfully..!", Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }


                            });
                        }


                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            user_message_progressbar.setVisibility(View.INVISIBLE);
                            user_message_send.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });


    }

    private void upload_message() {

        user_message_img2.setOnClickListener(new View.OnClickListener() {
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
            user_message_img2.setImageURI(img_uri);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myhome, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings && fragemnt_number == 1) {
            set_message.show();
            return true;
        }
        else {

            return false;
        }
    }


    void update() {

        NavigationView navigationView1 = findViewById(R.id.nav_view);
        View navigationView = navigationView1.getHeaderView(0);
        TextView username = navigationView.findViewById(R.id.user_name1);
        TextView useremail = navigationView.findViewById(R.id.user_email1);
        CircleImageView imageView = navigationView.findViewById(R.id.user_img_photo);
        username.setText(user.getDisplayName());
        useremail.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(imageView);


    }

    void Push_message_data(message_data data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("message").push();
        //     String key = databaseReference.getKey();
        //     data.setPost_key(key);

        databaseReference.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                user_message_progressbar.setVisibility(View.INVISIBLE);
                user_message_send.setVisibility(View.VISIBLE);
                set_message.dismiss();
                Toast.makeText(getApplicationContext(), "post..added..successfully..!", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginForm.class));
                finish();
                break;
            case R.id.Profile:
                fragemnt_number = 2;
                getSupportFragmentManager().beginTransaction().replace(R.id.framer, new profile()).commit();
                break;
            case R.id.Sitting:
                fragemnt_number = 3;
                getSupportFragmentManager().beginTransaction().replace(R.id.framer, new sitting()).commit();
                break;
            case R.id.Home:
                fragemnt_number = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.framer, new home()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void send(String data) {
        home.str = data;

    }
}