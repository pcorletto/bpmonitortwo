package com.example.android.bpmonitortwo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bpmonitortwo.R;
import com.example.android.bpmonitortwo.model.Keeper;
import com.example.android.bpmonitortwo.model.Reading;

import java.util.Calendar;
import java.util.Locale;



public class MainActivity extends ActionBarActivity {

    private EditText lastSystolicEditText, lastDiastolicEditText,
            systolicEditText, diastolicEditText, pulseEditText, descriptionEditText;
    private TextView readingCountTextView;
    private Button bpCheckButton, storeReadingButton, displayReadingsButton, resetUIButton,
            clearWeeklyReadingsButton, hideButton;
    private int mSystolic, mDiastolic, mPulse;
    private String bpCheckStatus, mRecommendations, mDescription;
    private Reading mReading = new Reading();
    private Keeper mWeeklyReadingKeeper = new Keeper();
    private int mIndex;
    private int mDayCount;
    private String mWeeklyReadings;
    private String mSystolicDiastolicString;
    private String mLanguagePreference;
    private boolean backPressToExit = false;
    private String avgSystolic, avgDiastolic;

    public static final String DAILY_READING = "DAILY_READING";
    public static final String INDEX = "INDEX";

    // Create an array of seven strings to pass it to the DisplayActivity's ListView to display
    // the results

    // Put in some dummy data into displayArray (array of seven strings) to experiment.
    // Later, I will fill this array of strings with the readings extracted from the
    // weekly_readings string. This will be transmitted to DisplayActivity via an Intent.

