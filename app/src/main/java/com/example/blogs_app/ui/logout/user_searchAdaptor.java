package com.example.blogs_app.ui.logout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blogs_app.R;
import com.example.blogs_app.data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_searchAdaptor extends RecyclerView.Adapter<user_searchAdaptor.ViewHolder>implements Filterable {
    ArrayList<data> user_info;
    ArrayList<data> itemssAll;
    Context context;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    private boolean test_follw = false;
    private OnItemClickListener mlistener;


    public interface OnItemClickListener
    {
        void Edit_item(int position);
    }
    public  void  setOnClickListener(OnItemClickListener listener)
    {
        this.mlistener = listener;
    }

    public user_searchAdaptor(ArrayList<data> user_info, Context context) {
        this.user_info = user_info;
        this.context = context;
        this.itemssAll = new ArrayList<>(user_info);
    }

    @NonNull
    @Override
    public user_searchAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.usersprofile, parent, false);
        return new ViewHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull user_searchAdaptor.ViewHolder holder, int position) {
        holder.username_userprofile.setText(user_info.get(position).getName());
        holder.useremail_userprofile.setText(user_info.get(position).getEmail());
        Glide.with(context).load(user_info.get(position).getUserphoto()).into(holder.userimg_userprofile);
        holder.get_follow(user_info.get(position).getUserid());
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("follwers");
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test_follw = true;

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (test_follw) {
                                if (snapshot.child(user.getUid()).hasChild(user_info.get(position).getUserid())) {
                                    reference.child(user.getUid()).child(user_info.get(position).getUserid()).removeValue();
                                    test_follw = false;
                                } else {
                                    reference.child(user.getUid()).child(user_info.get(position).getUserid()).setValue(user_info.get(position).getUserid());

                                    test_follw = false;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            }
        });

    }

    @Override
    public int getItemCount() {
        return user_info.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView useremail_userprofile, username_userprofile;
        CircleImageView userimg_userprofile;
        ImageView follow;
        FirebaseDatabase firebaseDatabase;
        FirebaseAuth auth;
        DatabaseReference databaseReference;

        public void get_follow(String userid) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(auth.getCurrentUser().getUid()).hasChild(userid)) {
                        follow.setImageResource(R.drawable.ic_favorite);

                    } else {
                        follow.setImageResource(R.drawable.ic_not_favourite);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            username_userprofile = itemView.findViewById(R.id.username_userprofile);
            useremail_userprofile = itemView.findViewById(R.id.useremail_userprofile);
            userimg_userprofile = itemView.findViewById(R.id.userimg_userprofile);
            follow = itemView.findViewById(R.id.follow_user);
            firebaseDatabase = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            databaseReference = firebaseDatabase.getReference().child("follwers");
            databaseReference.keepSynced(true);
            username_userprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener != null)
                    {
                        int postion = getAdapterPosition();
                        if(postion != RecyclerView.NO_POSITION)
                        {
                            listener.Edit_item(postion);
                        }
                    }
                }
            });

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        // background thread
        protected FilterResults performFiltering(CharSequence keyword)
        {
            ArrayList<data> filtereddata=new ArrayList<>();

            if(keyword.toString().isEmpty())
                filtereddata.addAll(itemssAll);
            else
            {
                for(data obj : itemssAll)
                {
                    if(obj.getName().toLowerCase().contains(keyword.toString().toLowerCase()))
                        filtereddata.add(obj);
                }
            }

            FilterResults results=new FilterResults();
            results.values=filtereddata;
            return results;
        }

        @Override  // main UI thread
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            user_info.clear();
            user_info.addAll((ArrayList<data>)results.values);
            notifyDataSetChanged();
        }
    };




}
