package com.mntechnique.frappesyncadapter.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.mntechnique.frappesyncadapter.api.FrappeNoteApi;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by revant on 1/8/17.
 */

public class SyncThread extends AsyncTask<Void, Void, String> {
    private Context mContext;
    private String endpointURL;

    public SyncThread(Context ctx, String endpointURL) {
        this.mContext = ctx;
        this.endpointURL = endpointURL;
    }

    @Override
    protected String doInBackground(Void... params) {
        final RequestFuture<String> futureRequest = RequestFuture.newFuture();
        FrappeNoteApi api = new FrappeNoteApi(mContext);
        final StringRequest stringRequest = new StringRequest(Request.Method
                .GET, "http://httpbin.org/ip", futureRequest, futureRequest);
        stringRequest.setTag("TAGRR");
        RequestQueue mQ = api.getSerialRequestQueue();
        mQ.add(stringRequest);
        try {
            return futureRequest.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}