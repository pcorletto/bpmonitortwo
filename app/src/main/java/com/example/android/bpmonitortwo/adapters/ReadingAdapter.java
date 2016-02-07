package com.example.android.bpmonitortwo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bpmonitortwo.R;
import com.example.android.bpmonitortwo.model.Reading;

/**
 * Created by hernandez on 2/4/2016.
 */
public class ReadingAdapter extends BaseAdapter {

    private Context mContext;
    private Reading[] mReadings;
    private int position;

    public ReadingAdapter(Context context, Reading[] readings, int index){
        mContext = context;
        mReadings = readings;
        position = index;

    }

    @Override
    public int getCount() {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mReadings[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.reading_list_item, null);

            holder = new ViewHolder();
            holder.dateTimeLabel = (TextView) convertView.findViewById(R.id.dateTimeLabel);
            holder.systolicLabel = (TextView) convertView.findViewById(R.id.systolicLabel);
            holder.diastolicLabel = (TextView) convertView.findViewById(R.id.diastolicLabel);
            holder.systolicStatusImageView = (ImageView) convertView.findViewById(R.id.systolicStatusImageView);
            holder.diastolicStatusImageView = (ImageView) convertView.findViewById(R.id.diastolicStatusImageView);
            convertView.setTag(holder);
        }

        else{
            // We have these views set up.
            holder = (ViewHolder) convertView.getTag();
        }

        // Now, set the data:

        Reading reading = mReadings[position];

        holder.dateTimeLabel.setText(reading.getDateAndTime());

        // Systolic section:
        holder.systolicLabel.setText(reading.getSystolic()+"");



        holder.systolicStatusImageView.setImageResource(reading.getSystolicIconId());

        // Diastolic section:
        holder.diastolicLabel.setText(reading.getDiastolic()+"");

        holder.diastolicStatusImageView.setImageResource(reading.getDiastolicIconId());



        return convertView;
    }

    private static class ViewHolder{
        TextView dateTimeLabel;
        TextView systolicLabel;
        TextView diastolicLabel;
        ImageView systolicStatusImageView;
        ImageView diastolicStatusImageView;




    }




}
