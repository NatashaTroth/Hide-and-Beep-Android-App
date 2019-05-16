package com.example.tranguyen.gameappproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
                EditText editText = (EditText) findViewById(R.id.editTextKey);
                String inputAuthKey = editText.getText().toString();

                try {
                    loadHuntAndGoToInstructions(inputAuthKey);
                }
                catch(Exception e){
                    //todo: tell user wrong auth code
                }

            }
        });

    }

    public void loadHuntAndGoToInstructions(String authKey){
//        final TextView textView = (TextView) findViewById(R.id.instuctionsTextView);
//        textView.setText("\n\n\nInLoadHuntAdn...");

        String url = "https://hide-and-beep.projects.multimediatechnology.at/hunt.json?auth_key=" + authKey;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                   // Hunt hunt;
                    @Override
                    public void onResponse(JSONObject responseHunt) {
                        Log.v("VolleyResponse","Response: " + responseHunt.toString());
                        try {
                            //textView.setText("got resp: " + responseHunt);
                            int id = responseHunt.getInt("id");
                            String name = responseHunt.getString("name");
                            String startDateStr = responseHunt.getString("start_date");
                            String expiryDateStr = responseHunt.getString("expiry_date");
                            String timeLimitStr = responseHunt.getString("set_time_limit");
                            Boolean noTimeLimit = responseHunt.getBoolean("no_time_limit");
                            String winningCode = responseHunt.getString("winning_code");

                            //getDates
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                            Date startDate = null;
                            Date expiryDate = null;
                            Date timeLimit = null;

                            try {
                                startDate = simpleDateFormat.parse(startDateStr);
                                expiryDate = simpleDateFormat.parse(expiryDateStr);
                                timeLimit = simpleTimeFormat.parse(timeLimitStr);
                                //timeLimitStr = new SimpleDateFormat("HH:mm").format(timeLimit);   //To make a string with hh:mm
                               // textView.setText("\n\n\nTime: " + timeLimitStr);

                            } catch (ParseException e) {
                                e.printStackTrace();
                                //textView.setText("ERROR\n\nDATES: " + e);
                                throw new Exception("Unable to parse the dates.");
                            }

                            Hunt hunt = new Hunt(id, name, startDate, expiryDate, timeLimit, noTimeLimit, winningCode);

                            fetchHints(hunt);


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
                   //textView.setText("failed " + error);
                    return;

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchHints(final Hunt hunt){
//        final TextView textView2 = (TextView) findViewById(R.id.instuctionsTextView);
//        textView2.setText("\n\n\nIn fetch hints...");
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hints.json?hunt_id=" + hunt.getId();

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    // Hunt hunt;
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        Log.v("VolleyResponse","Response: " + responseArray.toString());
                       // textView2.setText("\n\nSuccess Hints " + responseArray.length());
                        Hint[] hints = new Hint[responseArray.length()];

                        try {
                            for(int i = 0; i < responseArray.length(); i++) {

                                //textView2.append("Hint text: " + i);
                                // Get current json object
                                JSONObject responseHint = responseArray.getJSONObject(i);

                                int id = responseHint.getInt("id");

//                                //TODO: CHANGE AFTER REFACTORING RAILS - DO WE EVEN NEED POSITION - order in rails???
//                                int position;
//                                if(responseHint.has("position"))
//                                    responseHint.getInt("position");
//                                else
//                                    position = i;

                                //textView2.append("Hint text: " + i);
                                double latitude = responseHint.getDouble("latitude");
                                double longitude = responseHint.getDouble("longitude");
                                String text = responseHint.getString("text");

                                hints[i] = new Hint(id, latitude, longitude, text);
                                //textView2.append("END: " + i);

                            }

                           // textView2.setText("failed Hints " + hints[1]);

                            goToInstructions(hunt, hints);


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
                        //textView2.setText("failed Hints " + error);
                        return;

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void goToInstructions(Hunt hunt, Hint[] hints){
//        final TextView textView3 = (TextView) findViewById(R.id.instuctionsTextView);
//        textView3.setText("in goto instructions ");

        String toastText = "Hunt loaded!";
        Toast toast = Toast.makeText(getApplicationContext(),
                toastText,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();


        Intent intent = new Intent(EnterAuthKeyActivity.this, InstructionsActivity.class);
        intent.putExtra("hunt",(Serializable) hunt);
        intent.putExtra("hints",(Serializable) hints);
        startActivity(intent);


    }
}
