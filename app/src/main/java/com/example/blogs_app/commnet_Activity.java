package com.example.blogs_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class commnet_Activity extends AppCompatActivity {
    String postkey;
    RecyclerView recyclerView;
    ArrayList<commnet_details> commnet_detailsArrayList;
    commentAdaoptor adaoptor;
    ImageView img_post_com, send_post_com, userimg_post_com, com_img;
    ProgressBar progressbar_post_com;
    TextView post_com, title_post_com, desc_post_com, username_post_com, com_username, date_post_com;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Dialog dialog;

    void calling() {
        recyclerView = findViewById(R.id.commnets_recycle_view);
        username_post_com = findViewById(R.id.username_post_com);
        desc_post_com = findViewById(R.id.desc_post_com);
        title_post_com = findViewById(R.id.title_post_com);
        post_com = findViewById(R.id.post_com);
        progressbar_post_com = findViewById(R.id.progressbar_post_com);
        com_img = findViewById(R.id.com_img);
        com_username = findViewById(R.id.com_username);
        date_post_com = findViewById(R.id.date_post_com);
        userimg_post_com = findViewById(R.id.userimg_userprofile);
        send_post_com = findViewById(R.id.send_post_com);
        img_post_com = findViewById(R.id.img_post_com);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commnet_);
        calling();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(commnet_Activity.this));
        recyclerView.setHasFixedSize(true);
        String postimg = getIntent().getExtras().getString("postimg");
        String title = getIntent().getExtras().getString("title");
        String desc = getIntent().getExtras().getString("desc");
        String username = getIntent().getExtras().getString("username");
        String userid = getIntent().getExtras().getString("userid");
        String userimg = getIntent().getExtras().getString("userimg");
        String postdate = getIntent().getExtras().getString("postdate");
        postkey = getIntent().getExtras().getString("postkey");
        Glide.with(this).load(userimg).into(com_img);
        Glide.with(this).load(postimg).into(img_post_com);
        Glide.with(this).load(user.getPhotoUrl()).into(userimg_post_com);
        username_post_com.setText(user.getDisplayName());
        com_username.setText(username);
        date_post_com.setText(postdate);
        title_post_com.setText(title);
        desc_post_com.setText(desc);
        recy();
        Date currenttime = Calendar.getInstance().getTime();
        String post_date = DateFormat.getInstance().format(currenttime);
        send_post_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressbar_post_com.setVisibility(View.VISIBLE);
                    send_post_com.setVisibility(View.INVISIBLE);
                    databaseReference = firebaseDatabase.getReference("comment").child(postkey).push();
                    databaseReference.setValue(new commnet_details(post_com.getText().toString(), postkey, databaseReference.getKey(), post_date, user.getDisplayName(), user.getUid(), user.getPhotoUrl().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "comment..successfully..!!!", Toast.LENGTH_LONG).show();
                            send_post_com.setVisibility(View.VISIBLE);
                            progressbar_post_com.setVisibility(View.INVISIBLE);
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    void recy() {

        try {
            DatabaseReference databaseReference1 = firebaseDatabase.getReference("comment").child(postkey);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    commnet_detailsArrayList = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        /*
                        commnet_detailsArrayList.add(new commnet_details(
                                d.child("comment").getValue().toString()
                                , d.child("posyt_id").getValue().toString()
                                , d.child("comment_id").getValue().toString()
                                , d.child("commnet_date").getValue().toString()
                                , d.child("user_name").getValue().toString()
                                , d.child("user_id").getValue().toString()
                                , d.child("user_img").getValue().toString()
                        ));

                         */
                        commnet_details commnetDetails = d.getValue(commnet_details.class);
                        commnet_detailsArrayList.add(commnetDetails);
                    }
                    adaoptor = new commentAdaoptor(commnet_Activity.this, commnet_detailsArrayList);
                    recyclerView.setAdapter(adaoptor);
                    adaoptor.setOnItemClickListener(new commentAdaoptor.OnItemClickListener() {
                        @Override
                        public void onDeleteClick(int position) {
                            removeItem(position);
                        }

                        @Override
                        public void onEditClick(int position) {

                            changeItem(position);
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public void removeItem(int position) {
        if (commnet_detailsArrayList.get(position).getUser_id().compareTo(user.getUid()) == 0) {
            databaseReference = firebaseDatabase.getReference("comment");
            databaseReference.child(commnet_detailsArrayList.get(position).getPosyt_id()).child(commnet_detailsArrayList.get(position).getComment_id()).setValue(null);
            adaoptor.notifyItemRemoved(position);

        }
    }

    public void changeItem(int position) {
        if (commnet_detailsArrayList.get(position).getUser_id().compareTo(user.getUid()) == 0) {
            dialog = new Dialog(commnet_Activity.this);
            dialog.setContentView(R.layout.edit_delete_comments);
            dialog.getWindow().getAttributes().gravity = Gravity.TOP;

            ImageView userimg_editcomment = dialog.findViewById(R.id.userimg_userprofile);
            ImageView edit_comment_btn = dialog.findViewById(R.id.edit_comment_btn);
            TextView edit_comment_text = dialog.findViewById(R.id.edit_comment_text2);
            TextView username_edit_comment = dialog.findViewById(R.id.username_edit_comment1);
            Glide.with(commnet_Activity.this).load(commnet_detailsArrayList.get(position).getUser_img()).into(userimg_editcomment);
            username_edit_comment.setText(commnet_detailsArrayList.get(position).getUser_name());
            dialog.show();

            edit_comment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edit_comment_text.getText().toString().isEmpty()) {
                        edit_comment_text.setError("Enter your comment");
                    } else {

                        databaseReference = firebaseDatabase.getReference("comment");
                        databaseReference.child(commnet_detailsArrayList.get(position).getPosyt_id())
                                .child(commnet_detailsArrayList.get(position).getComment_id())
                                .setValue(new commnet_details(edit_comment_text.getText().toString(), commnet_detailsArrayList.get(position).getPosyt_id()
                                        , commnet_detailsArrayList.get(position).getComment_id()
                                        , commnet_detailsArrayList.get(position).getCommnet_date()
                                        , commnet_detailsArrayList.get(position).getUser_name(),
                                        commnet_detailsArrayList.get(position).getUser_id()
                                        ,commnet_detailsArrayList.get(position).getUser_img() ));
                        commnet_detailsArrayList.get(position).setComment(edit_comment_text.getText().toString());
                        adaoptor.notifyItemChanged(position);
                        dialog.dismiss();
                    }

                }
            });

        }
    }

}
