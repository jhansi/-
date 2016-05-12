package com.weatherunderground.jhansi.weatherunderground;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Listener {


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final String TAG = MainActivity.class.getSimpleName();

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    String urlTemplate = "http://api.wunderground.com/api/136b9059ad931d9a/conditions/q/";

    TextView tempText, unitText, locationText;

    ImageView weatherImage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        setContentView(R.layout.activity_main);


        // Display wait dialog till we get Location and weather details
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();

        tempText = (TextView) findViewById(R.id.temp);
        unitText = (TextView) findViewById(R.id.tempUnit);
        locationText = (TextView) findViewById(R.id.location);

        weatherImage = (ImageView) findViewById(R.id.imgWeather);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Location services connected.");

        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
            }
        } else {
            LocationManager mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

        if (location == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            progressDialog.dismiss();
        } else {

            String address = getCurrentLocationString(location);
            //getWeatherdetails();

            new GetWeatherAsyncTask(this).execute(urlTemplate + address);

        }


    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());

        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    private String getCurrentLocationString(Location location) {

        double LATITUDE = location.getLatitude();
        double LONGITUDE = location.getLongitude();

        String city = "";
        String state = "";
        StringBuilder strReturnedAddress = new StringBuilder("");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                if (addresses.size() > 0) {
                    city = addresses.get(0).getLocality();

                    state = StateNameAbbreviator.getStateAbbreviation(returnedAddress);

                }

                strReturnedAddress.append(state + CommonUtils.URL_SEPERATOR);
                strReturnedAddress.append(city);
                strReturnedAddress.append(CommonUtils.JSON);

            } else {
                Log.w(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }
        return strReturnedAddress.toString();
    }

    @Override
    public void onReceived(WeatherResult weatherResult) {

        updateUI(weatherResult);
        progressDialog.dismiss();
    }


    public void updateUI(WeatherResult weatherResult) {
        tempText.setText(weatherResult.getTemp_f());
        unitText.setText(CommonUtils.FAHRENHEIT);
        locationText.setText(weatherResult.getFull_name());

        weatherImage.setImageDrawable(weatherResult.getDrawableWeather());

        updateProviderIcon(weatherResult.getDrawableProviderIcon());

    }

    public void updateProviderIcon(Drawable drawable) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(drawable);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

}
interface Listener {
    void onReceived(WeatherResult weatherResult);
}