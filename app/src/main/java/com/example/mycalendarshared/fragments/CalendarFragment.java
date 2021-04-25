package com.example.mycalendarshared.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycalendarshared.CallBacks.OnEventCreated;
import com.example.mycalendarshared.CallBacks.OnEventDelete;
import com.example.mycalendarshared.DataBase.DataBaseReader;
import com.example.mycalendarshared.DataBase.Utils;
import com.example.mycalendarshared.Object.EventData;
import com.example.mycalendarshared.Object.EventsAdapter;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class CalendarFragment extends Fragment implements OnEventDelete  {

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private View view;
    private TimePicker timePicker;
    private EventData eventData;
    private OnEventCreated onEventCreated;
    private ImageView micEventText;
    private TextView currentMonth;
    private EditText eventContent;
    private EditText locationEvent;
    private ImageView micLocationEvent;



    private CircleImageView EventPicture;
    private LayoutInflater layoutInflater;
    private String Operation = "";
    private Button addEvent;
    private RecyclerView recycleViewEvents;
    private  EventsAdapter mAdapter;
    private  ArrayList<EventData> EventDataOnDate;
    private ArrayList<User> allUsers = new ArrayList<>();



    private void getSpeechInput() {
        Intent intent = new Intent((RecognizerIntent.ACTION_RECOGNIZE_SPEECH));
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this.getActivity(), "your Device downy Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 20:
                Uri fileUri = data.getData();
                DataBaseReader.getInstance().uploadImage("imagesEvents/",fileUri);
                EventPicture.setImageURI(fileUri);
            break;

            case 10:
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (Operation.equals("eventContent")) {
                        eventContent.setText(result.get(0));
                        Log.d("MyTag1", eventData.getEventContent() + " " + eventData.getDate().toString());
                    }
                    if (Operation.equals("LocationEvent")) {
                        locationEvent.setText(result.get(0));
                    }
                }
                break;
        }

    }

    public void clearAllEvents(){
        User user= DataBaseReader.getInstance().getUser();
        ArrayList<EventData> arrayListEventData=user.getEventsArrayList();
        for (int i=0;i<arrayListEventData.size();i++)
        {
            compactCalendarView.getEvents(arrayListEventData.get(i).getDate().getTime()).clear();

        }
    }


    public void showAllEvents()
    {
       User user= DataBaseReader.getInstance().getUser();
       ArrayList<EventData> arrayListEventData=user.getEventsArrayList();
       for (int i=0;i<arrayListEventData.size();i++)
       {
           compactCalendarView.addEvent(new Event(Color.GREEN, arrayListEventData.get(i).getDate().getTime()));
       }
    }


    public void openDialog(Date dateClicked) {
        View view = layoutInflater.inflate(R.layout.dialog, null);
        eventContent = view.findViewById(R.id.dialog_EDT_content);
        micEventText = view.findViewById(R.id.dialog_IMG_micName);
        locationEvent = view.findViewById(R.id.dialog_EDT_location);
        micLocationEvent = view.findViewById(R.id.dialog_IMG_micLocation);
        EventPicture = view.findViewById(R.id.dialog_IMG_uploadPic);
        timePicker=view.findViewById(R.id.dialog_timePicker);
        timePicker.setIs24HourView(true);
        ProgressDialog progressDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        EventPicture.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Utils.getImage(CalendarFragment.this,20);
        }
        });

        builder.setPositiveButton("Enter event", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventData.setEventContent(eventContent.getText().toString());
                eventData.setLocation(locationEvent.getText().toString());
                eventData.SetHourAndMinutes(timePicker.getHour(),timePicker.getMinute());
                onEventCreated.AddEventToUser(eventData);
                onDayClickShowEvents(eventData.getDate());
                eventData=null;
                eventData=new EventData();
                compactCalendarView.addEvent(new Event(Color.GREEN, dateClicked.getTime()));

            }
        });

        micLocationEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation = "LocationEvent";
                getSpeechInput();
            }
        });

        micEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Operation = "eventContent";
                getSpeechInput();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onEventCreated = (OnEventCreated) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        view = inflater.inflate(R.layout.fragment_calender, container, false);
        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        LocalDateTime now = LocalDateTime.now();
        eventData=new EventData();
        eventData.setDate(new Date(now.getYear()-1900,now.getMonthValue()-1,now.getDayOfMonth()));
        showAllEvents();
        buildRecyclerView();
        addEvent=view.findViewById(R.id.calendar_BTN_addEvent);
        compactCalendarView.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
        currentMonth=view.findViewById(R.id.calendar_TXT_month);
        currentMonth.setText(Utils.getMonth(compactCalendarView.getFirstDayOfCurrentMonth().getMonth()+1)+"  "+(compactCalendarView.getFirstDayOfCurrentMonth().getYear()+1900));
        setListeners();
        return view;
    }


   public void   onDayClickShowEvents(Date dateClicked)
  {
      EventDataOnDate= new ArrayList<EventData>();
      ArrayList<EventData> AllEventData=DataBaseReader.getInstance().getUser().getEventsArrayList();
      for (int i = 0; i <AllEventData.size(); i++) {
          if(dateClicked.getYear()==AllEventData.get(i).getDate().getYear())
          {
              if(dateClicked.getMonth()==AllEventData.get(i).getDate().getMonth())
              {
                  if(dateClicked.getDate()==AllEventData.get(i).getDate().getDate())
                  {
                      EventDataOnDate.add(AllEventData.get(i));
                  }

              }

          }

      }
      recycleViewEvents.setAdapter(new EventsAdapter(EventDataOnDate,this));

  }

    public void buildRecyclerView() {
        recycleViewEvents =view.findViewById(R.id.calendar_LST_eventList);
        mAdapter = new EventsAdapter(new ArrayList<>(),this);
        recycleViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewEvents.setAdapter(mAdapter);
    }



    public void setListeners(){
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(eventData.getDate());
            }
        });


        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                eventData.setDate(dateClicked);
                View decorView = getActivity().getWindow().getDecorView();
                Utils.hideSystemUI(decorView);
                //updateAllEvents();
                onDayClickShowEvents(dateClicked);

            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentMonth.setText(Utils.getMonth(compactCalendarView.getFirstDayOfCurrentMonth().getMonth()+1)+"  "+(compactCalendarView.getFirstDayOfCurrentMonth().getYear()+1900));
                View decorView = getActivity().getWindow().getDecorView();
                Utils.hideSystemUI(decorView);
            }
        });
    }

    @Override
    public void deleteEvent(Date date) {
        compactCalendarView.getEvents(date.getTime()).clear();
        clearAllEvents();
        showAllEvents();
    }
}
