package com.example.mycalendarshared.DataBase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.mycalendarshared.R;
import com.example.mycalendarshared.fragments.CalendarFragment;
import com.github.drjacky.imagepicker.ImagePicker;

import java.text.DateFormatSymbols;

public class Utils {

    public static void hideSystemUI( View decorView ) {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY       // Set the content to appear under the system bars so that the
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    public static void getImage(Activity activity) {
        ImagePicker.Companion
                .with(activity)
                .crop()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }


    public static void getImage(Fragment fragment) {
        ImagePicker.Companion
                .with(fragment)
                .crop()
                .cropOval()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }


    public static void getImage(Fragment fragment,int reqCode ) {
        ImagePicker.Companion
                .with(fragment)
                .crop()
                .cropOval()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(reqCode);
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public static void loadingDialog(Context context){
        ProgressDialog progressDialog;
        progressDialog=new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 30000);
    }










}
