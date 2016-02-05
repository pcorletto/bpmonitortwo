package com.example.android.bpmonitortwo.model;

/**
 * Created by hernandez on 1/11/2016...
 */
public class BPStatus{

    private String mBPStatus = "";

    public String getBPStatus(double s, double d){
        if((s>120)||(d>80)){

            mBPStatus = "high       ";

            if(s<90){

                mBPStatus = "S:LO, D:HI " ;
            }

            if(d<60){

                mBPStatus = "S:HI, D:LO " ;
            }

        }
        else if (((s>=90)&&(s<=120)&&((d>=60)&&(d<=80)))) {

            mBPStatus = "normal     ";
        }
        else{

            mBPStatus = "low        ";
        }
        return mBPStatus;
    }


}

