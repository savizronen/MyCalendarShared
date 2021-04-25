package com.example.mycalendarshared.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycalendarshared.DataBase.App;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.R;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText PhoneNumber;
    Button sendSmsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if(App.getLoggedUser() != null ){
            DataBaseReader.getInstance().updateUser(FirebaseAuth.getInstance().getUid());
            Intent intent=new Intent(this, ActivitySplash.class);
            startActivity(intent);
           finish();
           }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataBaseReader.getInstance().readUsersData();
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);
        setViews();
        setListeners();



    }

    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);
    }

    public void setViews(){
        PhoneNumber=(EditText)findViewById(R.id.login_EDT_phone);
        ccp=(CountryCodePicker)findViewById(R.id.country_code_picker);
        ccp.registerCarrierNumberEditText(PhoneNumber);
        sendSmsCode=(Button)findViewById(R.id.login_BTN_login);
    }

    public void setListeners(){
        sendSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, verifyAndSignInActivity.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);

            }
        });
    }
}

