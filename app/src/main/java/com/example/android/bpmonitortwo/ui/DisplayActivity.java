package com.example.android.bpmonitortwo.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bpmonitortwo.R;
import com.example.android.bpmonitortwo.adapters.ReadingAdapter;
import com.example.android.bpmonitortwo.model.Reading;

import java.util.Arrays;

import static com.example.android.bpmonitortwo.ui.MainActivity.DAILY_READING;
import static com.example.android.bpmonitortwo.ui.MainActivity.INDEX;

public class DisplayActivity extends ListActivity {

    private Button returnMainScreenButton;
    private ListView listView;
    private Reading[] mReadings;
    private int mIndex;
    private String mAvgSystolic;
    private String mAvgDiastolic;
    private String mSystolicBPStatus, mDiastolicBPStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        returnMainScreenButton = (Button) findViewById(R.id.returnMainButton);
        listView = (ListView) findViewById(android.R.id.list);

        // Added a footer to the ListView to display the average at the bottom of the list

        View averageFooterView = View.inflate(this, R.layout.footer_layout, null);
        ViewHolder holder = new ViewHolder();

           holder.averageLabel = (TextView) averageFooterView.findViewById(R.id.averageLabel);
           holder.systolicLabel = (TextView) averageFooterView.findViewById(R.id.systolicLabel);
           holder.systolicStatusImageView = (ImageView) averageFooterView.findViewById(R.id.systolicStatusImageView);
           holder.diastolicLabel = (TextView) averageFooterView.findViewById(R.id.diastolicLabel);
           holder.diastolicStatusImageView = (ImageView) averageFooterView.findViewById(R.id.diastolicStatusImageView);


        Intent intent = getIntent();

        mIndex = intent.getIntExtra(INDEX, 0);
        mAvgSystolic = intent.getStringExtra(getString(R.string.AVG_SYS));
        mAvgDiastolic = intent.getStringExtra(getString(R.string.AVG_DIA));


           // Set the AVERAGE data:

           holder.averageLabel.setText("AVERAGE:           ");

           holder.systolicLabel.setText(mAvgSystolic);
           holder.systolicStatusImageView.setImageResource(getSystolicIconId(mAvgSystolic));

           holder.diastolicLabel.setText(mAvgDiastolic);
           holder.diastolicStatusImageView.setImageResource(getDiastolicIconId(mAvgDiastolic));


        listView.addFooterView(averageFooterView);


        Parcelable[] parcelables = intent.getParcelableArrayExtra(DAILY_READING);



        mReadings = Arrays.copyOf(parcelables, mIndex, Reading[].class);


        // Add an OnItemClickListener to display more details about an individual reading displayed
        // on the ListView. For now, I'll just have it display the date and time when the reading
        // was taken, in a brief Toast message. I will create and open up a new Activity that displays
        // comments and/or any more information about the reading.

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DisplayActivity.this, "Reading taken on " + mReadings[position].getDateAndTime(),
                        Toast.LENGTH_LONG).show();

                Toast.makeText(DisplayActivity.this, "On that day you reported that " + mReadings[position].getDescription()
                                + "\n\n" + "Your pulse rate was " + mReadings[position].getPulse(),
                        Toast.LENGTH_LONG).show();
            }
        });



        // OnClickListener for "Store This Week's Readings" button

        holder.storeThisWeekReadings = (Button) findViewById(R.id.storeWeekReadingsBtn);

        holder.storeThisWeekReadings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mIndex<7){

                    // Display a Toast, because there are still not seven readings to store a week

                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_CONGESTION, 200);

                   Toast.makeText(DisplayActivity.this, "You still cannot store the week. It has been less than 7 readings! Store "
                           + (7 - mIndex) + " more.", Toast.LENGTH_LONG).show();

                }



            }
        });







        ReadingAdapter adapter = new ReadingAdapter(this, mReadings, mIndex);

        //ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
        // displayArray);



        listView.setAdapter(adapter);






        // Testing...







        // Test ends here.



        returnMainScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public final class ViewHolder{

        public TextView averageLabel;
        public TextView systolicLabel;
        public ImageView systolicStatusImageView;
        public TextView diastolicLabel;
        public ImageView diastolicStatusImageView;
        public Button storeThisWeekReadings;

    }

    public int getSystolicIconId(String avgSystolic) {

        int avgSystolicInt = Integer.parseInt(avgSystolic);

        //Initialize SystolicIconId to normal
        int mSystolicIconId = R.drawable.normal_reading;

        mSystolicBPStatus = getSystolicBPStatus(avgSystolicInt);

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

    public int getDiastolicIconId(String avgDiastolic) {

        int avgDiastolicInt = Integer.parseInt(avgDiastolic);

        //Initialize DiastolicIconId to normal
        int mDiastolicIconId = R.drawable.normal_reading;

        mDiastolicBPStatus = getDiastolicBPStatus(avgDiastolicInt);

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

}
