package com.sakhatech.parkme.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.sakhatech.spotizen.BR;


/**
 * Created by Bheema.
 * // Copyright (c) 2016 Techjini Solutions. All rights reserved.
 */
public class PaymentSummary extends BaseObservable implements Parcelable {
    public String mLocation;
    public long mDate;
    public long mArrival;
    public long mExit;
    public String mVehicleNumber;
    public String mAllottedSlot;
    public double mPrice;
    public double mOtherFee;

    public PaymentSummary()
    {

    }

    protected PaymentSummary(Parcel in) {
        mLocation = in.readString();
        mDate = in.readLong();
        mArrival = in.readLong();
        mExit = in.readLong();
        mVehicleNumber = in.readString();
        mAllottedSlot = in.readString();
        mPrice = in.readDouble();
        mOtherFee = in.readDouble();
    }

    public static final Creator<PaymentSummary> CREATOR = new Creator<PaymentSummary>() {
        @Override
        public PaymentSummary createFromParcel(Parcel in) {
            return new PaymentSummary(in);
        }

        @Override
        public PaymentSummary[] newArray(int size) {
            return new PaymentSummary[size];
        }
    };

    @Bindable
    public String getmLocation() {
        return mLocation;

    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
        notifyPropertyChanged(BR.mLocation);
    }

    @Bindable
    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
        notifyPropertyChanged(BR.mDate);
    }

    @Bindable
    public long getmArrival() {
        return mArrival;
    }

    public void setmArrival(long mArrival) {
        this.mArrival = mArrival;
        notifyPropertyChanged(BR.mArrival);
    }

    @Bindable
    public long getmExit() {
        return mExit;
    }

    public void setmExit(long mExit) {
        this.mExit = mExit;
        notifyPropertyChanged(BR.mExit);
    }

    @Bindable
    public String getmVehicleNumber() {
        return mVehicleNumber;
    }

    public void setmVehicleNumber(String mVehicleNumber) {
        this.mVehicleNumber = mVehicleNumber;
        notifyPropertyChanged(BR.mVehicleNumber);
    }

    @Bindable
    public String getmAllottedSlot() {
        return mAllottedSlot;
    }

    public void setmAllottedSlot(String mAllottedSlot) {
        this.mAllottedSlot = mAllottedSlot;
        notifyPropertyChanged(BR.mAllottedSlot);
    }

    @Bindable
    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
        notifyPropertyChanged(BR.mPrice);
    }

    @Bindable
    public double getmOtherFee() {
        return mOtherFee;
    }

    public void setmOtherFee(double mOtherFee) {
        this.mOtherFee = mOtherFee;
        notifyPropertyChanged(BR.mOtherFee);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLocation);
        dest.writeLong(mDate);
        dest.writeLong(mArrival);
        dest.writeLong(mExit);
        dest.writeString(mVehicleNumber);
        dest.writeString(mAllottedSlot);
        dest.writeDouble(mPrice);
        dest.writeDouble(mOtherFee);
    }
}
