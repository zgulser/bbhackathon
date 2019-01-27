package com.android.buzzaway.securecards;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.buzzaway.securecards.data.CardClient;
import com.android.buzzaway.securecards.data.CardModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    public static final String ARG_CARD_ID = "hack.card.id";
    private float radius;
    private CardModel cardModel;

    private GoogleMap mMap;
    private SeekBar mSeekbar;
    private TextView mProgress;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1000;
    private static final float DEFAULT_ZOOM = 12;
    private static final float DEFAULT_RADIUS_IN_METERS = 2000;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location myLocation;

    public static void start(@NonNull Context context, @NonNull CardModel cardModel) {
        Bundle args = new Bundle();
        args.putLong(ARG_CARD_ID, cardModel.id);
        Intent startGeofencing = new Intent(context, MapsActivity.class);
        startGeofencing.putExtras(args);
        context.startActivity(startGeofencing);
        ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initSeekbar();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        populateCardModel();

        findViewById(R.id.next).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }

    private void populateCardModel() {
        long id = getIntent().getLongExtra(ARG_CARD_ID, -1);
        cardModel = CardClient.instance.findCardById(id);
        Objects.requireNonNull(cardModel);
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void initSeekbar() {
        mSeekbar = findViewById(R.id.seekBar);
        mProgress = findViewById(R.id.progress);
        mProgress.setVisibility(View.INVISIBLE);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setAlpha(1.0f);
                mProgress.setText(String.valueOf(progress));
                mProgress.animate().alpha(0f).setDuration(500).setListener(new Animator.AnimatorListener() {

                    long started = System.currentTimeMillis();

                    @Override
                    public void onAnimationStart(Animator animator) {
                        started = System.currentTimeMillis();
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (System.currentTimeMillis() - started >= 500L) {
                            radius = Float.parseFloat(mProgress.getText().toString());
                            drawCircle(radius * 100);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        });
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            doCameraStuff();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCameraStuff();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @SuppressLint("MissingPermission")
    private void doCameraStuff() {
        mMap.setMyLocationEnabled(true);

        // get your location
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLocation = location;
                            // animate there
                            LatLng whereAmI = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(whereAmI));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(whereAmI, DEFAULT_ZOOM));

                            // draw circle
                            drawCircle(DEFAULT_RADIUS_IN_METERS);
                        }
                    }
                });
    }

    private void drawCircle(final float radius) {
        if (myLocation != null) {
            mMap.clear();
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                    .radius(radius) // In meters
                    .strokeColor(Color.parseColor("#757575"))
                    .strokeWidth(4.0f)
                    .fillColor(Color.parseColor("#66ffd54f"));

            // Get back the mutable Circle
            mMap.addCircle(circleOptions);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next) {
            mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    //ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 50, bStream);

                    //byte[] byteArray = bStream.toByteArray();
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    Bitmap b = Bitmap.createScaledBitmap(bitmap, 300, 460, true);
                    b.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();
                    CardRestrictionActivity.start(MapsActivity.this, myLocation, radius, byteArray, cardModel);
                }
            });
        }
    }

    public static Bitmap takescreenshot(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createScaledBitmap(v.getDrawingCache(), 200, 430, true);
        v.setDrawingCacheEnabled(false);
        return b;
    }
}
