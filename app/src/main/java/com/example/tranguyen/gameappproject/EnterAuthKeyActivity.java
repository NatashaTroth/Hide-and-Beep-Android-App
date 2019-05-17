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
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnterAuthKeyActivity extends AppCompatActivity {

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
                inputAuthKey = "hLX8E0uLEG";

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

        fetchHunt(authKey, new VolleyCallbackObject(){
            @Override
            public void onSuccess(JSONObject responseHunt){
                try {
                    final Hunt hunt = convertHuntJsonToJava(responseHunt);
                    fetchHints(hunt, new VolleyCallbackArray(){
                        @Override
                        public void onSuccess(JSONArray responseArray){
                            try {
                                final Hint[] hints = convertHintsJsonToJava(hunt, responseArray);
                                goToInstructions(hunt, hints);

                            }
//                            catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    });
                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
                catch (Exception e) {
                    e.printStackTrace();
                    // throw new Exception("Unable to get Hunt.");
                    return;
                }
            }
        });
    }

    public interface VolleyCallbackObject {
        void onSuccess(JSONObject result);
    }

    public interface VolleyCallbackArray {
        void onSuccess(JSONArray result);
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

            return new Hunt(id, name, startDate, expiryDate, timeLimit, noTimeLimit, winningCode);

        }
        catch (JSONException e) {
            e.printStackTrace();

        }
        catch (Exception e) {
            e.printStackTrace();
            // throw new Exception("Unable to get Hunt.");
            //return null;
        }
        return null;

    }

    private Hint[] convertHintsJsonToJava(Hunt hunt, JSONArray responseArray){
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
            // throw new Exception("Unable to get Hunt.");
            //return null;
        }

        return null;

    }



    private void fetchHunt(String authKey, final VolleyCallbackObject callback){
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hunt.json?auth_key=" + authKey;

        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    // Hunt hunt;
                    @Override
                    public void onResponse(JSONObject responseHunt) {
                        callback.onSuccess(responseHunt);
                        Log.v("VolleyResponse","Response: " + responseHunt.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast toast =  Toast.makeText(getApplicationContext(), "Something went wrong. Try again later!", Toast.LENGTH_SHORT);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            toast = Toast.makeText(getApplicationContext(), "Communication Error! Connect to internet!", Toast.LENGTH_SHORT);
                        } else if (error instanceof AuthFailureError) {
                            toast = Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ServerError) {
                            toast = Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof NetworkError) {
                            toast = Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ParseError) {
                            toast = Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT);
                        }

                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        return;

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }




    private void fetchHints(final Hunt hunt, final VolleyCallbackArray callback){
        String url = "https://hide-and-beep.projects.multimediatechnology.at/hints.json?hunt_id=" + hunt.getId();

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    // Hunt hunt;
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        callback.onSuccess(responseArray);
                        Log.v("VolleyResponse","Response: " + responseArray.toString());

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast toast =  Toast.makeText(getApplicationContext(), "Something went wrong. Try again later!", Toast.LENGTH_SHORT);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            toast = Toast.makeText(getApplicationContext(), "Communication Error! Connect to internet!", Toast.LENGTH_SHORT);
                        } else if (error instanceof AuthFailureError) {
                            toast = Toast.makeText(getApplicationContext(), "Authentication Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ServerError) {
                            toast = Toast.makeText(getApplicationContext(), "Server Side Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof NetworkError) {
                            toast = Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ParseError) {
                            toast = Toast.makeText(getApplicationContext(), "Parse Error!", Toast.LENGTH_SHORT);
                        }

                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        return;

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void goToInstructions(Hunt hunt, Hint[] hints){
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
