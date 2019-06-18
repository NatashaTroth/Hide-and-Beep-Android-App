package com.example.tranguyen.gameappproject;

import android.content.Context;
import android.view.Gravity;

import com.android.volley.RequestQueue;

public class MyToast {

    private static MyToast instance;
    private static Context ctx;

    private MyToast(Context context) {
        ctx = context;
    }

    public static synchronized MyToast getInstance(Context context) {
        if (instance == null) {
            instance = new MyToast(context);
        }
        return instance;
    }

    public void createToast(String text){
        android.widget.Toast toast = android.widget.Toast.makeText(ctx,
                text,
                android.widget.Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();

    }


}
