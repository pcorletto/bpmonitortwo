package com.example.android.bpmonitortwo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bpmonitortwo.R;

public class DetailsActivity extends ActionBarActivity {

    private TextView dateTimeTextView, systolicTextView, diastolicTextView, pulseTextView,
         systolicStatusTextView, diastolicStatusTextView, commentsTextView;
    private Button returnBackMainBtn;

    private String mDateTime, mSystolicBPStatus, mDiastolicBPStatus, mComments;
    private int mSystolic, mDiastolic, mPulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        systolicTextView = (TextView) findViewById(R.id.systolicTextView);
        diastolicTextView = (TextView) findViewById(R.id.diastolicTextView);
        pulseTextView = (TextView) findViewById(R.id.pulseTextView);
        systolicStatusTextView = (TextView) findViewById(R.id.systolicStatusTextView);
        diastolicStatusTextView = (TextView) findViewById(R.id.diastolicStatusTextView);
        commentsTextView = (TextView) findViewById(R.id.commentsTextView);
        returnBackMainBtn = (Button) findViewById(R.id.returnBackMainBtn);

        // Get the values passed in via Intent from the DisplayActivity

        Intent intent = getIntent();
        mDateTime = intent.getStringExtra(getString(R.string.date_time_label));
        mSystolic = intent.getIntExtra(getString(R.string.systolic_label), 0);
        mDiastolic = intent.getIntExtra(getString(R.string.diastolic_label), 0);
        mPulse = intent.getIntExtra(getString(R.string.pulse_label), 0);
        mSystolicBPStatus = intent.getStringExtra(getString(R.string.systolic_status_label));
        mDiastolicBPStatus = intent.getStringExtra(getString(R.string.diastolic_status_label));
        mComments = intent.getStringExtra(getString(R.string.comments_label));

        dateTimeTextView.setText(mDateTime);
        systolicTextView.setText(mSystolic+"");
        diastolicTextView.setText(mDiastolic+"");
        pulseTextView.setText(mPulse+"");
        systolicStatusTextView.setText(mSystolicBPStatus);
        diastolicStatusTextView.setText(mDiastolicBPStatus);
        commentsTextView.setText(mComments);

        returnBackMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
