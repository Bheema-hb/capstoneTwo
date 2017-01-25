package com.sakhatech.parkme.Activity.home;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sakhatech.parkme.Activity.BaseActivity;
import com.sakhatech.parkme.Activity.BaseFragment;
import com.sakhatech.parkme.Activity.home.model.StartParkingResponse;
import com.sakhatech.parkme.Activity.payment.PaymentActivity;
import com.sakhatech.parkme.ParkMeApplication;
import com.sakhatech.parkme.data.Contract;
import com.sakhatech.parkme.model.PaymentSummary;
import com.sakhatech.parkme.model.ParkMeResponse;
import com.sakhatech.parkme.network.NetworkConfig;
import com.sakhatech.parkme.network.NetworkService;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.parkme.Activity.home.map.MarkerInfoWindowAdapter;
import com.sakhatech.parkme.Activity.home.model.EndParkingResponse;
import com.sakhatech.parkme.Activity.home.model.ParkingSlot;
import com.sakhatech.parkme.Activity.home.model.ParkingSlotResponse;
import com.sakhatech.parkme.Activity.home.model.StartParkingRequest;
import com.sakhatech.parkme.Activity.payment.IntentConstant;
import com.sakhatech.spotizen.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class HomeMapFragment extends BaseFragment implements OnMapReadyCallback, ParkMeApplication.ParkMeLocationListener, ParkMeApplication.GoogleApiClientListener, EnterSlotFragment.OnStartParkingListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int PAYMENT_SUMMARY = 111;
    GoogleMap mGoogleMap;
    MapView mMapView;
    Location mLastLocation;
    private float DISTANCE_THRESHOLD = 50;//50 meters
    private ParkingSlotResponse mParkingSlots;

    protected static final int REQUEST_CHECK_SETTINGS = 100;
    private boolean mIsSlotsRequested;

    ArrayList<ParkingSlot> parkingSlots = new ArrayList<>();
    private Marker carLocationMarker;
    private Marker currentLocationMarker;
    Button mStartParkingButton;
    ParkingSlot mSelectedParking;

    private HashMap<Marker, ParkingSlot> mMarkersHashMap = new HashMap<>();
    private MarkerInfoWindowAdapter mParkedInfoWindowAdapter;
    private static TimerTask mTimerTask;
    static Timer mParkingTimer;
    private Handler mHandler;

    RelativeLayout  mParkedLayout;
    boolean mIsRestoreParkCalled;
    TextView mParkedAreaName, mParkedPriceTextView;

    RelativeLayout mLoadingLayout, mStartParkingLayout;
    TextView mLoadingText;
    private boolean mIsSetFirstParkedMarker;
    private MyWebRequestReceiver receiver;
    private int PARK_LOADER = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_map_frag_layout, null);
        mParkedLayout = (RelativeLayout) view.findViewById(R.id.parked_layout);
        mParkedAreaName = (TextView) view.findViewById(R.id.parked_area_name);
        mParkedPriceTextView = (TextView) view.findViewById(R.id.parking_price);
        mMapView = (MapView) view.findViewById(R.id.map_view);
        mStartParkingLayout = (RelativeLayout) view.findViewById(R.id.start_parking_layout);
        mLoadingLayout = (RelativeLayout) view.findViewById(R.id.loading_layout);
        mLoadingText = (TextView) view.findViewById(R.id.loading_text);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
//        createDummyArray();
        mHandler = new Handler();
        mStartParkingButton = (Button) view.findViewById(R.id.start_parking_button);
        mStartParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedParking != null && mSelectedParking.isParked) {
                    //TODO end parking
                    endParking();
                } else {
                    showEnterSlotDialog();
                }
            }
        });

        mIsRestoreParkCalled = false;
        showLoading(getString(R.string.loading));

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        intitiateLocation();
        showToast(getString(R.string.waiting_for_location), Toast.LENGTH_LONG);
        getPreviousParkingStatus();

    }


    private void getPreviousParkingStatus() {
        mLastLocation = null;
        getActivity().getSupportLoaderManager().initLoader(PARK_LOADER, null, this);


    }

    private void endParking() {
        //Ending the parking with dummy payment summary
        mSelectedParking.parkingEndTime = System.currentTimeMillis();
        cancelTimer();
        PaymentSummary paymentSummary = new PaymentSummary();
        paymentSummary.setmAllottedSlot(mSelectedParking.slotNumber + "");
        paymentSummary.setmArrival(mSelectedParking.parkingStartedTime);
        paymentSummary.setmDate(System.currentTimeMillis());
        paymentSummary.setmExit(System.currentTimeMillis());
        paymentSummary.setmLocation(mSelectedParking.carParkName);
        paymentSummary.setmPrice(200);
        String vehicleNumber=(mSelectedParking.vehicle==null)?"KA01MC2525":mSelectedParking.vehicle;
        paymentSummary.setmVehicleNumber(vehicleNumber);

        //clear map and start payment screen
        mGoogleMap.clear();
        mSelectedParking.isParkingEnd = true;

        Intent paymentIntent = new Intent(getActivity(), PaymentActivity.class);
        paymentIntent.putExtra(IntentConstant.PAYMENT_SUMMARY, paymentSummary);
        startActivityForResult(paymentIntent, PAYMENT_SUMMARY);

        getActivity().getContentResolver().delete(Contract.Record.uri, null, null);


    }


    private void initializeMap() {

        mStartParkingButton.setBackgroundResource(R.drawable.button_backgnd);
        mStartParkingButton.setText(getString(R.string.start_parking));
        hideView(mParkedLayout);
        mIsSlotsRequested = false;
        carLocationMarker = null;
    }

    private void showGeneralErrorDialog(String message) {
        showDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
            }
        }, getString(R.string.ok), getString(R.string.error_title), message, false);
    }


    private void intitiateLocation() {
        ParkMeApplication.getInstance().setLocationListener(this, this);
        ParkMeApplication.getInstance().getLocationHandler().start();
    }

    @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.onResume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (location != null) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    onPermisionDisabled();
                    return;
                }

