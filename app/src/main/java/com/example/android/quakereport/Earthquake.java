package com.example.android.quakereport;

/**
 * Created by Shami on 1/21/2017.
 */

public class Earthquake {

    private Double mMagnitude;
    private String mlocation;
    private long mtimeInMilliseconds;
    private String mUrl;

    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mlocation = location;
        mtimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public Double getmMagnitude()
    {

        return mMagnitude;
    }

    public String getlocation()
    {

        return mlocation;
    }

    public long getdate()
    {
        return mtimeInMilliseconds;
    }

    public String getUrl() {
        return mUrl;
    }

}
