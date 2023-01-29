package com.example.m_ployer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_ployer.Adapter.HomeAdapter;
import com.example.m_ployer.R;
import com.example.m_ployer.model.HomeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends Fragment {

    private RecyclerView recyclerView;

    HomeAdapter adapter;
    private List<HomeModel> list;
    private FirebaseUser user;
    public androidx.appcompat.widget.Toolbar toolbar;

    private final MutableLiveData<Integer> commentCount= new MutableLiveData<>();




    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        list = new ArrayList<>();
        adapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);

        loadDataFromFirestore();

        adapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String Uid, List<String> likedList, boolean isChecked) {
                DocumentReference reference=FirebaseFirestore.getInstance().collection("users")
                        .document(Uid)
                        .collection("post Images")
                        .document(id);

                if(likedList.contains((user.getUid())) ){
                    likedList.remove(user.getUid()); //Unlike
                }else {
                    likedList.add(user.getUid()); //like
                }

                Map<String, Object> map=new HashMap<>();
                map.put("likes",likedList);

                reference.update(map);
            }



            @Override
            public void onApplied(int position, String id, String Uid,List<String> appliedList,boolean isChecked) {

                DocumentReference reference=FirebaseFirestore.getInstance().collection("users")
                        .document(Uid)
                        .collection("post Images")
                        .document(id);

                if(appliedList.contains((user.getUid()))  ){
                    appliedList.remove(user.getUid()); //Unapply
                }else {
                    appliedList.add(user.getUid()); //Apply
                }
                Map<String, Object> map=new HashMap<>();
                map.put("Applied",appliedList);

                reference.update(map);
            }

            @Override
            public void setCommentCount(TextView textView) {
                Activity activity=getActivity();

                commentCount.observe((LifecycleOwner) activity, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {

                        if(commentCount.getValue() == 0){
                            textView.setVisibility(View.GONE);
                        }else
                            textView.setVisibility(View.VISIBLE);
                        textView.setText("See all"+commentCount.getValue()+" Comments");
                    }
                });
            }
        });
    }

   private void init(View view) {

        toolbar = view.findViewById(R.id.Toolbar);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    private void loadDataFromFirestore() {
        final DocumentReference reference = FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid());

        final CollectionReference collectionReference=FirebaseFirestore.getInstance().collection("users");

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Log.e("Error",error.getMessage());
                    return;
                }
                if(value==null){
                    return;
                }


                List<String> uidList;
                uidList = Collections.singletonList(String.valueOf(value.get("following")));
                if(uidList ==null || uidList.isEmpty()){
                    return;
                }

                collectionReference.whereIn("UId",uidList)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if(error != null){
                                    Log.e("Error",error.getMessage());
                                    return;
                                }

                                if(value == null)
                                    return;
                                for(QueryDocumentSnapshot snapshot: value){
                                    snapshot.getReference().collection("post Images")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                    if(error != null){
                                                        Log.e("Error",error.getMessage());
                                                        return;
                                                    }

                                                    if(value== null)
                                                        return;
                                                    list.clear();

                                                    for (QueryDocumentSnapshot snapshot : value) {

                                                        if (!snapshot.exists())
                                                            return;
                                                        HomeModel model = snapshot.toObject(HomeModel.class);
                                                        list.add(new HomeModel(
                                                                model.getUserName(),
                                                                model.getProfileImage(),
                                                                model.getImageUrl(),
                                                                model.getUid(),
                                                                model.getDescription(),
                                                                model.getId(),
                                                                model.getTimestamp(),
                                                                model.getLiked(),
                                                                model.getApplied()
                                                        ));
                                                        snapshot.getReference().collection("Comments").get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                        if(task.isSuccessful()){

                                                                            int count=0;
                                                                            for(QueryDocumentSnapshot snapshot1 : task.getResult()){
                                                                                count ++;
                                                                            }
                                                                            commentCount.setValue(count);
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                }

                            }
                        });
            }
        });

    }
}