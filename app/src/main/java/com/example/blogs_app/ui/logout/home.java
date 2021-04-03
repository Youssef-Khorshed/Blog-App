package com.example.blogs_app.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogs_app.R;
import com.example.blogs_app.commnet_Activity;
import com.example.blogs_app.data;
import com.example.blogs_app.message_data;
import com.example.blogs_app.sharedviewmode;
import com.example.blogs_app.postAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ArrayList<String> fiterato = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView Like_view;
    public static String str = "";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("message");
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts = new ArrayList<>();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        message_data messageData = d.getValue(message_data.class);
                        posts.add(messageData);

                    }
                   Toast.makeText(getContext(), "hi "+str, Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.my_home_page, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        Like_view = v.findViewById(R.id.like_btn1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                postAdaptor.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }



}
