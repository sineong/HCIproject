package com.sineong.testopenweatherapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.sineong.testopenweatherapi.Client.OpenWeatherAPIClient;
import com.sineong.testopenweatherapi.Client.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView latitudeField;
    private TextView longitudeField;
    public static double latitude;
    public static double longitude;

    private LocationManager locationManager;
    private String provider;
    Criteria criteria;
    private MyLocationListener mylistener;

    @SuppressLint("InlinedApi") @SuppressWarnings("deprecation")
    public static int getLocationMode(Context context) {
        int locationMode = Settings.Secure.LOCATION_MODE_OFF;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (TextUtils.isEmpty(locationProviders)){
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER)
                    && locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
            }
            else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }

        }

        return locationMode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        latitudeField = (TextView) findViewById(R.id.Latitude);
        longitudeField = (TextView) findViewById(R.id.Longitute);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setBearingRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setCostAllowed(true);


        provider = locationManager.getBestProvider(criteria, true);


        int mode = getLocationMode(this);

        switch (mode) {
            case android.provider.Settings.Secure.LOCATION_MODE_OFF:
                Toast.makeText(MainActivity.this, "LOCATION_MODE_OFF",
                        Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                Toast.makeText(MainActivity.this,
                        "LOCATION_MODE_SENSORS_ONLY = GPS_PROVIDER",
                        Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                Toast.makeText(MainActivity.this,
                        "LOCATION_MODE_BATTERY_SAVING = NETWORK_PROVIDER",
                        Toast.LENGTH_SHORT).show();
                break;
            case android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                Toast.makeText(MainActivity.this,
                        "LOCATION_MODE_HIGH_ACCURACY = GPS_PROVIDER+NETWORK_PROVIDER",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }



        Location location=null;
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }


        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Intent intent = new Intent(this, ShowWeatherInfo.class);
        intent.putExtra("LATITUDE", latitude);
        intent.putExtra("LONGITUDE", longitude);

        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        provider = locationManager.getBestProvider(criteria, true);

        try {
            locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            locationManager.removeUpdates(mylistener);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }

    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            latitudeField.setText("Latitude: "
                    + String.valueOf(location.getLatitude()));
            longitudeField.setText("Longitude: "
                    + String.valueOf(location.getLongitude()));


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}
