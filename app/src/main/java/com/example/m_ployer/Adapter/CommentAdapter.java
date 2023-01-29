package com.example.m_ployer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.m_ployer.R;
import com.example.m_ployer.model.CommentModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {


    Context context;
     List<CommentModel> list;


    public CommentAdapter(Context context, List<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.comment_items,parent,false);

        return new CommentHolder(view);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position).getProfileImageUrl())
                .into(holder.profileImage);
        holder.nameTV.setText(list.get(position).getName());
        holder.commentTV.setText(list.get(position).getComment());
    }

    static class CommentHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView nameTV, commentTV;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);

            profileImage=itemView.findViewById(R.id.profileImage);
            nameTV=itemView.findViewById(R.id.CommentNameTV);
            commentTV=itemView.findViewById(R.id.commentTV);
        }
    }
}
