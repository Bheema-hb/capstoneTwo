package com.sakhatech.parkme.Activity.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sakhatech.parkme.Activity.home.model.ParkingSlot;
import com.sakhatech.spotizen.R;


public class EnterSlotFragment extends DialogFragment implements View.OnClickListener {

    private static final String PARKING_LOCATION_NAME = "parking_location";

    private OnStartParkingListener mListener;
    private EditText slotNumber1, slotNumber2;
    private ParkingSlot mParkingSlot;

    public EnterSlotFragment() {
        // Required empty public constructor
    }

    public static EnterSlotFragment getInstance(String parkingLocationName) {
        EnterSlotFragment enterSlotFragment = new EnterSlotFragment();
        Bundle dataBundle = new Bundle();
        dataBundle.putString(PARKING_LOCATION_NAME, parkingLocationName);
        enterSlotFragment.setArguments(dataBundle);
        return enterSlotFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_enter_slot, container, false);
        ((TextView) rootView.findViewById(R.id.parking_location_value)).setText(mParkingSlot.carParkName);
        rootView.findViewById(R.id.action_start_parking).setOnClickListener(this);
        slotNumber1 = (EditText) rootView.findViewById(R.id.number_1);

        slotNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1)
                {
                    slotNumber2.requestFocus();
                }
            }
        });
        slotNumber2 = (EditText) rootView.findViewById(R.id.number_2);

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_start_parking) {
            if (mListener != null) {
                //TODO set the parking location
                if (slotNumber1.getText().toString().trim().length() == 0) {
                    slotNumber1.setError("Cannot be blank");
                    return;
                } else if (slotNumber2.getText().toString().length() == 0) {
                    slotNumber2.setError("Cannot be blank");
                    return;
                } else {
                    String slotNumber = slotNumber1.getText().toString() + slotNumber2.getText().toString();
                    mParkingSlot.slotNumber=slotNumber;
                    mListener.onStartParking(mParkingSlot);
                    dismiss();
                }
            }
        }
    }


    public void setData(ParkingSlot slot, OnStartParkingListener listener) {
        mListener = listener;
        mParkingSlot = slot;
    }


    public interface OnStartParkingListener {
        void onStartParking(ParkingSlot slot);
    }
}
