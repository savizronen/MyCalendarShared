package com.example.mycalendarshared.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.Object.EventData;
import com.example.mycalendarshared.Object.EventsAdapter;
import com.example.mycalendarshared.R;

import java.util.ArrayList;

public class CloseEventFragment extends Fragment {
    private  View view;
    private RecyclerView recyclerView;
    private  EventsAdapter mAdapter;
    private  ArrayList<EventData> EventData;



    public CloseEventFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.eventlist_fragment, container, false);
        setEventFragment();
        return view;
    }


    private void setEventFragment() {
        EventData= DataBaseReader.getInstance().getUser().getEventsArrayList();
        recyclerView =view.findViewById(R.id.event_LST_names);
        recyclerView.setAdapter(new EventsAdapter(EventData));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}



