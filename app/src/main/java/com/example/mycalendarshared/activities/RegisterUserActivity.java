package com.example.mycalendarshared.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycalendarshared.DataBase.App;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class RegisterUserActivity extends AppCompatActivity  {
    private ImageView user_profile_pic;
    private EditText fullName;
    private DatePicker birthdayDate;
    private Button SignUp;
    private User user;
    private  Uri fileUri;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);
        findViews();
        setListeners();


    }

    private void setListeners() {
        user_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.getImage(RegisterUserActivity.this);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user=new User(FirebaseAuth.getInstance().getUid(),fullName.getText().toString(),DataBaseReader.getInstance().getUrlImg(),new Date(birthdayDate.getYear()-1900, birthdayDate.getMonth(), birthdayDate.getDayOfMonth()));
                DataBaseReader.getInstance().saveUser(user);
                DataBaseReader.getInstance().readUserDataAndAddToListsUsers();
                Intent intent=new Intent(RegisterUserActivity.this, ActivitySplash.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            fileUri = data.getData();
            DataBaseReader.getInstance().uploadImage("images/",fileUri);
            user_profile_pic.setImageURI(fileUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            App.toast(new ImagePicker().Companion.getError(data));
        } else {
            App.toast("Task Cancelled");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);

    }

    public void findViews(){
        user_profile_pic=findViewById(R.id.register_IMG_profilePic);
        fullName=findViewById(R.id.register_EDT_name);
        birthdayDate =findViewById(R.id.BirthDayPicker);
        SignUp=findViewById(R.id.register_BTN_signUp);
    }




}
