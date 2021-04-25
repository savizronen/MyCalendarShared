package com.example.mycalendarshared.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.activities.LoginActivity;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfileFragment extends Fragment {
    private AppCompatButton disconnect;
    private AppCompatButton deleteUser;
    private  View view;
    private CircleImageView profileImage;
    private User user;


    public UserProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fregment_profile, container, false);
        findViews();
        user=DataBaseReader.getInstance().getUser();
        DataBaseReader.getInstance().readProfilePic(profileImage,user.getProfilePicFilePath());
        setListeners();
        return view;
    }


    private void  signOutUser() {
    FirebaseAuth.getInstance().signOut();
    DataBaseReader.getInstance().setUser(null);
    Intent intent=new Intent(getContext(), LoginActivity.class);
    startActivity(intent);
    getActivity().finish();
}


    private  void setListeners()
    {
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseReader.getInstance().removeUser();
                DataBaseReader.getInstance().setUser(null);
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();
            }
        });

    }

    public void findViews(){
        disconnect =view.findViewById(R.id.profile_BTN_disconnect);
        deleteUser=view.findViewById(R.id.profile_BTN_deleteUser);
        profileImage=view.findViewById(R.id.profile_IMG_profilePic);
    }





























}