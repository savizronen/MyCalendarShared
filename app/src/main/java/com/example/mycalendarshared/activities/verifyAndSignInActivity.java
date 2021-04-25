package com.example.mycalendarshared.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verifyAndSignInActivity extends AppCompatActivity
{
  private EditText verifiedCode;
  private  Button verifiedButton;
  private String phoneNumber;
  private String opId;
  private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__verifyand_signin);
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);
        phoneNumber =getIntent().getStringExtra("mobile").toString();
        verifiedCode=(EditText)findViewById(R.id.verify_EDT_code);
        verifiedButton=(Button)findViewById(R.id.verify_BTN_verify);
        mAuth=FirebaseAuth.getInstance();
        initAuthProvider();


        verifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifiedCode.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                else if(verifiedCode.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_LONG).show();
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(opId,verifiedCode.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                }

            }
        });
    }



    private void initAuthProvider()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {


                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        opId =s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }


    @Override
    public void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful())
                        {
                            DataBaseReader.getInstance().updateUser(FirebaseAuth.getInstance().getUid());
                            User user=DataBaseReader.getInstance().getUser();

                       if(FirebaseAuth.getInstance().getUid()!=null)
                       {
                           Intent intent;
                           if(user!=null) {

                                intent = new Intent(verifyAndSignInActivity.this, ActivitySplash.class);
                           }
                           else
                           {
                                intent = new Intent(verifyAndSignInActivity.this, RegisterUserActivity.class);
                           }

                           startActivity(intent);
                           finish();
                       }

                        } else {
                            Toast.makeText(getApplicationContext(),"Sign in Code Error",Toast.LENGTH_LONG).show();
                        } });
                    }
                }
