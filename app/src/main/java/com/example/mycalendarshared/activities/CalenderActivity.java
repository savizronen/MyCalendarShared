package com.example.mycalendarshared.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mycalendarshared.CallBacks.OnEventCreated;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.Object.EventData;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class CalenderActivity extends AppCompatActivity implements OnEventCreated {
    private ImageView mainButton;
    private DrawerLayout drawerLayoutCalender;
    private  NavigationView navigationView;
    private CircleImageView userImage ;
    private TextView userName;
    private TextView dateOfBirth;
    private  TextView mainLblTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        DataBaseReader.getInstance().updateUser(FirebaseAuth.getInstance().getUid());
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);
        mainLblTitle=findViewById(R.id.main_lbl_title);
        mainButton = findViewById(R.id.main_drawer_btn);
        drawerLayoutCalender=findViewById(R.id.drawerLayoutCalender);
        navigationView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHost_fragment);
        NavController navController =navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView,navController);
        userImage= navigationView.getHeaderView(0).findViewById(R.id.nav_header_circleImageView);
        userName= navigationView.getHeaderView(0).findViewById(R.id.nav_TXT_name);
        dateOfBirth =navigationView.getHeaderView(0).findViewById(R.id.nav_TXT_dateOfBirth);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> mainLblTitle.setText(destination.getLabel()));

        User user=  DataBaseReader.getInstance().getUser();
        if(user!=null) {
            userName.setText(user.getName());
            Date date=user.getBirthday();
            dateOfBirth.setText((date.getDate())+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900));
            DataBaseReader.getInstance().readProfilePic(userImage,user.getProfilePicFilePath());
        }
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutCalender.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void AddEventToUser(EventData event) {
        event.setImageEvent(DataBaseReader.getInstance().getUrlImg());
        User user= DataBaseReader.getInstance().getUser();
        user.getEventsArrayList().add(event);
        DataBaseReader.getInstance().saveUser(user);
    }


    @Override
    public void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
        Utils.hideSystemUI(decorView);

    }

}