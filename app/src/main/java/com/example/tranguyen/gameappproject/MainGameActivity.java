package com.example.tranguyen.gameappproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;


public class MainGameActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // 5 seconds

    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsToRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    // number for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;

    // get gps difference
    private Hunt hunt;
    private Hint[] hints;
    private int currentHint;
    private int totalHints;
    private double locationLat;
    private double locationLng;
    private int SCREEN_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //Avoid keyboard opening on creation of activity
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_HEIGHT = displayMetrics.heightPixels;

        //Get extras
        hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        hints = (Hint[]) getIntent().getSerializableExtra("hints");
        currentHint = getIntent().getExtras().getInt("currentHint");
        totalHints = hints.length;

        TextView numOfAllHints = (TextView) findViewById(R.id.numberOfHintsLeft);
        numOfAllHints.setText(String.valueOf(totalHints));

         //---Prepare overlays---
         prepareHintOverlay(hunt, hints, SCREEN_HEIGHT, currentHint);
         prepareHelpOverlay(hunt, SCREEN_HEIGHT);
         prepareEnterCodeOverlay(hunt, SCREEN_HEIGHT);

         createCountdownClock(hunt);

         setHomeOnClickListeners(hunt, hints);

         // request the location of user and add the permissions
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // build the google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

    }

    private void prepareHintOverlay(Hunt hunt, Hint[] hints, final int screenHeight, int currentHintNo){
        final LinearLayout linearLayout  = (LinearLayout) findViewById(R.id.hintOverlay);

        Log.d("OVERLAY HINT ", String.valueOf(currentHint));

        TextView hintTextView = (TextView) findViewById(R.id.hintText);
        hintTextView.setText(hints[currentHintNo].getText());
        //hide Hint overlay

        linearLayout.setTranslationY(screenHeight);

        //Click on "Hint" Button
        Button mainGameHintBtn = findViewById(R.id.mainGameHintBtn);
        mainGameHintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(0);
            }
        });

        //Click on Tick Button
        ImageButton hintTickBtn = findViewById(R.id.hintTickButton);
        hintTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(screenHeight);
            }
        });

    }


    private void prepareHelpOverlay(Hunt hunt, final int screenHeight){

        final LinearLayout linearLayout  = (LinearLayout) findViewById(R.id.helpOverlay);
        linearLayout.setTranslationY((-1)*screenHeight);

        ImageView helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY(0);
            }
        });

        //Click on Tick Button
        ImageButton helpTickBtn = findViewById(R.id.helpTickButton);
        helpTickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.animate().translationY((-1)*screenHeight);
            }
        });

    }

    private void prepareEnterCodeOverlay(final Hunt hunt, final int screenHeight){
        final ConstraintLayout linearLayout  = (ConstraintLayout) findViewById(R.id.enterCodeOverlay);
        linearLayout.setTranslationY(screenHeight);

        //Click on "Enter Treasure Code" Button
        final Button mainGameEnterCodeBtn = findViewById(R.id.mainGameEnterCodeBtn);
        final Button mainGameHintBtn = findViewById(R.id.mainGameHintBtn);
        mainGameEnterCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setTranslationY(0);
                mainGameEnterCodeBtn.setVisibility(View.GONE);
                mainGameHintBtn.setVisibility(View.GONE);
            }
        });

        //Click on "Back" Button
        Button enterCodeBackBtn = findViewById(R.id.enterCodeBackBtn);
        enterCodeBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setTranslationY(screenHeight);
                mainGameEnterCodeBtn.setVisibility(View.VISIBLE);
                mainGameHintBtn.setVisibility(View.VISIBLE);
            }
        });

        //Click on "Submit button
        Button submitCodeBtn = findViewById(R.id.submitCodeBtn);
        submitCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.editTextEnterTreasureCode);
                String inputCode = editText.getText().toString();

                if(inputCode.equals(hunt.getWinningCode())){
                    Intent intent = new Intent(MainGameActivity.this, WinActivity.class);
                    startActivity(intent);
                }
                else {
                    hunt.enterCodeTries--;
                    if(hunt.enterCodeTries <= 0){
                        String toastText = "Wrong code. That was your last try. Game over!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                        Intent intent = new Intent(MainGameActivity.this, LoseActivity.class);
                        startActivity(intent);
                    }
                    else{
                        String tryCase = "tries";
                        if(hunt.enterCodeTries == 1)
                            tryCase = "try";
                        String toastText = "Wrong code. Only " + hunt.enterCodeTries + " " + tryCase + " left!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                toastText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 50);
                        toast.show();
                    }

                }
            }
        });

    }

    private void createCountdownClock(Hunt hunt){
        final TextView timerTextView = (TextView) findViewById(R.id.gameTime);

        if(!hunt.getNoTimeLimit()){
            //Create timer
            new CountDownTimer(hunt.getTimeLimit(), 1000) {

                public void onTick(long millisUntilFinished) {
                    String formattedTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    timerTextView.setText(formattedTime);
                }

                public void onFinish() {
                    String toastText = "Time up. Game over!";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            toastText,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 50);
                    toast.show();
                    Intent intent = new Intent(MainGameActivity.this, LoseActivity.class);
                    startActivity(intent);
                }
            }.start();
        }
        else{
            timerTextView.setText(R.string.unlimited);
        }

    }

    private void setHomeOnClickListeners(final Hunt hunt, final Hint[] hints){
        ImageView owlHomeBtn = findViewById(R.id.homeOwl);
        owlHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainGameActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                        .setTitle("Warning!")
                        .setMessage("Going to home will end this hunt. Do you still want to go to home?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent = new Intent(MainGameActivity.this, HomescreenActivity.class);
                                startActivity(intent);

                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You need to install Google Play Services to use the App properly.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 50);
            toast.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop the location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            }
            else {
                finish();
            }

            return false;
        }

        return true;
    }

    private ArrayList<String> permissionsRequest(ArrayList<String> requiredPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : requiredPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            // get distance between locations
            locationLat = location.getLatitude();
            locationLng = location.getLongitude();

            // Confirmation that GPS connection is still available
            String toastLocation = "GPS requested. Lat/Lng: " + location.getLatitude() + "/" + location.getLongitude();

            Toast gpsToast = Toast.makeText(MainGameActivity.this,
                    toastLocation,
                    Toast.LENGTH_LONG);
            gpsToast.setGravity(Gravity.BOTTOM, 0, 50);
            gpsToast.show();

            // Rufe die Methode so oft auf, wie die GPS abgefragt wird
            switchToNextHint(locationLat, locationLng);
        }
    }

    public void switchToNextHint(double lat, double lng) {
        final LinearLayout linearLayout  = (LinearLayout) findViewById(R.id.hintOverlay);
        Location hintLocation = new Location("");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Location currentLocation = new Location("");
        TextView numOfAllHints = (TextView) findViewById(R.id.numberOfHintsLeft);

        // calculate the distance between current gps location of user and hint location
        hintLocation.setLatitude(hints[currentHint].getLatitude());
        hintLocation.setLongitude(hints[currentHint].getLongitude());
        currentLocation.setLatitude(lat);
        currentLocation.setLongitude(lng);

        float distanceBetween = hintLocation.distanceTo(currentLocation);

        if (distanceBetween <= 100 && distanceBetween >= 46) {
            switchWarningToOrangeAlarm();
            v.vibrate(500);

        }
        if (distanceBetween <= 45 && distanceBetween >= 21) {
            switchWarningToRedAlarm();
            v.vibrate(500);
        }
        if (distanceBetween <= 20) { // 20m => found the hint
            totalHints -= 1;
            currentHint += 1;

            if (currentHint < hints.length) {
                v.vibrate(500);
                prepareHintOverlay(hunt, hints, SCREEN_HEIGHT, currentHint);
                numOfAllHints.setText(String.valueOf(totalHints)); // show the total hint in circle of game

                AlertDialog.Builder switchToNextHint = new AlertDialog.Builder(MainGameActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                switchToNextHint.
                        setTitle("That was mäh-tastic!").
                        setMessage("Are you ready for the next hint?").
                        setCancelable(false).
                        setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            //@Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                linearLayout.animate().translationY(0);
                            }
                        }).setNegativeButton("NO", null).show();

                if (currentHint == hints.length - 1) {
                    numOfAllHints.setText(String.valueOf(0));

                    Intent intent = new Intent(MainGameActivity.this, EnterCodeActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void switchWarningToOrangeAlarm() {
        ImageView orangeAlarm = (ImageView)findViewById(R.id.imageView12);
        orangeAlarm.setImageResource(R.drawable.ic_orangealarm);
    }

    public void switchWarningToRedAlarm() {
        ImageView redAlarm = (ImageView)findViewById(R.id.imageView12);
        redAlarm.setImageResource(R.drawable.ic_redalarm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsToRejected.add(perm);
                    }
                }

                if (permissionsToRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsToRejected.get(0))) {
                            new AlertDialog.Builder(MainGameActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsToRejected.toArray(new String[permissionsToRejected.size()]),
                                                        ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).
                                    setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                }
                else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // if permissions is ok, get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            //Log.d("CURRENT LOCATION: ", location.toString());

            String toastLocation = "GPS connected.";

            Toast toast = Toast.makeText(MainGameActivity.this,
                    toastLocation,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 50);
            toast.show();
        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display your current location!", Toast.LENGTH_LONG).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }
}
