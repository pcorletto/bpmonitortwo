package com.example.android.bpmonitortwo.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
           holder.diastolicLabel.setText(mAvgDiastolic);






        listView.addFooterView(averageFooterView);








        Parcelable[] parcelables = intent.getParcelableArrayExtra(DAILY_READING);



        mReadings = Arrays.copyOf(parcelables, mIndex, Reading[].class);
        //mReadings = Arrays.copyOf(parcelables, parcelables.length, Reading[].class);




        // Display the weekly readings.

        //Toast.makeText(DisplayActivity.this, weeklyReadings, Toast.LENGTH_LONG).show();

        //String[] myStringArray = {weeklyReadings};

        // This code worked. Comment it out temporarily to test the passed displayArray.
        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);

        // Display dummy data from displayArray (Array of 7 strings): "Day 1, Day 2, ..." temporarily,
        // until it get it to work


        //listView.setEmptyView(findViewById(android.R.id.empty));

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

    }

}
