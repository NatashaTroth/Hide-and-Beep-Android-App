package com.example.tranguyen.gameappproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterAuthKeyActivity extends AppCompatActivity {

    public interface VolleyCallbackObject {
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallbackArray {
        void onSuccess(JSONArray result);
    }

    //TODO: Errorhandling

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
                intent.putExtra("sourceClass", EnterAuthKeyActivity.class);
                startActivity(intent);
            }
        });

        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editTextKey);
                String inputAuthKey = editText.getText().toString();

                //TODO: REMOVE - JUST FOR TESTING
                inputAuthKey = "nF66bqTp6E";

                loadHuntAndGoToInstructions(inputAuthKey);
            }
        });
    }


    public void loadHuntAndGoToInstructions(String authKey){
        final Context c = this;
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hunt.json?auth_key=" + authKey;
        VolleyRequests.fetchJsonObject(url, authKey, c, new VolleyCallbackObject(){
            @Override
            public void onSuccess(JSONObject responseHunt){
                try {
                    final Hunt hunt = convertHuntJsonToJava(responseHunt);
                    if(hunt == null)
                        throw new Exception("Hunt could not be converted into Java.");

                    String hintsUrl = "https://hide-and-beep.projects.multimediatechnology.at/hints.json?hunt_id=" + hunt.getId();
                    VolleyRequests.fetchJsonArray(hintsUrl, c, new VolleyCallbackArray(){
                        @Override
                        public void onSuccess(JSONArray responseArray){
                            try {
                                final Hint[] hints = convertHintsJsonToJava(responseArray);
                                if(hints == null)
                                    throw new Exception("Hints could not be converted into Java.");

                                goToInstructions(hunt, hints);

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();

                    //It will only come here if unsuccessful
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Hunt could not be loaded.",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 50);
                    toast.show();
                    return;
                }
            }
        });
    }



    private Hunt convertHuntJsonToJava(JSONObject responseHunt){

        try {
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

            }
            catch (ParseException e) {
                e.printStackTrace();
                throw new Exception("Unable to parse the dates.");
            }
            DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

            timeLimit  = timeFormat.parse(timeFormat.format(timeLimit));
           Long timeLimitMillis = timeLimit.getTime();
            return new Hunt(id, name, startDate, expiryDate, timeLimitMillis, noTimeLimit, winningCode);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Hint[] convertHintsJsonToJava(JSONArray responseArray){
        Hint[] hints = new Hint[responseArray.length()];
        try{
            for(int i = 0; i < responseArray.length(); i++) {
                // Get current json object
                JSONObject responseHint = responseArray.getJSONObject(i);
                int id = responseHint.getInt("id");
                double latitude = responseHint.getDouble("latitude");
                double longitude = responseHint.getDouble("longitude");
                String text = responseHint.getString("text");
                hints[i] = new Hint(id, latitude, longitude, text);

            }
            return hints;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void goToInstructions(Hunt hunt, Hint[] hints){
        String toastText = "Hunt loaded!";
        Toast toast = Toast.makeText(EnterAuthKeyActivity.this,
                toastText,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();

        Intent intent = new Intent(EnterAuthKeyActivity.this, InstructionsActivity.class);
        intent.putExtra("hunt",(Serializable) hunt);
        intent.putExtra("hints",(Serializable) hints);
        startActivity(intent);
    }
}
