/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    Context mContext;
    private URL mUrl;

    public EarthquakeLoader(Context context, URL url) {
        super(context);
        mContext=context;
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG,"onStartLoader");
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.e(LOG_TAG,"loadinBackground");
        if(mUrl == null)
        {
            return null;
        }
        String jsonResponse="";
        List<Earthquake> earthquakes=null;
        try{
            jsonResponse=QueryUtils.makeHttpRequest(mUrl);
            earthquakes = QueryUtils.extractEarthquakes(jsonResponse);

        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Error in making http request"+e );
        }

        return earthquakes;
    }
}