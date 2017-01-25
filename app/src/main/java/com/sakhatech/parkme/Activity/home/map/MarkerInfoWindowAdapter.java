package com.sakhatech.parkme.Activity.home.map;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.parkme.Activity.home.model.ParkingSlot;
import com.sakhatech.spotizen.R;

import java.util.HashMap;

/**
 * Created by Bheema
 * Company Techjini
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    Context mContext;
    Handler mHandler;


    private HashMap<Marker, ParkingSlot> mMarkersHashMap;

    TextView parkedTimeTextView;

    public MarkerInfoWindowAdapter(Context context) {
        mContext = context;
        mHandler = new Handler();
    }

    public void setMarkerHash(HashMap<Marker, ParkingSlot> markerMap) {
        mMarkersHashMap = markerMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        try {
            final ParkingSlot parkingSlot = mMarkersHashMap.get(marker);
            if (!parkingSlot.isVehicle) {
                View v = ((LayoutInflater) mContext.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.infowindow_layout, null);


                TextView parkingPrice = (TextView) v.findViewById(R.id.parking_price);

                TextView parkingArea = (TextView) v.findViewById(R.id.parking_area);
                TextView availableSlots = (TextView) v.findViewById(R.id.available_slot);

                parkingArea.setText(parkingSlot.parkingArea);
                availableSlots.setText(parkingSlot.availableSlots + " " + mContext.getString(R.string.slots_available_near) + " " + parkingSlot.parkingArea);
                parkingPrice.setText(mContext.getString(R.string.rupee_symbol) + "200");
                return v;
            } else {
                if (parkingSlot.isVehicle && parkingSlot.isParked) {
                    View v = ((LayoutInflater) mContext.getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.car_info_window, null);


                    TextView slotNumber = (TextView) v.findViewById(R.id.slot_number);
                    parkedTimeTextView = (TextView) v.findViewById(R.id.parked_time);

                    slotNumber.setText(mContext.getString(R.string.slot_number) + " " + parkingSlot.slotNumber);
                    parkedTimeTextView.setText(Utility.getSpentDuration(System.currentTimeMillis() - parkingSlot.parkingStartedTime));

                    return v;
                }

            }
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) throws RuntimeException {

        return null;
    }


}