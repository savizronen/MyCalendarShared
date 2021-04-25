package com.example.mycalendarshared.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.Object.EventsAdapter;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.Object.UsersAdapter;
import com.example.mycalendarshared.R;

import java.util.ArrayList;


public class BirthdayListFregment extends Fragment {
    private RecyclerviewUsersFregment usersList;

    public BirthdayListFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_birthday_list, container, false);
        setSearchFragment();
        return view;
    }

    private void setSearchFragment() {
        usersList= RecyclerviewUsersFregment.newInstance();
        FragmentTransaction ft=getChildFragmentManager().beginTransaction();
        ft.add(R.id.birthday_FRG_userList,usersList);
        ft.commit();
    }



}