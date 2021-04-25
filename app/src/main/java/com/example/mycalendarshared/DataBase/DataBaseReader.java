package com.example.mycalendarshared.DataBase;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycalendarshared.Object.EventData;
import com.example.mycalendarshared.Object.User;
import com.example.mycalendarshared.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataBaseReader {


    private static DataBaseReader instance;
    private    User myUser;
    private   ArrayList<User> AllUsers =new ArrayList<>();
    public   String urlImg;


//=============================

    public static void initDb() {
        if (instance == null) {
            instance = new DataBaseReader();
        }
    }

    public static DataBaseReader getInstance() {
        return instance;
    }

    public DatabaseReference userRef(String userId) {
        return FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    public void setEventList(ArrayList <EventData> newEvantData) {
        userRef(App.getLoggedUser().getUid()).child("eventsArrayList").setValue(newEvantData);
    }

    public void removeUser() {
        userRef(App.getLoggedUser().getUid()).removeValue();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

    public void saveUser(User user) {
        userRef(user.getUserId()).setValue(user);
    }

    public void readUsersData() {
        FirebaseDatabase.getInstance().getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AllUsers.clear();
                        for(DataSnapshot userDataSnap :dataSnapshot.getChildren())
                        {
                            User user=new User();
                            user.setUserId(userDataSnap.child("userId").getValue(String.class));
                            user.setBirthday(userDataSnap.child("birthday").getValue(Date.class));
                            user.setName(userDataSnap.child("name").getValue(String.class));
                            user.setProfilePic_filePath(userDataSnap.child("profilePicFilePath").getValue(String.class));
                            Long sizeOfArrayList= userDataSnap.child("eventsArrayList").getChildrenCount();
                            for (int i=0;i<sizeOfArrayList;i++) {
                                EventData event=new EventData();
                                event.setLocation(userDataSnap.child("eventsArrayList").child(""+i).child("location").getValue(String.class));
                                event.setDate(userDataSnap.child("eventsArrayList").child(""+i).child("date").getValue(Date.class));
                                event.setEventContent(userDataSnap.child("eventsArrayList").child(""+i).child("eventContent").getValue(String.class));
                                event.setImageEvent(userDataSnap.child("eventsArrayList").child(""+i).child("imageEvent").getValue(String.class));
                                user.getEventsArrayList().add(event);
                            }
                            AllUsers.add(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }


    public void readUserDataAndAddToListsUsers() {
        FirebaseDatabase.getInstance().getReference("Users").child(App.getLoggedUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=new User();
                        user.setUserId(dataSnapshot.child("userId").getValue(String.class));
                        user.setBirthday(dataSnapshot.child("birthday").getValue(Date.class));
                        user.setName(dataSnapshot.child("name").getValue(String.class));
                        user.setProfilePic_filePath(dataSnapshot.child("profilePicFilePath").getValue(String.class));
                        Long sizeOfArrayList= dataSnapshot.child("eventsArrayList").getChildrenCount();
                        for (int i=0;i<sizeOfArrayList;i++) {
                            EventData event=new EventData();
                            event.setLocation(dataSnapshot.child("eventsArrayList").child(""+i).child("location").getValue(String.class));
                            event.setDate(dataSnapshot.child("eventsArrayList").child(""+i).child("date").getValue(Date.class));
                            event.setEventContent(dataSnapshot.child("eventsArrayList").child(""+i).child("eventContent").getValue(String.class));
                            event.setImageEvent(dataSnapshot.child("eventsArrayList").child(""+i).child("imageEvent").getValue(String.class));
                            user.getEventsArrayList().add(event);
                        }
                        AllUsers.add(user);
                        myUser=user;
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
    }


    public  void updateUser(String IdInTheServer) {
        for (int i = 0; i< AllUsers.size(); i++)
        {
            if(AllUsers.get(i).getUserId().equals(IdInTheServer))
            {
                myUser= AllUsers.get(i);
            }
        }

    }

    public  User getUser() {
        return myUser;
    }

    public  void setUser(User user) {
        myUser=user;
    }



    public  ArrayList<User> getUsersList() {
        return AllUsers;

    }




    public static   void readProfilePic(CircleImageView imageView, String filePath){

        Picasso.with(App.getAppContext()).load(filePath).placeholder(R.drawable.ic_person_black_24dp).into(imageView);

    }

    public void uploadImage(String folder,Uri filePathUri) {
        if (filePathUri != null) {
            StorageReference ref = DataBaseReader.getStorageRef().child(folder).child(filePathUri.getLastPathSegment());
            ref.putFile(filePathUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        urlImg = uri.getResult().toString();
                        Toast.makeText(App.getAppContext(), urlImg, Toast.LENGTH_SHORT);
                    })
                    .addOnFailureListener(e -> App.log("Upload failed"));
        }
    }



    public  String getUrlImg() {
        return urlImg;
    }

    public  void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }



    public static StorageReference getStorageRef(){ return FirebaseStorage.getInstance().getReference(); }
    public static DatabaseReference getLoggedUserRef(){ return getUsersRef().child(App.getLoggedUser().getUid()); }
    public static DatabaseReference getUsersRef(){ return getDBref(KEYS.USERS_REF); }
    public static DatabaseReference getDBref(String ref){ return FirebaseDatabase.getInstance().getReference(ref); }
















}
