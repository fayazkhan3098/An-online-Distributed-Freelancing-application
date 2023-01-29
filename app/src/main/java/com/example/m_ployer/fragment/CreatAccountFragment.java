package com.example.m_ployer.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.m_ployer.AgeCalculation;
import com.example.m_ployer.FragmentReplacerActivity;
import com.example.m_ployer.MainActivity;
import com.example.m_ployer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CreatAccountFragment extends Fragment  {

private EditText nameEt, emailEt, passwordEt, confirmpasswordEt, DOB;
private ProgressBar progressBar;
private TextView loginTV;
private Button signUpBtn;
private FirebaseAuth auth;
private String DateOfBirth;
private int year, month,day;


public static final String EMAIL_REGEX ="^(.*)@(.*)$";

    public CreatAccountFragment() {
        // Required empty public constructor
    }
    AgeCalculation age=new AgeCalculation();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creat_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clickListener();
    }
    public void init(View view) {
        nameEt=view.findViewById(R.id.nameET);
        emailEt=view.findViewById(R.id.emailET);
        passwordEt=view.findViewById(R.id.passwordET);
        confirmpasswordEt=view.findViewById(R.id.confirmpassET);
        loginTV=view.findViewById(R.id.loginTV);
        signUpBtn=view.findViewById(R.id.signupBtn);
        progressBar=view.findViewById(R.id.progressBar);
        DOB=view.findViewById(R.id.DoB);


        auth=FirebaseAuth.getInstance();
    }
    private void clickListener() {

      DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getActivity()).showDialog(999);
            }
        });
        loginTV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((FragmentReplacerActivity) getActivity()).setFragment(new LoginFragment());
        }
    });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String name= nameEt.getText().toString();
            String email= emailEt.getText().toString();
            String password= passwordEt.getText().toString();
            String conpassword= confirmpasswordEt.getText().toString();
            String DateofB= DOB.getText().toString();



            if(name.isEmpty() || name.equals(" ")){
                nameEt.setError("Please Input Valid Name ");
                return;
            }
            if(email.isEmpty() || !email.matches(EMAIL_REGEX)){
                emailEt.setError("Please Input Valid email ");
                return;
            }
            if(password.isEmpty() || password.length()<6){
                passwordEt.setError("Please Input Valid Password ");
                return;
            }
            if( !conpassword.equals(password)){
                confirmpasswordEt.setError("Password Not Matched ");
                return;
            }
           //  if(DateofB.isEmpty() || AgeCalculation.cage <18){
                        //    DOB.setError("age Must Be Above 18  ");
                        //   return;
           //  }
             progressBar.setVisibility(View.VISIBLE);
             createAccount(name, email, password, DateOfBirth);
        }


        });

    }

    private void createAccount(String name, String email, String password, String Dateofbirth) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user=auth.getCurrentUser();
                            String image="https://cdn4.vectorstock.com/i/1000x1000/08/33/profile-icon-male-user-person-avatar-symbol-vector-20910833.jpg";
                            UserProfileChangeRequest.Builder request=new UserProfileChangeRequest.Builder();
                            request.setDisplayName(name);
                            request.setPhotoUri(Uri.parse(image));
                            assert user != null;
                            user.updateProfile(request.build());



                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getContext(), "Email Verification link Sent", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            uploaduser(user, name, email, Dateofbirth );
                        }else {
                            progressBar.setVisibility(View.GONE);
                            String exception =task.getException().getMessage();
                            Toast.makeText(getContext(),"error: "+exception,Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }
    private void uploaduser(FirebaseUser user, String name, String email, String Dateofbirth) {
        List<String> list=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        Map<String, Object>map= new HashMap<>();

        map.put("name", name);
        map.put("email", email );
        map.put("DateOfBirth", Dateofbirth );
        map.put("profileImage", " ");
        map.put("UId", user.getUid());
        map.put("status", " " );
        map.put("search", name.toLowerCase() );


        map.put("following", list);
        map.put("followers", list1);

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            assert getActivity()!= null;
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
                            getActivity().finish();

                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }
    private final DatePickerDialog.OnDateSetListener myDateListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg1 = year;
             arg2 = month;
             arg3 = day;
            String date=((arg1)+"-"+(arg2+1)+"-"+(arg3));
            try {
                AgeCalculation.give(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}