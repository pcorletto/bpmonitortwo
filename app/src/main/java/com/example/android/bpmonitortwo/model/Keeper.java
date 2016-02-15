package com.example.android.bpmonitortwo.model;

import java.text.DecimalFormat;

/**
 * Created by hernandez on 12/9/2015.
 */
public class Keeper {

    public Reading[] mReading = new Reading[7];

    private int mIndex;

    private String avgSystolic;

    private String avgDiastolic;

    private String mSystolicDiastolicString;

    private BPStatus mBPStatus = new BPStatus();

    public void reloadArray(String passedInString, int index){

        for(int i=0; i<index; i++) {

            // Extract the diastolic and systolic values from the readings
            // from the stored mSystolicDiastolicString string passed in from MainActivity.

            String systolic = passedInString.substring((38*i),(38*i+3));

            int s = Integer.parseInt(systolic);

            String diastolic = passedInString.substring((38*i+3),(38*i+6));

            int d = Integer.parseInt(diastolic);

            String pulse = passedInString.substring((38*i+6), (38*i+9));

            int p = Integer.parseInt(pulse);

            String dateAndTime = passedInString.substring((38*i+9),(38*i+23));

            String description1 = passedInString.substring((38*i+23), (38*i+37));

            char eol = passedInString.charAt(38*i+37);

            String description = description1 + eol;


            mReading[i] = new Reading(s, d, p, dateAndTime, description);

        }


    }


    public void setAReading(int s, int d, int pulse, String dateTime, String description){
        if(mIndex==7){
            //Keeper is full. Do not store any more readings...
        }
        else {
            mReading[mIndex] = new Reading(s, d, pulse, dateTime, description);

            mIndex++;
        }
    }


    public String getAllReadings(){
        String msg = "";
        int systolicValue;
        int diastolicValue;
        //int pulseValue; To be used later ... Under Construction...
        String systolicString;
        String diastolicString;
        //String pulseString; To be used later ... Under Construction ...

        for(int i=0; i<mIndex; i++){

            systolicValue = mReading[i].getSystolic();
            diastolicValue = mReading[i].getDiastolic();

            // Validate: if the systolic or diastolic values have only 1 or 2 digits, make them
            // three digits wide by putting in one or two leading zeroes.

            systolicString = putLeadingZeroes(systolicValue);
            diastolicString = putLeadingZeroes(diastolicValue);
            //pulseString = putLeadingZeroes(pulseValue);

            //The next block works perfectly fine, but I will try to put in dates and times now.
            // Concatenate the days, the systolic and diastolic values and the pressure status.

            //msg = msg + "Day " + (i+1) + ": " +
            //        systolicString + "/" +
            //        diastolicString + ": " +
            //        mReading[i].getBPStatus(mReading[i].getSystolic(), mReading[i].getDiastolic()) + ".";

            msg = msg + mReading[i].getDateAndTime() + ": " +
                    systolicString + "/" +
                    diastolicString + ": " +
                    mReading[i].getBPStatus(mReading[i].getSystolic(), mReading[i].getDiastolic()) + ".";
        }
        return msg;
    }

    public String getAllSystolicDiastolic(){
        String msg = "";
        int systolicValue;
        int diastolicValue;
        int pulseValue;
        String systolicString;
        String diastolicString;
        String pulseString;

        for(int i=0; i<mIndex; i++){

            systolicValue = mReading[i].getSystolic();
            diastolicValue = mReading[i].getDiastolic();
            pulseValue = mReading[i].getPulse();


            // Validate: if the systolic or diastolic values have only 1 or 2 digits, make them
            // three digits wide by putting in one or two leading zeroes.

            systolicString = putLeadingZeroes(systolicValue);

            diastolicString = putLeadingZeroes(diastolicValue);

            pulseString = putLeadingZeroes(pulseValue);

            msg = msg + systolicString + diastolicString + pulseString +
                    mReading[i].getDateAndTime() + mReading[i].getDescription();

        }

        return msg;

    }

