package com.example.m_ployer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.m_ployer.R;
import com.example.m_ployer.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    private List<Users> list;

    OnUserClicked onUserClicked;

    public UserAdapter(List<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder,  int position) {



            if (list.get(position).getUId().equals(User.getUid())) {
                holder.layout.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            } else {
                holder.layout.setVisibility(View.VISIBLE);
            }


        holder.nameTVUser.setText(list.get(position).getName());
        holder.StatusTVUser.setText(list.get(position).getStatus());

        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImageUser);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OnUserClicked.onClicked(list.get(position).getUId()); }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnUserClicked(OnUserClicked onUserClicked){
        this.onUserClicked=onUserClicked;
    }
    public interface OnUserClicked{
        static void onClicked(String uId) {

        }
    }


    static class UserHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImageUser;
        private TextView nameTVUser, StatusTVUser;
        private RelativeLayout layout;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            profileImageUser = itemView.findViewById(R.id.profileImageUser);
            nameTVUser = itemView.findViewById(R.id.nameTVUser);
            StatusTVUser = itemView.findViewById(R.id.statusTVUser);
            layout = itemView.findViewById(R.id.recyclerViewlayout);

        }


    }


}
