package com.example.mycalendarshared.DataBase;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class App extends Application {
private  static Context context;
private static Toast myToast;

    public static void log(String enter_upload) {
        Log.d("MyTag", enter_upload);
    }

    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        myToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        DataBaseReader.initDb();

    }

    public static Context getAppContext()
    {
        return context;
    }
    public static FirebaseUser getLoggedUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    public static Toast toast(String msg){
        myToast     .setText(msg);
        myToast     .show();
        return      myToast;
    }
}