//                showToast("Got location", Toast.LENGTH_SHORT);
                location.setLatitude(12.916867);
                location.setLongitude(77.585867);
                locateCurrentLocation(location);

                if (mSelectedParking == null && mIsRestoreParkCalled && !mIsSlotsRequested) {
                    // Creating a LatLng object for the current location
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mGoogleMap.setMyLocationEnabled(true);
                    getParkingSlots();
                }
            }
        }
    }

    private void locateCurrentLocation(Location location) {
        mLastLocation = location;
        if (currentLocationMarker == null) {

            LatLng slot = new LatLng(location.getLatitude(), location.getLongitude());

            currentLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(slot)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
        } else {

            double[] startValues = new double[]{currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude};
            double[] endValues = new double[]{location.getLatitude(), location.getLongitude()};
            ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
            latLngAnimator.setDuration(600);
            latLngAnimator.setInterpolator(new DecelerateInterpolator());

            latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    double[] animatedValue = (double[]) animation.getAnimatedValue();
                    currentLocationMarker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
                }
            });
            latLngAnimator.start();
        }
    }

    private void getParkingSlots() {
        if (Utility.isOnline(getActivity())) {
            mIsSlotsRequested = true;
            showLoading(getString(R.string.getting_slots));

            IntentFilter filter = new IntentFilter(MyWebRequestReceiver.PROCESS_RESPONSE);
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            receiver = new MyWebRequestReceiver();
            getActivity().registerReceiver(receiver, filter);


            Intent msgIntent = new Intent(getActivity(), NetworkService.class);

            msgIntent.putExtra("REQ_FOR", NetworkService.GET_SLOTS);
            getActivity().startService(msgIntent);


        } else {
            showGetParkingSlotsErrorDialog(getString(R.string.check_internet));
        }

    }


    public void handleGetSlotsResponse(ParkingSlotResponse slotResponse) {
        if (slotResponse != null) {
            if (slotResponse != null && slotResponse.data != null && slotResponse.data.success) {
                mIsSlotsRequested = true;
                Log.d("Parking Slot", "Parking Slot" + ParkMeApplication.getInstance().getmGson().toJson(slotResponse));
                setParkingLots(slotResponse);

            } else if (slotResponse != null && slotResponse.getError() != null) {
                showGetParkingSlotsErrorDialog(slotResponse.getError().message);
            } else {
                showGetParkingSlotsErrorDialog(null);
            }
        } else {
            showGetParkingSlotsErrorDialog(null);
        }
    }

    private void setParkingLots(ParkingSlotResponse slotResponse) {
        mParkingSlots = slotResponse;
        setUpParkingDetial();
    }

    private void setUpParkingDetial() {
        showView(mStartParkingLayout);
        showView(mStartParkingButton);
        hideLoading();
        setUpMap();
        for (int i = 0; i < mParkingSlots.data.slots.size(); i++) {
            ParkingSlot slot = mParkingSlots.data.slots.get(i);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(Utility.convertToPixels(getActivity(), 11));
            String text = "" + slot.availableSlots;
            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker2_green).copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bmp);
            //If the text is bigger than the canvas , reduce the font size
            if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.setTextSize(Utility.convertToPixels(getActivity(), 11));        //Scaling needs to be used for different dpi's

            //Calculate the positions
            int xPos = (canvas.getWidth() / 2) - 20;     //-10 is for regulating the x position offset

            //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2) - 20);
