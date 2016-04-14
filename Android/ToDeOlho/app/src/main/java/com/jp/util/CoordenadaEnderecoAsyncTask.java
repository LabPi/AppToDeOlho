package com.jp.util;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jp on 11/04/16.
 */
public class CoordenadaEnderecoAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private String resposta;
    private double latitude;
    private double longitude;


    public CoordenadaEnderecoAsyncTask(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        WebService webService = new WebService("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true");
        resposta = webService.get()[1];
        if(!resposta.equals("erro"))
        {
            try {
                JSONObject json = new JSONObject(resposta);
                if(json.getString("status").equals("OK"))
                {
                    JSONArray jsonArray = json.getJSONArray("results");
                    JSONObject json2 = jsonArray.getJSONObject(0);
                    resposta = json2.getString("formatted_address");
                }
                else
                {
                    resposta = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            resposta = "";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
