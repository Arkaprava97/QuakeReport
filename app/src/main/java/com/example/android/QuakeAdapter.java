package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Arkaprava on 28-12-2017.
 */

public class QuakeAdapter extends ArrayAdapter<Quake> {
    private static final String LOG_TAG = QuakeAdapter.class.getSimpleName();
    public  QuakeAdapter(Activity context, List<Quake> earthquakes) {
        super(context, 0, earthquakes);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Quake currentQuake =getItem(position);
        TextView magtextView= (TextView) listItemView.findViewById(R.id.magnitude);
        DecimalFormat decimalFormatter =new DecimalFormat("0.0");
        String mag =decimalFormatter.format(currentQuake.getMag());
        GradientDrawable magnitudeCircle =(GradientDrawable) magtextView.getBackground();
        int magnitudeColor =getmagnitudeColor(currentQuake.getMag());
        magnitudeCircle.setColor(magnitudeColor);
        magtextView.setText(mag);
        TextView loctextView= (TextView)listItemView.findViewById(R.id.locationOffset);
        String loc=currentQuake.getLocation();
        int pos =loc.indexOf("of");
        if(pos==-1)
        {
            loctextView.setText("Near the");
        }
        else
        {
            loctextView.setText(loc.substring(0,pos+2));
        }
        TextView loc2TextView =(TextView)listItemView.findViewById(R.id.Primarylocation);
        loc2TextView.setText(loc.substring(pos +2));
        //create date obj
        Date date1=new Date(currentQuake.getTimeInMilliseconds());
        TextView datetextView =(TextView)listItemView.findViewById(R.id.date);
        datetextView.setText(getDate(date1));
        TextView timetextView =(TextView)listItemView.findViewById(R.id.time);
        timetextView.setText(getTime(date1));
        return listItemView;
    }
    public String getDate(Date date)
    {
        //for day
        SimpleDateFormat formatter=new SimpleDateFormat("MMM DD,yy");
        String dateToDisplay = formatter.format(date);
        return dateToDisplay;
    }
    public  String getTime(Date date)
    {
        //for time and AM/PM
        SimpleDateFormat formatter2= new SimpleDateFormat("h:mm a");
        String time =formatter2.format(date);
        return  time;
    }
    private int getmagnitudeColor(double Magnitude)
    {
        int magnitudeColorResourceId;
        int MagnitudeFloor =(int)Math.floor(Magnitude);
        switch (MagnitudeFloor)
        {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }
}
