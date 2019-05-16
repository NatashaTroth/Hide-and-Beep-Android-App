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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
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

                try {
                    getHuntAndGotToInstructions("ujyYamORdh");
                }
                catch(Exception e){
                    //todo: tell user wrong auth code
                }

//                Intent intent = new Intent(EnterAuthKeyActivity.this, InstructionsActivity.class);
//                startActivity(intent);

            }
        });

    }

    public void getHuntAndGotToInstructions(String authKey){

        //final Hunt hunt;

        final TextView textView = (TextView) findViewById(R.id.instuctionsTextView);
        textView.setText("In get hunt");

        // Instantiate the RequestQueue.
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hunt.json?auth_key=" + authKey;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseHunt) {
                        Log.v("VolleyResponse","Response: " + responseHunt.toString());
                        try {
                            int id = responseHunt.getInt("id");
                            String name = responseHunt.getString("name");
                            String startDateStr = responseHunt.getString("start_date");
                            String expiryDateStr = responseHunt.getString("expiry_date");
                            String timeLimitStr = responseHunt.getString("set_time_limit");
                            Boolean noTimeLimit = responseHunt.getBoolean("no_time_limit");
                            String winningCode = responseHunt.getString("winning_code");
                            textView.setText("successful " + expiryDateStr + "   " + timeLimitStr);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                            Date startDate = null;
                            Date expiryDate = null;
                            Date timeLimitDate = null;

                            try {
                                startDate = simpleDateFormat.parse(startDateStr);
                                expiryDate = simpleDateFormat.parse(expiryDateStr);
                                timeLimitDate = simpleTimeFormat.parse(timeLimitStr);
                                //timeLimitStr = new SimpleDateFormat("HH:mm").format(timeLimitDate);   //To make a string with hh:mm
                                //textView.setText("\n\n\nTime: " + timeLimitStr);



                            } catch (ParseException e) {
                                e.printStackTrace();
                               // textView.setText("ERROR\n\nDATES: " + e);
                                throw new Exception("Unable to parse the dates.");

                            }

                            Hunt hunt = new Hunt(id, name, startDate, expiryDate, timeLimitDate, noTimeLimit, winningCode);
                            Intent intent = new Intent(EnterAuthKeyActivity.this, InstructionsActivity.class);
                            intent.putExtra("hunt", (Serializable) hunt);
                            startActivity(intent);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                           // throw new Exception("Unable to get Hunt.");
                            return;

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error
                    textView.setText("failed " + error);
                    return;

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(request);

    }
}
