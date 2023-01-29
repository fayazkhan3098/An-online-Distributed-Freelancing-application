package com.example.m_ployer.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.m_ployer.FragmentReplacerActivity;
import com.example.m_ployer.R;
import com.example.m_ployer.model.HomeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    Activity context;
    private List<HomeModel> list;

    OnPressed onPressed;


    public HomeAdapter(List<HomeModel> list, Activity context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        holder.userNameTv.setText(list.get(position).getUserName());
        holder.timeTv.setText(" "+list.get(position).getTimestamp());

        List<String> Likelist =list.get(position).getLiked();
        int count = Likelist.size();
        if(count==0){
            holder.likeCountTv.setVisibility(View.INVISIBLE);
        }else if(count==1){
            holder.likeCountTv.setText(count+ " Like");
        }else {
            holder.likeCountTv.setText(count+ " Likes");
        }
//      Check if already liked
        holder.likeCheckbox.setChecked(Likelist.contains(user.getUid()));


        List<String> ApplyList =list.get(position).getApplied();
        int countApply = ApplyList.size();
        if(countApply==0){
            holder.ApplyTV.setVisibility(View.INVISIBLE);
        }else {
            holder.ApplyTV.setText(countApply+ " Applied");
        }

        // check if already applied
        holder.applyCheckbox.setChecked(Likelist.contains(user.getUid()));


        holder.descriptionTv.setText(list.get(position).getDescription());

        Random random= new Random();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        Glide.with(context.getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.ic_person)
                .timeout(6500)
                .into(holder.profileImage);
        Glide.with(context.getApplicationContext())
                .load(list.get(position).getImageUrl())
                .placeholder(new ColorDrawable(color))
                .timeout(7000)
                .into(holder.imageView);

        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getUid(),
                list.get(position).getUserName(),
                list.get(position).getLiked(),
                list.get(position).getApplied()
        );


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnPressed(OnPressed onPressed){
        this.onPressed=onPressed;
    }

    public interface OnPressed{
        void onLiked(int position,String id, String Uid,List<String> LikedList, boolean isChecked);
       // void onComment(int position, String id, String Uid, String Comment, LinearLayout commentlayout, EditText commentET);
        void onApplied(int position,String id, String Uid,List<String> appliedList, boolean isChecked);
        void setCommentCount(TextView textView);
    }


    class HomeHolder extends RecyclerView.ViewHolder{

        private final CircleImageView profileImage;
        private final TextView userNameTv, timeTv, likeCountTv, descriptionTv, ApplyTV, commentTV;
        private final ImageView imageView;
        private final CheckBox likeCheckbox, applyCheckbox;
        private final ImageButton  commentBtn, shareBtn;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);


            profileImage=itemView.findViewById(R.id.profileimage);
            imageView=itemView.findViewById(R.id.imageView);
            userNameTv=itemView.findViewById(R.id.nametv);
            timeTv=itemView.findViewById(R.id.timetv);
            likeCountTv=itemView.findViewById(R.id.likecountTv);
            ApplyTV=itemView.findViewById(R.id.apllycountTv);
            likeCheckbox=itemView.findViewById(R.id.likeBtn);
            applyCheckbox=itemView.findViewById(R.id.applyBtn);
            commentBtn=itemView.findViewById(R.id.commentBtn);
            shareBtn=itemView.findViewById(R.id.shareBtn);
            descriptionTv=itemView.findViewById(R.id.DescTV);
            commentTV=itemView.findViewById(R.id.commentTV);

            onPressed.setCommentCount(commentTV);

        }

        public void clickListener(int position, String id, String userName, String Uid, List<String> liked, List<String> applied) {


            commentBtn.setOnClickListener(v -> {


                 Intent intent= new Intent(context, FragmentReplacerActivity.class);
                 intent.putExtra("id",id);
                 intent.putExtra("Uid",Uid);
                 intent.putExtra("isComment",true);

                 context.startActivity(intent);

            });



            likeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    onPressed.onLiked(position, id, Uid, liked, isChecked)
            );



            applyCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> onPressed.onApplied(position, id, Uid, applied, isChecked));
        }
    }

}
