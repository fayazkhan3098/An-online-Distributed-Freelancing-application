package com.example.m_ployer;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.m_ployer.fragment.Comment;
import com.example.m_ployer.fragment.CreatAccountFragment;
import com.example.m_ployer.fragment.LoginFragment;

public class FragmentReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_replacer);

        frameLayout=findViewById(R.id.frameLayout);

        Boolean isComment= getIntent().getBooleanExtra("isComment",false);

        if(isComment)
            setFragment(new Comment());
        else
            setFragment(new LoginFragment());
        setFragment(new LoginFragment());
    }
    public void setFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right );

        if(fragment instanceof CreatAccountFragment){
            fragmentTransaction.addToBackStack(null);
        }

        if(fragment instanceof Comment){
            String id=getIntent().getStringExtra("id");
            String uid=getIntent().getStringExtra("Uid");

            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            bundle.putString("Uid",uid);
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}