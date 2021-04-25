package com.example.mycalendarshared.Object;

import android.text.format.DateFormat;

import com.example.mycalendarshared.R;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String profilePath;
    private  String userId;
    private String name;
    private Date birthday;
    private ArrayList<EventData> eventsArrayList=new ArrayList<>();
    private int checkBoxResource= R.drawable.ic_baseline_check_box_outline_blank_24;


    public User( ){

    }


    public User(String userId ,String name ,String profilepath,Date birthday){
        this.userId=userId;
        this.name=name;
        this.profilePath =profilepath;
        this.birthday=birthday;


    }
    public User(String userId ,String name,Date birthday){
        this.userId=userId;
        this.name=name;
        this.birthday=birthday;

    }



    public void markCheckBox(){ checkBoxResource= R.drawable.ic_baseline_check_box_24; }

    public void unMarkCheckBox(){checkBoxResource=R.drawable.ic_baseline_check_box_outline_blank_24;}
    public int getCheckBoxResource(){return  checkBoxResource;}

    public int getBirthdayPic() { return R.drawable.ic_baseline_star_24; }


   public boolean checkIfEventExist(EventData eventData){
    int numOfEvents=eventsArrayList.size();
    for(int i=0;i<numOfEvents;i++)
        if(eventsArrayList.get(i).equals(eventData))
            return  true;
        return  false;
    }

    public String getProfilePicFilePath() { return profilePath; }

    public void setProfilePic_filePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getBirthdayString(){
        String day          = (String) DateFormat.format("dd",   birthday); // 20
        String monthNumber  = (String) DateFormat.format("MM",   birthday); // 06
        String year         = (String) DateFormat.format("yyyy", birthday); // 2013
        return (day+"/"+monthNumber+"/"+year);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ArrayList<EventData> getEventsArrayList() {
        return eventsArrayList;
    }
    public void setEventsArrayList(ArrayList<EventData> eventsArrayList) {
        this.eventsArrayList = eventsArrayList;
    }


}
