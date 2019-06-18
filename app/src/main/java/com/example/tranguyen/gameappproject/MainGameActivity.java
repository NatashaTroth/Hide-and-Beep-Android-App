package com.example.tranguyen.gameappproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    private Overlays overlays;
    private TextView numOfAllHints;
    private MyToast myToast;

    //To make sure the beeping only plays once
    enum Color {
        YELLOW,
        ORANGE,
        RED
    }

    Color radarColor = Color.YELLOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        //Avoid keyboard opening on creation of activity
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        myToast = MyToast.getInstance(this);
        SCREEN_HEIGHT = getScreenHeight();
        getExtras();
        numOfAllHints = (TextView) findViewById(R.id.numberOfHintsLeft);
        numOfAllHints.setText(String.valueOf(totalHints));

        prepareOverlays();
        createCountdownClock(hunt);
        setHomeOnClickListeners(hunt, hints);

        // request the location of user and add the permissions
        requestPermissionsLocation();

        // build the google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

    }

    private int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void getExtras() {
        hunt = (Hunt) getIntent().getSerializableExtra("hunt");
        hints = (Hint[]) getIntent().getSerializableExtra("hints");
        currentHint = getIntent().getExtras().getInt("currentHint");
        totalHints = hints.length;
    }

    private void prepareOverlays() {
        overlays = new Overlays(this);
        overlays.prepareHintOverlay(hints, SCREEN_HEIGHT, currentHint);
        overlays.prepareHelpOverlay(SCREEN_HEIGHT);
        overlays.prepareEnterCodeOverlay(SCREEN_HEIGHT, hunt, MainGameActivity.this);
    }

    private void createCountdownClock(Hunt hunt) {
        final TextView timerTextView = (TextView) findViewById(R.id.gameTime);

        if (!hunt.getNoTimeLimit()) {
            //Create timer
            new CountDownTimer(hunt.getTimeLimit(), 1000) {

                public void onTick(long millisUntilFinished) {
                    String formattedTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                    timerTextView.setText(formattedTime);
                }

                public void onFinish() {
                    myToast.createToast("Time up. Game over!");
                    Intent intent = new Intent(MainGameActivity.this, LoseActivity.class);
                    startActivity(intent);
                }
            }.start();
        } else {
            timerTextView.setText(R.string.unlimited);
        }
    }


    private void setHomeOnClickListeners(final Hunt hunt, final Hint[] hints) {
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

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    private void requestPermissionsLocation() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
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
            myToast.createToast("You need to install Google Play Services to use the App properly.");
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
            } else {
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
        if (location != null && currentHint < hints.length) {
            // get distance between locations
            locationLat = location.getLatitude();
            locationLng = location.getLongitude();
            handleNewLocation(locationLat, locationLng);
        }
    }

    private void handleNewLocation(double lat, double lng) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        float distanceBetween = calcDistanceBetween(lat, lng);
        if (distanceBetween <= 80 && distanceBetween >= 36)
            handleOrangeRadar(vibrator);
        else if (distanceBetween <= 35 && distanceBetween >= 16)
            handleRedRadar(vibrator);
        else if (distanceBetween <= 15) {
            totalHints -= 1;
            currentHint += 1;
            //not on last hint
            if (totalHints > 0)
                switchHint(vibrator);
            else
                switchToEnterCode();
        } else
            handleYellowRadar(vibrator);

    }

    private float calcDistanceBetween(double lat, double lng) {
        Location hintLocation = new Location("");
        Location currentLocation = new Location("");
        hintLocation.setLatitude(hints[currentHint].getLatitude());
        hintLocation.setLongitude(hints[currentHint].getLongitude());
        currentLocation.setLatitude(lat);
        currentLocation.setLongitude(lng);
        return hintLocation.distanceTo(currentLocation);
    }

    private void handleOrangeRadar(Vibrator vibrator) {
        //only play beep when you first arrive at orange
        if (radarColor != Color.ORANGE) {
            MediaPlayer mediaPlayer = MediaPlayer.create(MainGameActivity.this, R.raw.beep_slower);
            mediaPlayer.start();
            radarColor = Color.ORANGE;
        }
        switchWarningAlarm(R.drawable.ic_orangealarm);
        vibrator.vibrate(500);
    }

    private void handleRedRadar(Vibrator vibrator) {
        //only play beep when you first arrive at orange
        if (radarColor != Color.RED) {
            MediaPlayer mediaPlayer = MediaPlayer.create(MainGameActivity.this, R.raw.beep_faster);
            mediaPlayer.start();
            radarColor = Color.RED;
        }
        switchWarningAlarm(R.drawable.ic_redalarm);
        vibrator.vibrate(500);
    }

    private void handleYellowRadar(Vibrator vibrator) {
        radarColor = Color.YELLOW;
        switchWarningAlarm(R.drawable.ic_yellowalarm);
        vibrator.vibrate(500);
    }

    private void switchWarningAlarm(int image) {
        ImageView alarmImageView = (ImageView) findViewById(R.id.imageView12);
        alarmImageView.setImageResource(image);
    }

    private void switchHint(Vibrator vibrator) {
        final LinearLayout hintOverlay = (LinearLayout) findViewById(R.id.hintOverlay);
        vibrator.vibrate(500);
        overlays.prepareHintOverlay(hints, SCREEN_HEIGHT, currentHint);
        numOfAllHints.setText(String.valueOf(totalHints));

        AlertDialog.Builder switchToNextHint = new AlertDialog.Builder(MainGameActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        switchToNextHint.
                setTitle("That was mäh-tastic!").
                setMessage("Are you ready for the next hint?").
                setCancelable(false).
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    //@Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hintOverlay.animate().translationY(0);
                    }
                }).show();
    }

    private void switchToEnterCode() {
        //Last hint was found
        numOfAllHints.setText(String.valueOf(0));
        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.enterCodeOverlay);
        final Button mainGameEnterCodeBtn = findViewById(R.id.mainGameEnterCodeBtn);
        final Button mainGameHintBtn = findViewById(R.id.mainGameHintBtn);
        overlays.openEnterCodeOverlay(constraintLayout, mainGameEnterCodeBtn, mainGameHintBtn);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
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
                } else {
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
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            myToast.createToast("You need to enable permissions to display your current location!");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }
}
