package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    private static final String LOG_TAG =QueryUtils.class.getSimpleName();
 /*
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static List<Quake> fetchEarthQuakeData(String requestUrl)
    {
         Log.e(LOG_TAG,"fetchEarthquakeData()");
        //Create URL object
        URL url=createURL(requestUrl);
        //Perform HTTP request to the URL and receive JSONresponse back
        String JSONresponse =null;
        try {
            JSONresponse =makeHTTPRequest(url);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Error closing Input Stream",e);
        }
        List<Quake> quakes= extractFeatureFromJSON(JSONresponse);
        return  quakes;
    }
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }
    //Make an HTTP request to the given URL and return a String as the response.
    private static String makeHTTPRequest(URL url) throws IOException
    {
        String jsonresponse = null;
        //If url is  null then return early.
        if(url == null)
            return jsonresponse;
        HttpURLConnection connection= null;
        InputStream inputStream=null;
        try {
            connection= (HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000/*millisecs*/);
            connection.setConnectTimeout(15000/*millisecs*/);
            connection.setRequestMethod("GET");
            connection.connect();
            //If request was successful(response code 200)
            //then read inputStream and parse the response
            if(connection.getResponseCode()==200)
            {
                inputStream = connection.getInputStream();
                jsonresponse = readFromStream(inputStream);
            }
            else
                Log.e(LOG_TAG,"Error response code"+connection.getResponseCode());
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Problem retrieving earthquake JSON results");
        }
        finally {
            if(connection !=null)
                connection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        return jsonresponse;
    }
    private static String readFromStream(InputStream inputStream)throws IOException
    {
        StringBuilder output =new StringBuilder();
        if(inputStream !=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader br= new BufferedReader(inputStreamReader);
            String line =br.readLine();
            while (line!=null)
            {
                output.append(line);
                line=br.readLine();
            }
        }
        return output.toString();
    }
    private static List<Quake> extractFeatureFromJSON(String earthquakeJSON)
    {
        List<Quake> earthquakes = new ArrayList<Quake>();
        if(TextUtils.isEmpty(earthquakeJSON))
            return  null;
        try {
            JSONObject baseJSONResponse = new JSONObject(earthquakeJSON);
            JSONArray featureArray = baseJSONResponse.getJSONArray("features");
            // build up a list of Earthquake objects with the corresponding data.
            for (int i=0;i<featureArray.length();i++)
            {
                JSONObject jsonObject= featureArray.getJSONObject(i);
                JSONObject properties = jsonObject.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                long timeinmillisecs = properties.getLong("time");
                String location= properties.getString("place");
                Quake ob =new Quake(location,mag,timeinmillisecs);
                earthquakes.add(ob);
            }
        }catch (JSONException e)
        {
            Log.e(LOG_TAG,"Problem parsing the JSON response",e);
        }
        return earthquakes;
    }

    /**
     * Return a list of  objects that has been built up from
     * parsing a JSON response.
     */
   /* public static ArrayList<Quake> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Quake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string
            JSONObject jsonObject= new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features= jsonObject.getJSONArray("features");
            // build up a list of Earthquake objects with the corresponding data.
            for(int i =0;i<features.length();i++)
            {
                JSONObject ob = features.getJSONObject(i);
                JSONObject properties = ob.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String location = properties.getString("place");
                long timeinmillisec = properties.getLong("time");
                Quake temp =new Quake(location,mag,timeinmillisec);
                earthquakes.add(temp);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }*/

}

