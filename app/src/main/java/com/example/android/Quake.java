package com.example.android.quakereport;

/**
 * Created by Arkaprava on 28-12-2017.
 */

public class Quake {
    private String location;
    private double mag;
    private long time;
    public Quake(String location,double mag,long time)
    {
        this.location=location;
        this.mag=mag;
        this.time=time;
    }
    public String getLocation()
    {return location;}
    public double getMag()
    {return mag;}
    public long getTimeInMilliseconds()
    {return  time;}
}
