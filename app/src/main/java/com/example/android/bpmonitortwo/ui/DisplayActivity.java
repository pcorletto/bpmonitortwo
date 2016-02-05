package com.example.android.bpmonitortwo.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        returnMainScreenButton = (Button) findViewById(R.id.returnMainButton);
        listView = (ListView) findViewById(android.R.id.list);


        Intent intent = getIntent();

        //String[] displayArray = intent.getStringArrayExtra(getString(R.string.display_array));

        Parcelable[] parcelables = intent.getParcelableArrayExtra(DAILY_READING);

        mIndex = intent.getIntExtra(INDEX, 0);

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





        returnMainScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
