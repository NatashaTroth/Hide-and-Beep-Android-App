package com.example.tranguyen.gameappproject;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
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
import org.json.JSONObject;

public class VolleyRequests {

//    Context c;

    public VolleyRequests(){
        // this.c = c;
    }


    public static void fetchJsonObject(String url, String authKey, final Context c, final EnterAuthKeyActivity.VolleyCallbackObject callback){

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
                        Toast toast =  Toast.makeText(c, "Something went wrong. Try again later!", Toast.LENGTH_SHORT);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            toast = Toast.makeText(c, "Communication Error! Connect to internet!", Toast.LENGTH_SHORT);
                        } else if (error instanceof AuthFailureError) {
                            toast = Toast.makeText(c, "Authentication Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ServerError) {
                            toast = Toast.makeText(c, "Server Side Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof NetworkError) {
                            toast = Toast.makeText(c, "Network Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ParseError) {
                            toast = Toast.makeText(c, "Parse Error!", Toast.LENGTH_SHORT);
                        }

                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        return;

                    }
                });

        MySingleton.getInstance(c).addToRequestQueue(request);
    }




    public static void fetchJsonArray(String url, final Context c, final EnterAuthKeyActivity.VolleyCallbackArray callback){
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
                        Toast toast =  Toast.makeText(c, "Something went wrong. Try again later!", Toast.LENGTH_SHORT);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            toast = Toast.makeText(c, "Communication Error! Connect to internet!", Toast.LENGTH_SHORT);
                        } else if (error instanceof AuthFailureError) {
                            toast = Toast.makeText(c, "Authentication Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ServerError) {
                            toast = Toast.makeText(c, "Server Side Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof NetworkError) {
                            toast = Toast.makeText(c, "Network Error!", Toast.LENGTH_SHORT);
                        } else if (error instanceof ParseError) {
                            toast = Toast.makeText(c, "Parse Error!", Toast.LENGTH_SHORT);
                        }

                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        return;

                    }
                });

        MySingleton.getInstance(c).addToRequestQueue(request);
    }
}
