package com.example.blogs_app.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogs_app.R;
import com.example.blogs_app.commnet_Activity;
import com.example.blogs_app.message_data;
import com.example.blogs_app.postAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class home extends Fragment {
    RecyclerView recyclerView;
    postAdaptor postAdaptor;
    ArrayList<message_data> posts;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView Like_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        posts.add(new message_data(d.child("title").getValue().toString(), d.child("description").getValue().toString(), d.child("user_name").getValue().toString(), d.child("user_id").getValue().toString(), d.child("post_image").getValue().toString(), d.child("user_image").getValue().toString(), d.child("post_key").getValue().toString(), d.child("date").getValue().toString(), d.child("like_state").getValue().toString()));


                    }
                    postAdaptor = new postAdaptor(getActivity(), posts);
                    recyclerView.setAdapter(postAdaptor);
                    postAdaptor.setOnItemClickListener(new postAdaptor.OnItemClickListener() {
                        @Override
                        public void onEditClick(int position) {
                            Edit(position);
                        }




                    });


                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_home_page, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        Like_view = v.findViewById(R.id.like_btn1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("message");

        return v;
    }

    public void Edit(int positon) {

        Intent intent = new Intent(getContext(), commnet_Activity.class);
        intent.putExtra("title", posts.get(positon).getTitle());
        intent.putExtra("desc", posts.get(positon).getDescription());
        intent.putExtra("postimg", posts.get(positon).getPost_image());
        intent.putExtra("username", posts.get(positon).getUser_name());
        intent.putExtra("userid", posts.get(positon).getUser_name());
        intent.putExtra("userimg", posts.get(positon).getUser_image());
        intent.putExtra("postdate", posts.get(positon).getDate());
        intent.putExtra("postkey", posts.get(positon).getPost_key());
        postAdaptor.notifyItemChanged(positon);
        getContext().startActivity(intent);

    }
/*
    public void Like(int position) {
        databaseReference = firebaseDatabase.getReference("message");
        if ((Integer.toString(R.drawable.like).compareTo(posts.get(position).getLike_state()) == 0)) {
            databaseReference.child(posts.get(position).getPost_key())
                    .setValue(new message_data(posts.get(position).getTitle()
                            , posts.get(position).getDescription()
                            , posts.get(position).getUser_name()
                            , posts.get(position).getUser_id()
                            , posts.get(position).getPost_image()
                            , posts.get(position).getUser_image()
                            , posts.get(position).getPost_key()
                            , posts.get(position).getDate()
                            , Integer.toString(R.drawable.liked)
                    ));
            posts.get(position).setLike_state(Integer.toString(R.drawable.liked));

        } else {
            databaseReference.child(posts.get(position).getPost_key())
                    .setValue(new message_data(posts.get(position).getTitle()
                            , posts.get(position).getDescription()
                            , posts.get(position).getUser_name()
                            , posts.get(position).getUser_id()
                            , posts.get(position).getPost_image()
                            , posts.get(position).getUser_image()
                            , posts.get(position).getPost_key()
                            , posts.get(position).getDate()
                            , Integer.toString(R.drawable.like)
                    ));
            posts.get(position).setLike_state(Integer.toString(R.drawable.like));


        }

    }
    */

}
