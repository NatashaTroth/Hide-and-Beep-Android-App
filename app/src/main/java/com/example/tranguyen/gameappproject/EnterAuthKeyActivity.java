package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterAuthKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_auth_key);

        setOnclickEventListeners();


    }



    private void setOnclickEventListeners(){
        //Set OnClickEventListeners
        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterAuthKeyActivity.this, HomescreenActivity.class);
                startActivity(intent);
            }
        });

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterAuthKeyActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        //TODO: CHANGE THIS BUTTON NAME
        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHunt("ujyYamORdh");

//                Intent intent = new Intent(EnterAuthKeyActivity.this, InstructionsActivity.class);
//                startActivity(intent);

            }
        });

    }

    public Hunt getHunt(String authKey){

        final TextView textView = (TextView) findViewById(R.id.instuctionsTextView);
        textView.setText("In get hunt");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hunt.json?auth_key=" + authKey;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("VolleyResponse","Response: " + response.toString());
                        textView.setText("successful " + response);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        textView.setText("failed " + error);


                    }
                });


        //Log.v("RESULTCOUNT", Integer.toString(resultCount));

        // Add the request to the RequestQueue.
            queue.add(request);









        //maybe double check auth key is correct

        String startDateStr = "2016/10/29 00:00:00";
        String expiryDateStr = "2016/11/12 23:59:59";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date startDate = null;
        Date expiryDate = null;

        try {
            startDate = sdf.parse(startDateStr);
            expiryDate = sdf.parse(expiryDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long millis = date.getTime();



        return new Hunt(1, "My Test Hunt", startDate, expiryDate, 55.6, false, "myWinnningcode" );


    }
}
