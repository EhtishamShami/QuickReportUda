package com.example.android.quakereport;

/**
 * Created by Shami on 1/21/2017.
 */

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    private  static final String Log_Tag=QueryUtils.class.getSimpleName();



    private QueryUtils() {
    }


    public static URL createurl(String url,String minMagnitude,String orderBy)
    {
        URL myurl=null;

        try{
            Uri builtUri = Uri.parse(url)
                    .buildUpon()
                    .appendQueryParameter("format", "geojson")
                    .appendQueryParameter("eventtype", "earthquake")
                    .appendQueryParameter("orderby", "time")
                    .appendQueryParameter("minmag", minMagnitude)
                    .appendQueryParameter("limit","10")
                    .appendQueryParameter("orderby", orderBy)
                    .build();

            myurl=new URL(builtUri.toString());



        }catch(MalformedURLException exception)
        {
            Log.e(Log_Tag,"URl cannot be parsed "+exception);
        }

        return myurl;

    }

    public static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse="";

        if(url==null)
        {
            return jsonResponse;
        }
        HttpURLConnection connection=null;
        InputStream in=null;

        try{
           connection=(HttpURLConnection)url.openConnection();
           connection.setReadTimeout(20000 /* milliseconds */);
           connection.setConnectTimeout(20000);
           connection.setRequestMethod("GET");
           connection.connect();

           if(connection.getResponseCode()==200)
           {
               in=connection.getInputStream();
               jsonResponse=readfromstream(in);
           }

        }
        catch (IOException e)
        {
            Log.e(Log_Tag,"Error response code: " + connection.getResponseCode());
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {

                in.close();
            }
        }

        return jsonResponse;
    }


    private static String readfromstream(InputStream inputStream)throws IOException
    {
        StringBuilder stringBuilder=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null)
            {
                stringBuilder.append(line);
                line=reader.readLine();
            }
        }
        return stringBuilder.toString();

    }


    public static ArrayList<Earthquake> extractEarthquakes(String JSON_RESPONSE) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
                JSONObject object=new JSONObject(JSON_RESPONSE);
                JSONArray  features=object.getJSONArray("features");

                for(int i=0;i<features.length();i++)
                {
                    JSONObject data=features.getJSONObject(i);
                    JSONObject propeties=data.getJSONObject("properties");
                    Double mag=propeties.getDouble("mag");
                    String place=propeties.getString("place");
                    long date=propeties.getLong("time");
                    String url=propeties.getString("url");
                    earthquakes.add(new Earthquake(mag,place,date,url));
                }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}