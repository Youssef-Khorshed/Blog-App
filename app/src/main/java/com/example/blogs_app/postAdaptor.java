package com.example.blogs_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class postAdaptor extends RecyclerView.Adapter<postAdaptor.ViewHolder> implements Filterable {
    Context context;
    ArrayList<message_data> post;
    ArrayList<message_data> itemssAll;
    private OnItemClickListener mlistenerl;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private boolean like_tst = false;

    public interface OnItemClickListener {
        void onEditClick(int position);


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mlistenerl = listener;
    }

    public postAdaptor(Context context, ArrayList<com.example.blogs_app.message_data> post) {
        this.context = context;
        this.post = post;
        this.itemssAll = new ArrayList<>(post);
    }

    @NonNull
    @Override
    public postAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.show_message_home, parent, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference().child("like");
        databaseReference.keepSynced(true);
        return new ViewHolder(view, mlistenerl);
    }

    @Override
    public void onBindViewHolder(@NonNull postAdaptor.ViewHolder holder, int position) {
        holder.post_title.setText(post.get(position).getTitle());
        holder.post_desc.setText(post.get(position).getDescription());
        holder.textView.setText(post.get(position).getUser_name());
        holder.post_date.setText(post.get(position).getDate());
        Glide.with(context).load(post.get(position).getPost_image()).into(holder.post_img);
        Glide.with(context).load(post.get(position).getUser_image()).into(holder.user_view);
        holder.get_like(post.get(position).getPost_key());
        holder.like_btn.setImageResource(R.drawable.like);
        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_tst = true;


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (like_tst) {
                            if (snapshot.child(post.get(position).getPost_key()).hasChild(auth.getCurrentUser().getUid())) {
                                databaseReference.child(post.get(position).getPost_key()).child(auth.getCurrentUser().getUid()).removeValue();
                                like_tst = false;
                            } else {
                                databaseReference.child(post.get(position).getPost_key()).child(auth.getCurrentUser().getUid()).setValue("rand");
                                like_tst = false;
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
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView post_title, post_desc, textView, post_date;
        public ImageView post_img, user_view, like_btn, message_btn;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        FirebaseAuth auth;

        public void get_like(String postkey) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postkey).hasChild(auth.getCurrentUser().getUid())) {
                        like_btn.setImageResource(R.drawable.ic_favorite);
                    } else {
                        like_btn.setImageResource(R.drawable.ic_not_favourite);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            post_title = itemView.findViewById(R.id.post_title);
            post_desc = itemView.findViewById(R.id.post_desc);
            post_img = itemView.findViewById(R.id.post_img);
            textView = itemView.findViewById(R.id.username_post_com);
            user_view = itemView.findViewById(R.id.userimg_userprofile);
            post_date = itemView.findViewById(R.id.post_date);
            message_btn = itemView.findViewById(R.id.message_btn);
            like_btn = itemView.findViewById(R.id.like_btn1);
            firebaseDatabase = FirebaseDatabase.getInstance();
            auth = FirebaseAuth.getInstance();
            databaseReference = firebaseDatabase.getReference().child("like");
            databaseReference.keepSynced(true);

/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, commnet_Activity.class);
                    int positon = getAdapterPosition();
                    intent.putExtra("title", post.get(positon).getTitle());
                    intent.putExtra("desc", post.get(positon).getDescription());
                    intent.putExtra("postimg", post.get(positon).getPost_image());
                    intent.putExtra("username", post.get(positon).getUser_name());
                    intent.putExtra("userid", post.get(positon).getUser_name());
                    intent.putExtra("userimg", post.get(positon).getUser_image());
                    intent.putExtra("postdate", post.get(positon).getDate());
                    intent.putExtra("postkey", post.get(positon).getPost_key());

                    context.startActivity(intent);
                }
            });


 */


            message_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
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
            ArrayList<message_data> filtereddata=new ArrayList<>();

            if(keyword.toString().isEmpty())
                filtereddata.addAll(itemssAll);
            else
            {
                for(message_data obj : itemssAll)
                {
                    if(obj.getUser_name().toLowerCase().contains(keyword.toString().toLowerCase()))
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
            post.clear();
            post.addAll((ArrayList<message_data>)results.values);
            notifyDataSetChanged();
        }
    };



}