//
            canvas.drawText(text, xPos, yPos, paint);

            // Create user marker with custom icon and other options
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(slot.latitude, slot.longitude));
//            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin2_green));
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(bmp));
//            markerOption.icon(BitmapDescriptorFactory.fromBitmap(Utility.writeTextOnDrawable(R.drawable.marker1_green, "" + slot.availableSlots, getActivity())));
            Marker currentMarker = mGoogleMap.addMarker(markerOption);
            mMarkersHashMap.put(currentMarker, slot);


        }
        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity());
        markerInfoWindowAdapter.setMarkerHash(mMarkersHashMap);

        mGoogleMap.setInfoWindowAdapter(markerInfoWindowAdapter);

    }

    public void setParkedInfoWindow(boolean isFromRestore) {


        mParkedInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity());
        mMarkersHashMap = new HashMap<>();
        // Create user marker with custom icon and other options
        MarkerOptions markerOption = new MarkerOptions().position(new LatLng(mSelectedParking.latitude, mSelectedParking.longitude));
//            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin2_green));
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon));
        if (carLocationMarker != null) {
            carLocationMarker.remove();
        }
        carLocationMarker = mGoogleMap.addMarker(markerOption);
        if (mIsSetFirstParkedMarker) {
            mIsSetFirstParkedMarker = false;
            LatLng latLng = new LatLng(mSelectedParking.latitude, mSelectedParking.longitude);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
        }
        mMarkersHashMap.put(carLocationMarker, mSelectedParking);

        mParkedInfoWindowAdapter.setMarkerHash(mMarkersHashMap);
        mGoogleMap.setInfoWindowAdapter(mParkedInfoWindowAdapter);
        carLocationMarker.showInfoWindow();

    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    private void setUpMap() {

        if (mGoogleMap != null) {
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                    try {
                        //Crashing when clicked on car
                        marker.showInfoWindow();
                        if (!mMarkersHashMap.get(marker).isVehicle) {
                            mSelectedParking = mMarkersHashMap.get(marker);
                        }
                        return true;
                    } catch (Exception e) {

                    }
                    return false;
                }
            });
        }
    }


    private void showGetParkingSlotsErrorDialog(String message) {
        if (message == null) {
            message = getString(R.string.unable_to_fetch_slots);
        }
        showDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                getParkingSlots();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                dialog.dismiss();

            }
        }, getString(R.string.retry), getString(R.string.cancel), getString(R.string.error_title), message, false, false);

    }


    @Override
    public void onPermisionDisabled() {
        showToast(getString(R.string.location_permission_not_available), Toast.LENGTH_LONG);
        if (getActivity() != null) {
            getActivity().finish();
        } else {
            System.exit(1);
        }

    }

    @Override
    public void onConnected() {
        LocationRequest locationRequest = ParkMeApplication.getInstance().getLocationHandler().getLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        builder.setNeedBle(false);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(ParkMeApplication.getInstance().getLocationHandler().getGoogleApiClient(), builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        ParkMeApplication.getInstance().getLocationHandler().startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    getActivity(),
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        showToast(getString(R.string.unable_to_locate), Toast.LENGTH_LONG);
                        break;
                }
            }
        });
    }

    @Override
    public void onFailedToConnect() {
        showDialog(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    dismissDialog();
                    getActivity().finish();
                }
            }
        }, getString(R.string.ok), getString(R.string.location_error), getString(R.string.unable_to_locate), false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ParkMeApplication.getInstance().getLocationHandler().startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        getActivity().finish();
                        break;

                }
                break;
            case PAYMENT_SUMMARY:
                resetParking();
                break;


        }
    }

    private void resetParking() {
        ((BaseActivity) getActivity()).showToolBar();
        mGoogleMap.clear();
        initializeMap();
        mSelectedParking = null;
        mIsRestoreParkCalled = true;
        mIsSlotsRequested = false;
        currentLocationMarker = null;
        hideView(mStartParkingLayout);
        onLocationChanged(mLastLocation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {

        }
        ParkMeApplication.getInstance().getLocationHandler().stopLocationUpdates();
        cancelTimer();
    }


    public void showEnterSlotDialog() {

        if (mSelectedParking == null) {
            showToast(getString(R.string.select_parking_location), Toast.LENGTH_SHORT);
            return;
        }
        EnterSlotFragment dialogFragment = new EnterSlotFragment();
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFragmentTheme);
        dialogFragment.setData(mSelectedParking, this);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (!dialogFragment.isAdded()) {
            try {
                dialogFragment.show(fm, "Notification fragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStartParking(ParkingSlot slotNumber) {

        startParking(slotNumber);

    }

    private void startParking(ParkingSlot slot) {

        mGoogleMap.clear();
        mSelectedParking.isVehicle = true;
        mSelectedParking.isParked = true;
        mSelectedParking.parkingStartedTime = System.currentTimeMillis();
        mSelectedParking.isParked = true;
        mIsSetFirstParkedMarker = true;
        mSelectedParking.vehicle = ParkMeSharedPreference.getInstance(getActivity()).getVehicle();
        mSelectedParking.slotNumber=slot.slotNumber;
        mSelectedParking.parkingArea=slot.parkingArea;
        initializeParkedTimer(false);


//            showBadgeDialog();
        ContentValues values = new ContentValues();
        values.put(Contract.Record.PARK_AREA, slot.parkingArea);
        values.put(Contract.Record.PARK_AVAILABLE_SLOTS, slot.availableSlots);
        values.put(Contract.Record.PARK_ID, slot.parkId);
        values.put(Contract.Record.PARK_LATITUDE, slot.latitude);
        values.put(Contract.Record.PARK_LONGITUDE, slot.longitude);
        values.put(Contract.Record.PARK_NAME, slot.carParkName);
        values.put(Contract.Record.PARK_START_TIME, System.currentTimeMillis());
        values.put(Contract.Record.PARK_SLOT_NUMBER, slot.slotNumber);


        getActivity().getContentResolver().insert(Contract.Record.uri, values);


    }


    private void initializeParkedTimer(final boolean isFromRestore) {
        mIsSetFirstParkedMarker = true;
        hideToolBar();
        hideLoading();
        mStartParkingButton.setBackgroundResource(R.drawable.button_backgnd_end);
        mStartParkingButton.setText(getString(R.string.end_parking));
        showView(mStartParkingLayout);
        showView(mParkedLayout);
        mParkedAreaName.setText(mSelectedParking.carParkName);
        mParkedPriceTextView.setText(getString(R.string.dollar_symbol) + "200");

        mParkingTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setParkedInfoWindow(isFromRestore);
                    }
                });
            }
        };
        mParkingTimer.scheduleAtFixedRate(mTimerTask, 1000, 1000);
    }

    private void hideToolBar() {
        ((BaseActivity) getActivity()).hideToolBar();
    }


    public void showBadgeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.badge_layout);

        TextView okButton = (TextView) dialog.findViewById(R.id.badge_ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void cancelTimer() {
//        if (mTimerTask != null) {
//            mTimerTask.cancel();
//        }
        if (mParkingTimer != null) {
            mParkingTimer.purge();
            mParkingTimer.cancel();
        }

    }

    public void showLoading(String message) {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingText.setText(message);
    }

    public void hideLoading() {
        mLoadingLayout.setVisibility(View.GONE);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = Contract.Record.uri;
        return new CursorLoader(getActivity(),
                baseUri,
                null,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        handlePreviousParking(data);
        mIsRestoreParkCalled = true;
        getActivity().getSupportLoaderManager().destroyLoader(PARK_LOADER);
    }

    private void handlePreviousParking(Cursor data) {
        if (data != null && data.moveToFirst()) {

            mSelectedParking = new ParkingSlot();
            mSelectedParking.parkingStartedTime = Long.parseLong(data.getString(data.getColumnIndex(Contract.Record.PARK_START_TIME)));
            mSelectedParking.isParked = true;
            mSelectedParking.vehicle = ParkMeSharedPreference.getInstance(getActivity()).getVehicle();
            mSelectedParking.parkingArea = data.getString(data.getColumnIndex(Contract.Record.PARK_AREA));
            mSelectedParking.availableSlots = Integer.parseInt(data.getString(data.getColumnIndex(Contract.Record.PARK_AVAILABLE_SLOTS)));
            mSelectedParking.parkId = Long.parseLong(data.getString(data.getColumnIndex(Contract.Record.PARK_ID)));
            mSelectedParking.isVehicle = true;
            mSelectedParking.latitude = Double.parseDouble(data.getString(data.getColumnIndex(Contract.Record.PARK_LATITUDE)));
            mSelectedParking.longitude = Double.parseDouble(data.getString(data.getColumnIndex(Contract.Record.PARK_LONGITUDE)));
            mSelectedParking.slotNumber = data.getString(data.getColumnIndex(Contract.Record.PARK_SLOT_NUMBER));
//                mSelectedParking.parkLotType = restoreResponse.data.parkLotType;
            if (mGoogleMap != null) {
                mGoogleMap.clear();
                initializeParkedTimer(true);
            }

            LatLng latLng = new LatLng(mSelectedParking.latitude, mSelectedParking.longitude);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));


        } else {

            //no previous parking state
            if (mLastLocation != null) {
                onLocationChanged(mLastLocation);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    public class MyWebRequestReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE = "com.nanodegree.intent.action.SLOT_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            ParkingSlotResponse parkingSlotResponse = (ParkingSlotResponse) intent.getSerializableExtra("ParkingSlots");
            handleGetSlotsResponse(parkingSlotResponse);

        }


    }
}
