package com.example.android.bpmonitortwo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bpmonitortwo.R;

public class CheckActivity extends ActionBarActivity {

    private TextView mSystolicTextView, mDiastolicTextView, mSystolicStatus, mDiastolicStatus, mRecommendationsBody;
    private String mSystolicBPStatus, mDiastolicBPStatus, mRecommendations;
    private Button mReturnToMainButton;
    private int mSystolic, mDiastolic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        mSystolicTextView = (TextView) findViewById(R.id.systolicTextView);
        mDiastolicTextView = (TextView) findViewById(R.id.diastolicTextView);
        mSystolicStatus = (TextView) findViewById(R.id.systolicStatus);
        mDiastolicStatus = (TextView) findViewById(R.id.diastolicStatus);
        mRecommendationsBody = (TextView) findViewById(R.id.recommendationsBody);
        mReturnToMainButton = (Button) findViewById(R.id.returnToMainBtn);

        Intent intent = getIntent();

        mSystolic = intent.getIntExtra(getString(R.string.systolic_reading), 0);
        mDiastolic = intent.getIntExtra(getString(R.string.diastolic_reading), 0);
        mRecommendations = intent.getStringExtra(getString(R.string.recommendations));

        mSystolicTextView.setText(mSystolic + "");
        mDiastolicTextView.setText(mDiastolic + "");

        String systolicBPStatus = getSystolicBPStatus(mSystolic);
        String diastolicBPStatus = getDiastolicBPStatus(mDiastolic);

        mSystolicStatus.setText(systolicBPStatus);

        mDiastolicStatus.setText(diastolicBPStatus);

        mRecommendationsBody.setText(mRecommendations);

        // Mark any high or low readings in red.

        if (anySystolicAbnormalReadings(mSystolic)){
            mSystolicTextView.setTextColor(Color.RED);
        }

        if (anyDiastolicAbnormalReadings(mDiastolic)){
            mDiastolicTextView.setTextColor(Color.RED);
        }

        mReturnToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public boolean anySystolicAbnormalReadings(int s){

        if((s>120)||(s<90)){
            return true;
        }


        else{
            return false;
        }

    }

    public boolean anyDiastolicAbnormalReadings(int d){

        if((d>80)||(d<60)){
            return true;
        }

        else{
            return false;
        }
    }
}
