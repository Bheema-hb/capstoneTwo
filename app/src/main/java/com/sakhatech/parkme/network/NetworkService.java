package com.sakhatech.parkme.network;

import android.app.IntentService;
import android.content.Intent;

import com.sakhatech.parkme.Activity.home.HomeMapFragment;
import com.sakhatech.parkme.Activity.home.model.ParkingSlotResponse;
import com.sakhatech.parkme.ParkMeApplication;
import com.sakhatech.spotizen.BuildConfig;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bheema.
 */

public class NetworkService extends IntentService {
    public static final int GET_SLOTS = 1;

    public NetworkService() {
        super("NetworkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int requestFor = intent.getIntExtra("REQ_FOR", 0);
        if (requestFor == GET_SLOTS) {

            getParkingSlots();
        }
    }

    private void getParkingSlots() {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(NetworkConfig.URL.GET_PARKING_LOCATIONS);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            Reader reader = new InputStreamReader(in, "UTF-8");
            ParkingSlotResponse parkingSlotResponse = ParkMeApplication.getInstance().getmGson().fromJson(reader, ParkingSlotResponse.class);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(HomeMapFragment.MyWebRequestReceiver.PROCESS_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("ParkingSlots", parkingSlotResponse);
            sendBroadcast(broadcastIntent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
