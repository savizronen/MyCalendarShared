package com.example.mycalendarshared.Object;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class EventData {

    private Date date;
    private  String eventContent;
    private String imageEvent;
    private String location;
    private ArrayList<String> UsersInEventList;

    public EventData() {

        UsersInEventList = new ArrayList<String>();
        this.imageEvent ="";

    }

    public EventData(int day,int month,int year, String eventContent) {
        this.imageEvent ="";
        this.eventContent = eventContent;
        UsersInEventList = new ArrayList<String>();

    }


    public EventData(Date date, String eventContent) {
        this.date=date;
        this.eventContent = eventContent;
        UsersInEventList = new ArrayList<String>();

    }

    public String getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(String imageEvent) {
        this.imageEvent = imageEvent;
    }
    public Date getDate() {
        return date;
    }

    public ArrayList<String> getUsersInEventList() {
        return UsersInEventList;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public boolean checkIfUserExist(String userId){
   for(int i=0;i<UsersInEventList.size();i++){
       if(UsersInEventList.get(i).equals(userId))
           return true;
   }
   return  false;
    }

    public void setEventName(String eventName) {
        eventName = eventName;
    }

    public void setUsersInEventList(ArrayList<String> usersInEventList) {
        UsersInEventList = usersInEventList;
    }


    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public  void SetHourAndMinutes(int hours,int minutes)
    {
    if(date!=null) {
        this.date.setHours(hours);
        this.date.setMinutes(minutes);
    }
    }

    public boolean equals( EventData eventData) {
 if((date.equals(eventData.getDate())&&(eventContent.equals(eventData.getEventContent()))&&(location.equals(eventData.getLocation()))))
  return true;
    return false;

    }
}
