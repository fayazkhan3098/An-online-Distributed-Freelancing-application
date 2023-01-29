package com.example.m_ployer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_ployer.Adapter.CommentAdapter;
import com.example.m_ployer.R;
import com.example.m_ployer.model.CommentModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment extends Fragment {

    EditText commentET;
    ImageButton sendCommentBtn;
    RecyclerView recyclerView;

    CommentAdapter commentAdapter;

    FirebaseUser user;

    CollectionReference reference;

    List<CommentModel>list;

    String id,uid;
    public Comment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        reference= FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .collection("post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();
        clickListener();
    }

    private void clickListener() {
        sendCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment= commentET.getText().toString();
                if(comment.isEmpty()){
                    Toast.makeText(getContext(), "Please ask something before sending it", Toast.LENGTH_SHORT).show();
                }



                String commentID=reference.document().getId();

                Map<String, Object> map=new HashMap<>();
                map.put("uid", user.getUid());
                map.put("comment", comment);
                map.put("commentID", commentID);
                map.put("postID", id);

                map.put("name", user.getDisplayName());
                map.put("profileImageUrl", user.getPhotoUrl().toString());

                reference.document(commentID)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    commentET.setText("");
                                }else{
                                    Toast.makeText(getContext(), "Failed To Ask : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void loadCommentData() {

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!= null)
                    return;
                if (value == null){
                    Toast.makeText(getContext(), "No Comments", Toast.LENGTH_SHORT).show();
                    return;}
                for(QueryDocumentSnapshot snapshot : value){
                    CommentModel model=snapshot.toObject(CommentModel.class);
                    list.add(model);
                }
                commentAdapter.notifyDataSetChanged();
            }
        });


    }

    private void init(View view) {

        commentET=view.findViewById(R.id.commentET);
        sendCommentBtn=view.findViewById(R.id.sendCommentBtn);
        recyclerView=view.findViewById(R.id.commentRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        user= FirebaseAuth.getInstance().getCurrentUser();

        list=new ArrayList<>();
        commentAdapter=new CommentAdapter(getContext(),list);
        recyclerView.setAdapter(commentAdapter);

        if(getArguments()== null)
            return;

        id=getArguments().getString("id");
        uid=getArguments().getString("Uid");
    }
}