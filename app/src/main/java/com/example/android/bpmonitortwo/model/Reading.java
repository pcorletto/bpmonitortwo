package com.example.android.bpmonitortwo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.bpmonitortwo.R;

/**
 * Created by hernandez on 12/9/2015.
 */
public class Reading implements Parcelable {
    private int mSystolic, mDiastolic, mPulse;
    private String mDateTime;
    private String mDescription;

    public Reading (int systolic, int diastolic, int pulse, String dateTime, String description){

        this.setSystolic(systolic);
        this.setDiastolic(diastolic);
        this.setPulse(pulse);
        mBPStatus = this.getBPStatus(systolic, diastolic);
        this.setDateAndTime(dateTime);
        this.setDescription(description);

    }

    public Reading (int systolic, int diastolic, String dateTime){

        this.setSystolic(systolic);
        this.setDiastolic(diastolic);
        mBPStatus = this.getBPStatus(systolic, diastolic);
        this.setDateAndTime(dateTime);

    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getPulse() {

        return mPulse;
    }

    public void setPulse(int pulse) {
        mPulse = pulse;
    }

    private String mBPStatus;
    private String mSystolicBPStatus;
    private String mDiastolicBPStatus;


    private BPStatus bpStatus = new BPStatus();
    private int mSystolicIconId;
    private int mDiastolicIconId;

    // Constructors..


    public int getSystolicIconId() {

        //Initialize SystolicIconId to normal
        mSystolicIconId = R.drawable.normal_reading;

        mSystolicBPStatus = getSystolicBPStatus(mSystolic);

        if(mSystolicBPStatus.equals("low")){
            mSystolicIconId = R.drawable.low_reading;
        }

        else if(mSystolicBPStatus.equals("normal")){
            mSystolicIconId = R.drawable.normal_reading;
        }

        else if(mSystolicBPStatus.equals("high")){
            mSystolicIconId = R.drawable.high_reading;
        }

        return mSystolicIconId;

    }

    public void setSystolicIconId(int systolicIconId) {
        mSystolicIconId = systolicIconId;
    }

    public int getDiastolicIconId() {
        //Initialize DiastolicIconId to normal
        mDiastolicIconId = R.drawable.normal_reading;

        mDiastolicBPStatus = getDiastolicBPStatus(mDiastolic);

        if(mDiastolicBPStatus.equals("low")){
            mDiastolicIconId = R.drawable.low_reading;
        }

        else if(mDiastolicBPStatus.equals("normal")){
            mDiastolicIconId = R.drawable.normal_reading;
        }

        else if(mDiastolicBPStatus.equals("high")){
            mDiastolicIconId = R.drawable.high_reading;
        }

        return mDiastolicIconId;
    }

    public void setDiastolicIconId(int diastolicIconId) {
        mDiastolicIconId = diastolicIconId;
    }

    public Reading(){

    }


    public void setSystolic(int s){
        mSystolic = s;
    }

    public int getSystolic(){
        return this.mSystolic;
    }

    public void setDiastolic(int d){
        mDiastolic = d;
    }

    public int getDiastolic(){
        return this.mDiastolic;
    }

    public void setBPStatus(String status){
        mBPStatus = status;
    }

    public String getBPStatus(int s, int d) {

        mBPStatus = bpStatus.getBPStatus(s, d);

        return this.mBPStatus;

    }

    public String getBPStatus(){

        return mBPStatus;
    }

    public String getSystolicBPStatus(double s){
        if(s<90){
            mSystolicBPStatus="low";
        }
        else if(s>120){
            mSystolicBPStatus="high";

        }
        else{
            mSystolicBPStatus="normal";
        }
        return mSystolicBPStatus;
    }

    public String getDiastolicBPStatus(double d){
        if(d<60){
            mDiastolicBPStatus="low";
        }
        else if(d>80){
            mDiastolicBPStatus="high";

        }
        else{
            mDiastolicBPStatus="normal";
        }
        return mDiastolicBPStatus;
    }


    public String getDateAndTime(){
        return this.mDateTime;
    }

    public void setDateAndTime(String dateAndTime) {
        mDateTime = dateAndTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mSystolic);
        dest.writeInt(mDiastolic);
        dest.writeInt(mPulse);
        dest.writeString(mDescription);
        dest.writeString(mDateTime);
        dest.writeString(mBPStatus);

    }

    private Reading(Parcel in){

        mSystolic = in.readInt();
        mDiastolic = in.readInt();
        mPulse = in.readInt();
        mDescription = in.readString();
        mDateTime = in.readString();
        mBPStatus = in.readString();

    }

    public static final Creator<Reading>CREATOR = new Creator<Reading>() {
        @Override
        public Reading createFromParcel(Parcel source) {
            return new Reading(source);
        }

        @Override
        public Reading[] newArray(int size) {
            return new Reading[size];
        }
    };
}