    public void clearAllReadings(){

        // Reset index back to 0, and initialize the array of readings.

        mIndex = 0;
        mReading = new Reading[7];
    }


    public int getIndex(){

        return mIndex;
    }

    public void setIndex(int index){
        mIndex = index;
    }

    public void setSystolicDiastolicString(String systolicDiastolicString){

        mSystolicDiastolicString = systolicDiastolicString;

    }

    public String getSystolicDiastolicString(){

        return mSystolicDiastolicString;

    }

    public String showAverage(int dayCount){


        String avg = "AVRG: ";
        int sumSystolic = 0;
        int sumDiastolic = 0;
        double avgSystolic = 0;
        double avgDiastolic = 0;
        String bpStatus="";

        for(int i=0; i<dayCount; i++){

            sumSystolic = sumSystolic + mReading[i].getSystolic();

            sumDiastolic = sumDiastolic + mReading[i].getDiastolic();
        }
        avgSystolic = (double) sumSystolic / dayCount;
        avgDiastolic = (double) sumDiastolic / dayCount;

        // Round avgSystolic and avgDiastolic (double values) to the nearest int
        // For example, if the value is 118.7, round it to 119.

        DecimalFormat df = new DecimalFormat("#");
        double roundedAvgSystolic = Math.round(avgSystolic);
        double roundedAvgDiastolic = Math.round(avgDiastolic);

        int avgSystolicAsInt = Integer.parseInt(df.format(roundedAvgSystolic));
        int avgDiastolicAsInt = Integer.parseInt(df.format(roundedAvgDiastolic));

        bpStatus = mBPStatus.getBPStatus(avgSystolic, avgDiastolic);

        String avgSystolicString = putLeadingZeroes(avgSystolicAsInt);
        String avgDiastolicString = putLeadingZeroes(avgDiastolicAsInt);

        avg = avg + avgSystolicString + "/" + avgDiastolicString + ": " + bpStatus +".";

        return avg;

    }

    public boolean isEmpty(int index){
        if(index==0) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isFull(int index){
        if(index==7) {
            return true;
        }
        else{
            return false;
        }
    }

    public String putLeadingZeroes(int value){

        if(value<10){
            return "00" + value;

        }

        else if((value>=10)&&(value<100)){
            return "0" + value;
        }

        else{
            // if value >=100

            return value + "";

        }

    }

    public String getAvgSystolic(int dayCount){



        int sumSystolic = 0;

        double avgSystolic = 0;

        for(int i=0; i<dayCount; i++){

            sumSystolic = sumSystolic + mReading[i].getSystolic();

        }

        avgSystolic = (double) sumSystolic / dayCount;


        // Round avgSystolic (double value) to the nearest int
        // For example, if the value is 118.7, round it to 119.

        DecimalFormat df = new DecimalFormat("#");

        double roundedAvgSystolic = Math.round(avgSystolic);

        int avgSystolicAsInt = Integer.parseInt(df.format(roundedAvgSystolic));

        this.avgSystolic = avgSystolicAsInt + "";

        return this.avgSystolic;
    }

    public String getAvgDiastolic(int dayCount){



        int sumDiastolic = 0;

        double avgDiastolic = 0;

        for(int i=0; i<dayCount; i++){

            sumDiastolic = sumDiastolic + mReading[i].getDiastolic();

        }

        avgDiastolic = (double) sumDiastolic / dayCount;


        // Round avgDiastolic (double value) to the nearest int
        // For example, if the value is 78.7, round it to 79.

        DecimalFormat df = new DecimalFormat("#");

        double roundedAvgDiastolic = Math.round(avgDiastolic);

        int avgDiastolicAsInt = Integer.parseInt(df.format(roundedAvgDiastolic));

        this.avgDiastolic = avgDiastolicAsInt + "";

        return this.avgDiastolic;

    }

}