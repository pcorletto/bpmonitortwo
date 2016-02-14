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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bpmonitortwo.R;
import com.example.android.bpmonitortwo.model.Keeper;
import com.example.android.bpmonitortwo.model.Reading;

import java.util.Calendar;
import java.util.Locale;



public class MainActivity extends ActionBarActivity {

    private RelativeLayout mRelativeLayout;
    private EditText lastSystolicEditText, lastDiastolicEditText,
            systolicEditText, diastolicEditText;
    private TextView readingCountTextView;
    private Button bpCheckButton, storeReadingButton, displayReadingsButton, resetUIButton,
            clearWeeklyReadingsButton, hideButton;
    private int mSystolic, mDiastolic;
    private String bpCheckStatus;
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

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        lastSystolicEditText = (EditText) findViewById(R.id.lastSystolicEditText);
        lastDiastolicEditText = (EditText) findViewById(R.id.lastDiastolicEditText);
        systolicEditText = (EditText) findViewById(R.id.systolicEditText);
        diastolicEditText = (EditText) findViewById(R.id.diastolicEditText);
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
                Toast.makeText(MainActivity.this, getString(R.string.your_blood_pressure_is) +
                        bpCheckStatus, Toast.LENGTH_LONG).show();

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

                    mWeeklyReadingKeeper.setSystolicDiastolicString(mSystolicDiastolicString);

                    mWeeklyReadingKeeper.reloadArray(mSystolicDiastolicString, mIndex);

                    mWeeklyReadingKeeper.setAReading(mSystolic, mDiastolic, getCurrentDateAndTime());

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


}