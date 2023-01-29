package com.example.m_ployer.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.m_ployer.FragmentReplacerActivity;
import com.example.m_ployer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.m_ployer.fragment.CreatAccountFragment.EMAIL_REGEX;

public class ForgotPassword extends Fragment {
    private TextView loginTv;
    private Button recoverBtn;
    private EditText emailET;
    private ProgressBar progressBar;
    FirebaseAuth auth;


  public ForgotPassword() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clicklistener();
    }

    private void init(View view) {
      loginTv=view.findViewById(R.id.LogInTv);
      recoverBtn=view.findViewById(R.id.recoverbtn);
      emailET=view.findViewById(R.id.emailET);
      progressBar=view.findViewById(R.id.progressbar);

        auth=FirebaseAuth.getInstance();
    }

    private void clicklistener() {
      loginTv.setOnClickListener(v ->
              ((FragmentReplacerActivity) getActivity()).setFragment(new LoginFragment()));
      recoverBtn.setOnClickListener(new View.OnClickListener() {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override
          public void onClick(View v) {
              String email=emailET.getText().toString();

              if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
                  emailET.setError("Input Valid email ");
                  return;
              }
              progressBar.setVisibility(View.VISIBLE);
              auth.sendPasswordResetEmail(email)
                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"password reset email send successfully",
                                        Toast.LENGTH_SHORT).show();
                                emailET.setText("");
                            }else{
                                Toast.makeText(getContext(),"Error: "+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                              progressBar.setVisibility(View.GONE);
                          }
                      });
          }
      });
    }

}