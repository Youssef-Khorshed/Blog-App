package com.example.blogs_app;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class commentAdaoptor extends RecyclerView.Adapter<commentAdaoptor.ViewHolder> {
    Context context;
    Dialog dialog;
    ArrayList<commnet_details> commnet_detailsArrayList;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    private OnItemClickListener mListener;

    public commentAdaoptor(Context context, ArrayList<commnet_details> commnet_detailsArrayList) {
        this.context = context;
        this.commnet_detailsArrayList = commnet_detailsArrayList;
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public commentAdaoptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments, parent, false);
        return new ViewHolder(view, mListener);

    }

    @Override
    public void onBindViewHolder(@NonNull commentAdaoptor.ViewHolder holder, int position) {
        holder.comment.setText(commnet_detailsArrayList.get(position).getComment());
        holder.usernamecomment.setText(commnet_detailsArrayList.get(position).getUser_name());
        holder.comment_date.setText(commnet_detailsArrayList.get(position).getCommnet_date());
        Glide.with(context).load(commnet_detailsArrayList.get(position).getUser_img()).into(holder.userimgcomment);
        /*
        holder.delete_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                if(user.getUid() == commnet_detailsArrayList.get(position).getUser_id()) {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.edit_delete_comments);
                    dialog.getWindow().getAttributes().gravity = Gravity.TOP;

                    ImageView userimg_editcomment = dialog.findViewById(R.id.userimg_editcomment);
                    ImageView delete_edited_comment = dialog.findViewById(R.id.delete_edited_comment);
                    ImageView edit_comment_btn = dialog.findViewById(R.id.edit_comment_btn);
                    TextView edit_comment_text = dialog.findViewById(R.id.edit_comment_text1);
                    TextView username_edit_comment = dialog.findViewById(R.id.username_edit_comment);

                    Glide.with(context).load(commnet_detailsArrayList.get(position).getUser_img()).into(userimg_editcomment);
                    username_edit_comment.setText(commnet_detailsArrayList.get(position).getUser_name());
                    dialog.show();
                    delete_edited_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference = database.getReference("comment").child(commnet_detailsArrayList.get(position).getPosyt_id()).child(commnet_detailsArrayList.get(position).getComment_id());
                            databaseReference.setValue(null);
                            dialog.dismiss();
                        }
                    });

                    edit_comment_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Date currenttime = Calendar.getInstance().getTime();
                            String post_date = DateFormat.getInstance().format(currenttime);
                            databaseReference = database.getReference("comment").child(commnet_detailsArrayList.get(position).getPosyt_id()).child(commnet_detailsArrayList.get(position).getComment_id());
                            databaseReference.setValue(new commnet_details(edit_comment_text.getText().toString(),commnet_detailsArrayList.get(position).getPosyt_id(),commnet_detailsArrayList.get(position).getComment_id(), post_date, user.getDisplayName(),user.getUid(), user.getPhotoUrl().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "comment.edited..successfully..!!!", Toast.LENGTH_LONG).show();

                                }
                            });
                            dialog.dismiss();
                        }
                    });



                }
            }
        });
         */

    }

    @Override
    public int getItemCount() {
        return commnet_detailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView usernamecomment, comment, comment_date;
        public ImageView userimgcomment, delete_comment, Edit_click_lisener;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            userimgcomment = itemView.findViewById(R.id.userimg_userprofile);
            usernamecomment = itemView.findViewById(R.id.username_edit_comment1);
            comment = itemView.findViewById(R.id.edit_comment_text1);
            comment_date = itemView.findViewById(R.id.comment_date1);
            delete_comment = itemView.findViewById(R.id.delete_edited_comment);
            Edit_click_lisener = itemView.findViewById(R.id.edit_comment_btn);
            delete_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);

                        }
                    }
                }
            });
            Edit_click_lisener.setOnClickListener(new View.OnClickListener() {
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
}
