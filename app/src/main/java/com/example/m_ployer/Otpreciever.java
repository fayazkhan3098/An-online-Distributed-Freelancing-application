package com.example.m_ployer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otpreciever extends AppCompatActivity {
    EditText t2;
    Button b2;
    String phoneNumber;
    private FirebaseAuth mAuth;
    private static final String TAG = "PhoneAuthActivity";
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpreciever);

        phoneNumber=getIntent().getStringExtra("mobile").toString();
        t2=(EditText)findViewById(R.id.OTPTextt);
        b2=(Button)findViewById(R.id.Processotp);
        mAuth = FirebaseAuth.getInstance();
        initiateotp();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t2.getText().toString().isEmpty())
                    Toast.makeText(getBaseContext(),"Blank fields can not be precess",Toast.LENGTH_LONG).show();
                else if(t2.getText().toString().length()!=6)
                    Toast.makeText(getBaseContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                else{
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential( mVerificationId,t2.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void initiateotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                                        // This callback will be invoked in two situations:
                                        // 1 - Instant verification. In some cases the phone number can be instantly
                                        //     verified without needing to send or enter a verification code.
                                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                        //     detect the incoming verification SMS and perform verification without
                                        //     user action.
                                        Log.d(TAG, "onVerificationCompleted:" + credential);
                                        signInWithPhoneAuthCredential(credential);
                                    }




                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        // This callback is invoked in an invalid request for verification is made,
                                        // for instance if the the phone number format is not valid.
                                        Log.w(TAG, "onVerificationFailed", e);

                                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                            // Invalid request
                                        } else if (e instanceof FirebaseTooManyRequestsException) {
                                            // The SMS quota for the project has been exceeded
                                        }

                                        // Show a message and update the UI
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        // The SMS verification code has been sent to the provided phone number, we
                                        // now need to ask the user to enter the code and then construct a credential
                                        // by combining the code with a verification ID.
                                        Log.d(TAG, "onCodeSent:" + verificationId);

                                        // Save verification ID and resending token so we can use them later
                                        mVerificationId = verificationId;
                                        mResendToken = token;
                                    }
                                }
                        )          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Otpreciever.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(),"SignIn code error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



}