package com.example.mycalendarshared.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.Object.UsersAdapter;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;

import java.util.ArrayList;


public class RecyclerviewUsersFregment extends Fragment {
    private View view;
    private ArrayList<User> mExampleList=DataBaseReader.getInstance().getUsersList();
    private RecyclerView mRecyclerView;
    private UsersAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<User> usersArray;

    public RecyclerviewUsersFregment() {
        // Required empty public constructor
    }

    public static RecyclerviewUsersFregment newInstance() {
        RecyclerviewUsersFregment fragment = new RecyclerviewUsersFregment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        usersArray= DataBaseReader.getInstance().getUsersList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.fragment_recyclerview_users, container, false);
        buildRecyclerView();
        EditText editText = view.findViewById(R.id.birthday_EDT_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return  view;
    }

    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User item : mExampleList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }


    public void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }
    public void changeItem(int position, String text) {
        mAdapter.notifyItemChanged(position);
    }

    public void buildRecyclerView() {
        mRecyclerView = view.findViewById(R.id.recycler_view_users);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new UsersAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
