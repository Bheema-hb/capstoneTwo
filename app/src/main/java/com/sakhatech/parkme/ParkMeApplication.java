package com.sakhatech.parkme;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Bheema
 * Company Techjini
 */
public class ParkMeApplication extends Application {

    private static ParkMeApplication mInstance;
    private String mSessionId;
    private Gson mGson;
    public static final String TAG = ParkMeApplication.class
            .getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initializeGson();
        FacebookSdk.sdkInitialize(this);

    }

    public static synchronized ParkMeApplication getInstance() {

        return mInstance;
    }

    private void initializeGson() {
        mGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public Gson getmGson() {
        if (mGson == null) {
            initializeGson();
        }
        return mGson;
    }



    /////Location related

    ParkMeLocationListener mLocalLocationListener;
    GoogleApiClientListener mGoogleApiListner;

    public interface ParkMeLocationListener {
        public void onLocationChanged(Location location);

        public void onPermisionDisabled();
    }


    public interface GoogleApiClientListener {
        public void onConnected();

        public void onFailedToConnect();
    }


    private LocationHandler mLocationHandler;

    public LocationHandler getLocationHandler() {
        if (mLocationHandler == null) {
            mLocationHandler = new LocationHandler();
        }
        return mLocationHandler;
    }


    public void setLocationListener(ParkMeLocationListener listener, GoogleApiClientListener clientListener) {
        mLocalLocationListener = listener;
        mGoogleApiListner = clientListener;
    }

    public void setUpLocation() {
        getLocationHandler().start();
    }


    public class LocationHandler implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        public GoogleApiClient getmGoogleApiClient() {
            return mGoogleApiClient;
        }

        public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
            this.mGoogleApiClient = mGoogleApiClient;
        }

        private LocationRequest mLocationRequest;
        private final int LOCATION_ACCURACY = 200; //200 meters
        // Location updates intervals in sec
        private final int UPDATE_INTERVAL = 5000; // 5 sec
        private final int FATEST_INTERVAL = 3000; // 3 sec
        private final int DISPLACEMENT = 10; // 10 meters

        private GoogleApiClient mGoogleApiClient;

        public GoogleApiClient getGoogleApiClient() {
            return mGoogleApiClient;
        }

        public LocationRequest getLocationRequest() {
            return mLocationRequest;
        }


        public LocationHandler() {
            createLocationRequest();
            buildGoogleApiClient(this, this);
        }

        /**
         * Creating google api client object
         */
        protected synchronized void buildGoogleApiClient(GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            mGoogleApiClient = new GoogleApiClient.Builder(ParkMeApplication.this)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .addApi(LocationServices.API)
                    .build();
        }

        private void createLocationRequest() {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FATEST_INTERVAL);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(1);

        }

        public void start() {
            mGoogleApiClient.connect();
        }

        public void stop() {
//            stopLocationUpdates();
            mGoogleApiClient.disconnect();

        }

        @Override
        public void onConnected(Bundle bundle) {
            if (mGoogleApiListner != null) {
                mGoogleApiListner.onConnected();
            } else {

                startLocationUpdates();
            }
        }

        @Override
        public void onConnectionSuspended(int arg0) {
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            if (mGoogleApiListner != null) {
                mGoogleApiListner.onFailedToConnect();
            }

            Toast.makeText(ParkMeApplication.this, "Connection Failed", Toast.LENGTH_LONG).show();
        }


        @Override
        public void onLocationChanged(Location location) {
//            Toast.makeText(UnopayApplication.this, "Location received", Toast.LENGTH_LONG).show();
            if (location != null && location.getAccuracy() < LOCATION_ACCURACY) {
                if (mLocalLocationListener != null) {
                    mLocalLocationListener.onLocationChanged(location);
                }
            }
        }

        /**
         * Starting the location updates
         */
        public boolean startLocationUpdates() {

            if (ActivityCompat.checkSelfPermission(ParkMeApplication.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ParkMeApplication.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                if (mLocalLocationListener != null) {
                    mLocalLocationListener.onPermisionDisabled();
                }
                return false;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, LocationHandler.this);
            return true;

        }


        /**
         * Stopping location updates
         */
        public void stopLocationUpdates() {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
        }


    }


}
