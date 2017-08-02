package com.mntechnique.frappesyncadapter.api;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mntechnique.frappesyncadapter.R;
import com.mntechnique.frappesyncadapter.domain.ToDo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FrappeToDoApi {
    public static final String TAG = "VolleyPatterns";
    static int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    public static final int MAX_CACHE_SIZE = 2 * 1024 * 1024; //2 MB
    public static final int SERIAL = 1;
    public static final int PARALLEL = 0;

    private RequestQueue mRequestQueue;
    private RequestQueue mSerialRequestQueue;
    private Context mContext;
    private String bearerToken;

    public ToDo get(String remoteId) {
        RequestFuture<String> future = makeSyncRequest(SERIAL, Request.Method.GET,
                getServerURL() + "/api/resource/ToDo"+remoteId, new JSONObject(),
                bearerToken);
        ToDo note = new ToDo();
        try {
            String response = future.get();
            Log.d("futureRequest - >", response);
            JSONObject jsonResp = new JSONObject(response);
            note.setServerId(jsonResp.getString("name"));
            note.setTitle(jsonResp.getString("name"));
            note.setPosition(jsonResp.getString("idx"));
            note.setDescription(jsonResp.getString("content"));
        } catch (JSONException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return note;
    }

    public List<ToDo> get() {
        RequestFuture<String> future = makeSyncRequest(SERIAL, Request.Method.GET,
                getServerURL() + "/api/resource/ToDo?fields=[\"*\"]", new JSONObject(),
                bearerToken);
        List<ToDo> notes = new ArrayList<ToDo>();
        try {
            String response = future.get();
            Log.d("futureRequest - >", response);
            JSONObject jsonResp = new JSONObject(response);
            JSONArray rToDos = jsonResp.getJSONArray("data");
            for(int i = 0; i < rToDos.length(); i++){
                ToDo n = new ToDo();
                n.setServerId(rToDos.getJSONObject(i).getString("name"));
                n.setTitle(rToDos.getJSONObject(i).getString("name"));
                n.setPosition(rToDos.getJSONObject(i).getString("idx"));
                n.setDescription(rToDos.getJSONObject(i).getString("description"));
                notes.add(n);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("LoggingToDosb4ret", notes.toString());
        return notes;
    }

    public ToDo post(ToDo note) {
        return null;
    }

    public ToDo put(ToDo note) {
        return null;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public RequestQueue getSerialRequestQueue() {
        if (mSerialRequestQueue == null) {
            mSerialRequestQueue = prepareSerialRequestQueue(mContext);
            mSerialRequestQueue.start();
        }
        return mSerialRequestQueue;
    }

    private static RequestQueue prepareSerialRequestQueue(Context context) {
        Cache cache = new DiskBasedCache(context.getCacheDir(), MAX_CACHE_SIZE);
        Network network = getNetwork();
        return new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
    }

    private static Network getNetwork() {
        HttpStack stack;
        stack = new HurlStack();
        return new BasicNetwork(stack);
    }

    public RequestFuture<String> makeSyncRequest(int requestType, int verb, String endpointURL,
                                                 final JSONObject data, final String bearerToken){
        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest req = new StringRequest(verb, endpointURL,
                future,
                future){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                JSONObject bToken = new JSONObject();
                try {
                    bToken = new JSONObject(bearerToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String,String> params = new HashMap<String, String>();
                try {
                    Log.d("LOGTOKEN", bToken.getString("access_token"));
                    params.put("Authorization", "Bearer " + bToken.getString("access_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("data", data.toString());
                return params;
            }
        };

        if(requestType == SERIAL){
            getSerialRequestQueue().add(req);
        } else if (requestType == PARALLEL){
            getRequestQueue().add(req);
        }

        return future;
    }

    // Constructors
    public String getServerURL() {
        return mContext.getResources().getString(R.string.serverURL);
    }

    public FrappeToDoApi(Context context, String bearerToken) {
        this.mContext = context;
        this.bearerToken = bearerToken;
    }

    public FrappeToDoApi(Context context) {
        this.mContext = context;
    }

}