    private String[] displayArray = {"Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7", "AVRG"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lastSystolicEditText = (EditText) findViewById(R.id.lastSystolicEditText);
        lastDiastolicEditText = (EditText) findViewById(R.id.lastDiastolicEditText);
        systolicEditText = (EditText) findViewById(R.id.systolicEditText);
        diastolicEditText = (EditText) findViewById(R.id.diastolicEditText);
        pulseEditText = (EditText) findViewById(R.id.pulseEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        bpCheckButton = (Button) findViewById(R.id.bpCheckButton);
        storeReadingButton = (Button) findViewById(R.id.storeReadingButton);
        displayReadingsButton = (Button) findViewById(R.id.displayReadingsButton);
        resetUIButton = (Button) findViewById(R.id.resetUIButton);
        readingCountTextView = (TextView) findViewById(R.id.readingCountTextView);
        clearWeeklyReadingsButton = (Button) findViewById(R.id.clearWeeklyReadingsButton);
        hideButton = (Button) findViewById(R.id.hideButton);


        // Retrieve any previous reading stored on the SharedPreferences file

        SharedPreferences sharedPreferences = MainActivity.this
                .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);
        mSystolic = sharedPreferences.getInt(getString(R.string.SYSTOLIC_READING),0);
        mDiastolic = sharedPreferences.getInt(getString(R.string.DIASTOLIC_READING),0);
        mIndex = sharedPreferences.getInt(getString(R.string.READING_COUNT), 0);
        mWeeklyReadings = sharedPreferences.getString(getString(R.string.WEEKLY_READINGS),"");
        mSystolicDiastolicString = sharedPreferences.getString(getString(R.string.SYSTOLIC_DIASTOLIC_STRING), "");
        mLanguagePreference = sharedPreferences.getString(getString(R.string.LANGUAGE_PREF), "en");


        // Display any previous readings in the edit texts for last systolic and diastolic
        // and readingCountEditText

        lastSystolicEditText.setTextColor(Color.BLACK);
        lastSystolicEditText.setText(mSystolic + "");

        lastDiastolicEditText.setTextColor(Color.BLACK);
        lastDiastolicEditText.setText(mDiastolic + "");

        readingCountTextView.setText(mIndex + "");

        // Mark any high or low readings in red.

        if (anySystolicAbnormalReadings(mSystolic)){
            lastSystolicEditText.setTextColor(Color.RED);
        }

        if (anyDiastolicAbnormalReadings(mDiastolic)){
            lastDiastolicEditText.setTextColor(Color.RED);
        }

        bpCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mSystolicString = systolicEditText.getText().toString();

                // Check if the user did not enter anything. If no entry, then alert
                if(TextUtils.isEmpty(mSystolicString)){
                    systolicEditText.setError(getString(R.string.empty_reading_alert));
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                    return;
                }

                // If the value entered is 1000 or greater, alert that it can only be three digits
                //long.

                if(mSystolicString.length()>3){
                    systolicEditText.setError(getString(R.string.max_alert));
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                    return;
                }

                mSystolic = Integer.parseInt(mSystolicString);

                String mDiastolicString = diastolicEditText.getText().toString();

                // Check if the user did not enter anything. If no entry, then alert
                if(TextUtils.isEmpty(mDiastolicString)){
                    diastolicEditText.setError(getString(R.string.empty_reading_alert));
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                    return;
                }

                // If the value entered is 1000 or greater, alert that it can only be three digits
                //long.

                if(mDiastolicString.length()>3){
                    diastolicEditText.setError(getString(R.string.max_alert));
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                    return;
                }

                mDiastolic = Integer.parseInt(mDiastolicString);

                // Mark any high or low readings in red.

                if (anySystolicAbnormalReadings(mSystolic)){
                    systolicEditText.setTextColor(Color.RED);
                }

                if (anyDiastolicAbnormalReadings(mDiastolic)){
                    diastolicEditText.setTextColor(Color.RED);
                }

                // Check whether pressure is low, normal or high, and
                // display the check status in a toast



                bpCheckStatus = mReading.getBPStatus(mSystolic, mDiastolic);


                // Transmit the mSystolic and mDiastolic values to the CheckActivity
                // via an Intent.

                mRecommendations = getRecommendations(mReading.getSystolicBPStatus(mSystolic), mReading.getDiastolicBPStatus(mDiastolic));

                Intent intent = new Intent(MainActivity.this, CheckActivity.class);

                intent.putExtra(getString(R.string.systolic_reading), mSystolic);
                intent.putExtra(getString(R.string.diastolic_reading), mDiastolic);
                intent.putExtra(getString(R.string.recommendations), mRecommendations);
                startActivity(intent);



                //Toast.makeText(MainActivity.this, getString(R.string.your_blood_pressure_is) +
                  //      bpCheckStatus, Toast.LENGTH_LONG).show();

                // Store the last reading on a SharedPreferences file so that it can be
                // displayed even after the app is stopped.

                SharedPreferences sharedPreferences = MainActivity.this
                        .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.SYSTOLIC_READING), mSystolic);
                editor.putInt(getString(R.string.DIASTOLIC_READING), mDiastolic);
                editor.putString(getString(R.string.LANGUAGE_PREF), mLanguagePreference);
                editor.commit();

                // Display this checked readings in the edit texts for last systolic and diastolic

                lastSystolicEditText.setTextColor(Color.BLACK);
                lastSystolicEditText.setText(mSystolic + "");

                lastDiastolicEditText.setTextColor(Color.BLACK);
                lastDiastolicEditText.setText(mDiastolic + "");

                // Mark any high or low readings in red.

                if (anySystolicAbnormalReadings(mSystolic)){
                    lastSystolicEditText.setTextColor(Color.RED);
                }

                if (anyDiastolicAbnormalReadings(mDiastolic)){
                    lastDiastolicEditText.setTextColor(Color.RED);
                }

            }
        });

        // Clear any prior readings
        systolicEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                systolicEditText.setTextColor(Color.BLACK);
                systolicEditText.requestFocus();
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.showSoftInput(systolicEditText, 0);
                }
                systolicEditText.setText("");
                return true;
            }
        });

        diastolicEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                diastolicEditText.setTextColor(Color.BLACK);
                diastolicEditText.requestFocus();
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.showSoftInput(diastolicEditText, 0);
                }
                diastolicEditText.setText("");
                return true;
            }
        });

        storeReadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWeeklyReadingKeeper.isFull(mIndex)) {
                    Toast.makeText(MainActivity.this, getString(R.string.keeper_full_message),
                            Toast.LENGTH_LONG).show();

                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);

                } else {

                    String mSystolicString = systolicEditText.getText().toString();

                    // Check if the user did not enter anything. If no entry, then alert
                    if (TextUtils.isEmpty(mSystolicString)) {
                        systolicEditText.setError(getString(R.string.empty_reading_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;
                    }

                    // If the value entered is 1000 or greater, alert that it can only be three digits
                    //long.

                    if(mSystolicString.length()>3){
                        systolicEditText.setError(getString(R.string.max_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;
                    }

                    mSystolic = Integer.parseInt(mSystolicString);


                    String mDiastolicString = diastolicEditText.getText().toString();

                    // Check if the user did not enter anything. If no entry, then alert
                    if (TextUtils.isEmpty(mDiastolicString)) {
                        diastolicEditText.setError(getString(R.string.empty_reading_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;
                    }

                    // If the value entered is 1000 or greater, alert that it can only be three digits
                    //long.

                    if(mDiastolicString.length()>3){
                        diastolicEditText.setError(getString(R.string.max_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;

                    }

                    mDiastolic = Integer.parseInt(mDiastolicString);

                    // Also, capture pulse and description from EditTexts, just like it did before
                    // for systolic and diastolic. I need to store these in SharedPref file later.

                    String mPulseString = pulseEditText.getText().toString();

                    // Check if the user did not enter anything. If no entry, then alert
                    if (TextUtils.isEmpty(mPulseString)) {
                        pulseEditText.setError(getString(R.string.empty_reading_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;
                    }

                    // If the value entered is 1000 or greater, alert that it can only be three digits
                    //long.

                    if(mPulseString.length()>3){
                        pulseEditText.setError(getString(R.string.max_alert));
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                        toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);
                        return;

                    }

                    mPulse = Integer.parseInt(mPulseString);

                    mDescription = descriptionEditText.getText().toString();

                    // Make Description exactly 40 characters long.

                    int descLength = mDescription.length();

                    String builderString = "";

                    for(int i=0; i<(15-descLength) ; i++){

                        builderString = builderString + " ";

                    }

                    mDescription = mDescription + builderString;



                    // Set a reading at last index position.

                    SharedPreferences sharedPreferences = MainActivity.this
                            .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);
                    mIndex = sharedPreferences.getInt(getString(R.string.READING_COUNT), 0);

                    mWeeklyReadingKeeper.setIndex(mIndex);

                    // Re-fill the readings keeper array with the values previously stored
                    // in the mSystolicDiastolicString stored in the SharedPreferences file.
                    // I will use a method I called "reloadArray".
                    // Otherwise, we would get a Null Pointer Exception for trying to
                    // get values from an array with empty positions when we call the
                    // getAllReadings method. I will store the extracted values from
                    // systolic and diastolic (mSystolicDiastolicString) into index from
                    // 0 to currentIndex

                    // Make a dummy systolicDiastolicString, 38 characters long.
                    //mSystolicDiastolicString = "12008007702/15 12:46 PM---------------";

                    mWeeklyReadingKeeper.setSystolicDiastolicString(mSystolicDiastolicString);

                    mWeeklyReadingKeeper.reloadArray(mSystolicDiastolicString, mIndex);

                    mWeeklyReadingKeeper.setAReading(mSystolic, mDiastolic, mPulse, getCurrentDateAndTime(), mDescription);

                    // Store the last reading on a SharedPreferences file so that it can be
                    // displayed even after the app is stopped.

                    mIndex = mWeeklyReadingKeeper.getIndex();

                    mWeeklyReadings = mWeeklyReadingKeeper.getAllReadings();

                    mSystolicDiastolicString = mWeeklyReadingKeeper.getAllSystolicDiastolic();

                    // Testing testing. Could I put data into displayArray here?

                    for(int i=0; i<mIndex; i++) {
                        String temp = "";
                        for (int j = 37*i; j <= ((37*i)+36); j++) {


                            char c = mWeeklyReadings.charAt(j);
                            if (c != '.') {
                                temp = temp + c;
                            }


                            displayArray[i] = temp;


                        }
                    }

                    displayArray[mIndex]=mWeeklyReadingKeeper.showAverage(mIndex);

                    sharedPreferences = MainActivity.this
                            .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(getString(R.string.SYSTOLIC_READING), mSystolic);
                    editor.putInt(getString(R.string.DIASTOLIC_READING), mDiastolic);
                    editor.putInt(getString(R.string.READING_COUNT), mIndex);
                    editor.putString(getString(R.string.WEEKLY_READINGS), mWeeklyReadings);
                    editor.putString(getString(R.string.SYSTOLIC_DIASTOLIC_STRING), mSystolicDiastolicString);
                    editor.putString(getString(R.string.LANGUAGE_PREF), mLanguagePreference);
                    editor.commit();

                    // Display this stored reading in the edit texts for last systolic and diastolic
                    // Also, update the reading count (INDEX) and display it in readingCountEditText
                    lastSystolicEditText.setTextColor(Color.BLACK);
                    lastSystolicEditText.setText(mSystolic + "");

                    lastDiastolicEditText.setTextColor(Color.BLACK);
                    lastDiastolicEditText.setText(mDiastolic + "");

                    readingCountTextView.setText(mIndex + "");

                    // Play a beeping sound once the reading is successfully stored.

                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);


                    // Mark any high or low readings in red.

                    if (anySystolicAbnormalReadings(mSystolic)){
                        lastSystolicEditText.setTextColor(Color.RED);
                    }

                    if (anyDiastolicAbnormalReadings(mDiastolic)){
                        lastDiastolicEditText.setTextColor(Color.RED);
                    }

                    // Mark any high or low readings in red.

                    if (anySystolicAbnormalReadings(mSystolic)){
                        systolicEditText.setTextColor(Color.RED);
                    }

                    if (anyDiastolicAbnormalReadings(mDiastolic)){
                        diastolicEditText.setTextColor(Color.RED);
                    }


                }
            }
        });

        displayReadingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                Boolean isEmpty;
                //Get stored readings from SharedPreferences file
                SharedPreferences sharedPreferences = MainActivity.this
                        .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);

                mIndex = sharedPreferences.getInt(getString(R.string.READING_COUNT),0);
                mWeeklyReadings = sharedPreferences.getString(getString(R.string.WEEKLY_READINGS),"");
                mSystolicDiastolicString = sharedPreferences.getString(getString(R.string.SYSTOLIC_DIASTOLIC_STRING), "");



                // If the keeper is empty, alert the user.

                if(mWeeklyReadingKeeper.isEmpty(mIndex)){

                    isEmpty = true;
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);

                }

                else {


                    // Clear dummy values from the displayArray positions from displayArray[mIndex]
                    // to displayArray[6]

                    isEmpty = false;


                    for (int k = mIndex; k <= 7; k++) {
                        displayArray[k] = "";
                    }

                    // Reload the mWeeklyReadingKeeper with readings. I will refactor this code
                    // and have it in a method all by itself: reloadArray

                    // Re-fill the readings keeper array with the values previously stored
                    // in the mSystolicDiastolicString stored in the SharedPreferences file.
                    // I will use a method I called "reloadArray".
                    // Otherwise, we would get a Null Pointer Exception for trying to
                    // get values from an array with empty positions when we call the
                    // getAllReadings method. I will store the extracted values from
                    // systolic and diastolic (mSystolicDiastolicString) into index from
                    // 0 to currentIndex

                    //mWeeklyReadingKeeper.setSystolicDiastolicString(mSystolicDiastolicString);

                    mWeeklyReadingKeeper.reloadArray(mSystolicDiastolicString, mIndex);


                    for (int i = 0; i < mIndex; i++) {
                        String temp = "";
                        for (int j = 37 * i; j <= ((37 * i) + 36); j++) {


                            char c = mWeeklyReadings.charAt(j);
                            if (c != '.') {
                                temp = temp + c;
                            }


                            displayArray[i] = temp;


                        }
                    }

                    displayArray[mIndex] = mWeeklyReadingKeeper.showAverage(mIndex);


                    // Start a new Intent and transmit the weeklyReadings string to a new screen
                    // that displays the results on a list view.

                    // Also, transmit the displayArray that I have filled in with dummy data:
                    // "Day 1, Day 2, ...", via the Intent. When I get this to work, I will
                    // pass this array of strings to the ListView, and not the weekly_readings
                    // string.

                }

                // Get the average systolic and diastolic values for the keeper.

                avgSystolic = mWeeklyReadingKeeper.getAvgSystolic(mIndex);

                avgDiastolic = mWeeklyReadingKeeper.getAvgDiastolic(mIndex);

                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);

                    intent.putExtra(DAILY_READING, mWeeklyReadingKeeper.mReading);

                    intent.putExtra(INDEX, mIndex);

                    intent.putExtra(getString(R.string.AVG_SYS), avgSystolic);

                    intent.putExtra(getString(R.string.AVG_DIA), avgDiastolic);





                   // intent.putExtra(getString(R.string.display_array), displayArray);
                    //intent.putExtra(getString(R.string.is_empty), isEmpty);
                    startActivity(intent);
                //}

            }
        });

        resetUIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                systolicEditText.requestFocus();
                systolicEditText.setTextColor(Color.BLACK);
                diastolicEditText.setTextColor(Color.BLACK);
                systolicEditText.setText("");
                diastolicEditText.setText("");
                pulseEditText.setText("");
                descriptionEditText.setText("");

            }
        });

        clearWeeklyReadingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(getString(R.string.clear_readings_confirm_message));
                builder.setMessage(getString(R.string.clear_readings_confirm_question));

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do do my action here
                        // Reset index back to zero and initialize array. This is done in
                        // clearAllReadings of Keeper.java

                        mWeeklyReadingKeeper.clearAllReadings();

                        // Clear all EditTexts
                        lastSystolicEditText.setText("");
                        lastDiastolicEditText.setText("");
                        readingCountTextView.setText("");
                        systolicEditText.setText("");
                        diastolicEditText.setText("");
                        pulseEditText.setText("");
                        descriptionEditText.setText("");
                        systolicEditText.requestFocus();

                        displayArray[0] = "";
                        displayArray[1] = "";
                        displayArray[2] = "";
                        displayArray[3] = "";
                        displayArray[4] = "";
                        displayArray[5] = "";
                        displayArray[6] = "";
                        displayArray[7] = "";
                        mIndex=0;

                        SharedPreferences sharedPreferences = MainActivity.this
                                .getSharedPreferences(getString(R.string.BP_PREF_FILE), MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();



            }
        });

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });



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

    private void setLocal(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.english:
                if (item.isChecked())
                    item.setChecked(false);
                else item.setChecked(true);
                mLanguagePreference="en";
                setLocal(mLanguagePreference);
                return true;
            case R.id.espanol:
                if (item.isChecked())
                    item.setChecked(false);
                else item.setChecked(true);
                mLanguagePreference="es";
                setLocal(mLanguagePreference);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        if (backPressToExit) {
            super.onBackPressed();
        }
        this.backPressToExit = true;
        Toast.makeText(this, getString(R.string.on_back_pressed_alert), Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {


            @Override


            public void run() {


                backPressToExit = false;
            }
        }, 2000);
    }

    public String getCurrentDateAndTime(){

        Calendar ci = Calendar.getInstance();

        String am_pm = "";
        if(ci.get(Calendar.AM_PM)==0){
            am_pm = "AM";
        }

        else{
            am_pm = "PM";
        }

        // Add one to the number of the month, because in Java, January is represented
        // using zero.

        String formattedMonth = String.format("%02d", ci.get(Calendar.MONTH)+1 );
        String formattedDay = String.format("%02d", ci.get(Calendar.DAY_OF_MONTH));
        String formattedHour = String.format("%02d", ci.get(Calendar.HOUR));
        String formattedMinute = String.format("%02d", ci.get(Calendar.MINUTE));

        // If the hour is between 12:00 and 12:59, Java puts 00:00 - 00:59. Convert the 00 to 12:

        if(ci.get(Calendar.HOUR)==0){
            formattedHour = "12";
        }

        String ciMonthDayTime = formattedMonth + "/" + formattedDay

                + " " + formattedHour  + ":" + formattedMinute  + " " + am_pm;

        return ciMonthDayTime;


    }

    public String getRecommendations(String systolicBPStatus, String diastolicBPStatus) {

        if((systolicBPStatus.equals("high")&&(diastolicBPStatus.equals("low")))||(systolicBPStatus.equals("low")&&(diastolicBPStatus.equals("high")))){

            mRecommendations = "Your systolic is high and your diastolic low, or your systolic is low" +
                    " and your diastolic is high. This is a very unusual and dangerous combination. Please" +
                    " see your primary care physician and/or cardiologist immediately!";

        }

        else if(systolicBPStatus.equals("low")||(diastolicBPStatus.equals("low"))){

            mRecommendations = "Treatment depends on the type of hypotension you have and the severity of your signs and symptoms. The goal of treatment is to bring blood pressure back to normal to relieve signs and symptoms. Another goal is to manage any underlying condition causing the hypotension.\n" +
                    "\n" +
                    "Your response to treatment depends on your age, overall health, and strength. It also depends on how easily you can stop, start, or change medicines.\n" +
                    "\n" +
                    "In a healthy person, low blood pressure without signs or symptoms usually isn't a problem and needs no treatment.\n" +
                    "\n" +
                    "If you have signs or symptoms of hypotension, you should sit or lie down right away. Put your feet above the level of your heart. If your signs or symptoms don't go away quickly, you should seek medical care.\n" +
                    "\n" +
                    "Orthostatic Hypotension\n" +
                    "\n" +
                    "Many treatments are available for orthostatic hypotension. If you have this condition, your doctor may advise making lifestyle changes, such as:\n" +
                    "\n" +
                    "Drinking plenty of fluids, such as water or sports drinks that contain nutrients like sodium and potassium.\n" +
                    "Drinking little or no alcohol.\n" +
                    "Standing up slowly.\n" +
                    "Not crossing your legs while sitting.\n" +
                    "Slowly increasing the amount of time you sit up if you've been immobile for a long time because of a medical condition. The term \"immobile\" refers to not being able to move around very much.\n" +
                    "Eating small, low-carbohydrate meals if you have postprandial hypotension (a form of orthostatic hypotension).\n" +
                    "Talk with your doctor about using compression stockings. These stockings apply pressure to your lower legs. The pressure helps move blood throughout your body.\n" +
                    "\n" +
                    "If medicine is causing your low blood pressure, your doctor may change the medicine or adjust the dose you take.\n" +
                    "\n" +
                    "Several medicines are used to treat orthostatic hypotension. These medicines, which raise blood pressure, include fludrocortisone and midodrine.\n" +
                    "\n" +
                    "Neurally Mediated Hypotension\n" +
                    "\n" +
                    "If you have neurally mediated hypotension (NMH), you may need to make lifestyle changes. These may include:\n" +
                    "\n" +
                    "Avoiding situations that trigger symptoms, such as standing for long periods. Unpleasant, upsetting, or scary situations also can trigger symptoms.\n" +
                    "Drinking plenty of fluids, such as water or sports drinks that contain nutrients like sodium and potassium.\n" +
                    "Increasing your salt intake (as your doctor advises).\n" +
                    "Learning to recognize symptoms that occur before fainting and taking action to raise your blood pressure. For example, sitting down and putting your head between your knees or lying down can help raise blood pressure.\n" +
                    "If medicine is causing your hypotension, your doctor may change the medicine or adjust the dose you take. He or she also may prescribe medicine to treat NMH.\n" +
                    "\n" +
                    "Severe Hypotension Linked to Shock\n" +
                    "\n" +
                    "Shock is a life-threatening emergency. People who have shock need prompt treatment from medical personnel. Call 9–1–1 right away.\n" +
                    "\n" ;
                    //  + "References: http://www.ncbi.nlm.nih.gov/pubmedhealth/PMH0063034/";

        }

        else if(systolicBPStatus.equals("high")||(diastolicBPStatus.equals("high"))){

            mRecommendations = "Monitoring your blood pressure is not a treatment for high blood" +
                    " pressure. To help treat your high blood pressure, you may need to eat a " +
                    "healthy diet, maintain a healthy weight, get enough exercise, and not smoke. " +
                    "Your doctor may also suggest medicines to help lower your blood pressure. " +
                    "Regularly measuring your blood pressure is just one part of a plan to lower " +
                    "your high blood pressure." + "\n\n" +
                    "REFERENCES: http://www.ncbi.nlm.nih.gov/pubmedhealth/PMH0041082/#conssmbp.s3";
        }

        else if(systolicBPStatus.equals("normal")&&(diastolicBPStatus.equals("normal"))){

            mRecommendations = "In order to maintain a normal blood pressure, follow these recommendations:\n " + "\n" +
                    "Eat a low-salt diet. Always check your food labels for sodium intake.\n" +
                    "Do not smoke.\n" +
                    "Get plenty of sleep.\n" +
                    "Eat a balanced diet, low in saturated fat and high in fiber, fruits and vegetables.\n" +
                    "Avoid stressful situations or a stressful lifestyle.";

        }

        return mRecommendations;
    }


}