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
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mntechnique.frappesyncadapter.R;
import com.mntechnique.frappesyncadapter.domain.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FrappeNoteApi {
    public static final String TAG = "VolleyPatterns";
    static int MAX_SERIAL_THREAD_POOL_SIZE = 1;
    public static final int MAX_CACHE_SIZE = 2 * 1024 * 1024; //2 MB
    public static final int SERIAL = 1;
    public static final int PARALLEL = 0;

    private RequestQueue mRequestQueue;
    private RequestQueue mSerialRequestQueue;
    private Context mContext;
    private String bearerToken;

	public Note get(String remoteId) {
        RequestFuture<String> future = makeSyncRequest(SERIAL, Request.Method.GET,
                getServerURL() + "/api/resource/Note"+remoteId, new JSONObject(),
                bearerToken);
        Note note = new Note();
        try {
            String response = future.get();
            Log.d("futureRequest - >", response);
            JSONObject jsonResp = new JSONObject(response);
            note.setServerId(jsonResp.getString("name"));
            note.setTitle(jsonResp.getString("name"));
            note.setPosition(jsonResp.getString("idx"));
            note.setContent(jsonResp.getString("content"));
        } catch (JSONException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return note;
    }

	public List<Note> get() {
        RequestFuture<String> future = makeSyncRequest(SERIAL, Request.Method.GET,
                getServerURL() + "/api/resource/Note?fields=[\"*\"]", new JSONObject(),
                bearerToken);
        List<Note> notes = new ArrayList<Note>();
        try {
            String response = future.get();
            Log.d("futureRequest - >", response);
            JSONObject jsonResp = new JSONObject(response);
            JSONArray rNotes = jsonResp.getJSONArray("data");
            for(int i = 0; i < rNotes.length(); i++){
                Note n = new Note();
                n.setServerId(rNotes.getJSONObject(i).getString("name"));
                n.setTitle(rNotes.getJSONObject(i).getString("name"));
                n.setPosition(rNotes.getJSONObject(i).getString("idx"));
                n.setContent(rNotes.getJSONObject(i).getString("content"));
                notes.add(n);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("LoggingNotesb4ret", notes.toString());
        return notes;
	}

	public Note post(Note note) {
		return null;
	}

	public Note put(Note note) {
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

    public void makeAPICall(int requestType, int verb, String endpointURL, final JSONObject data,
                            Response.Listener<String> stringListener,
                            Response.ErrorListener errorListener,
                            final String bearerToken) {

        VolleyLog.d("Endpoint URL", endpointURL);

        endpointURL = endpointURL.replace("+", "%20");

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest req = new StringRequest(verb, endpointURL,
                stringListener,
                errorListener){
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

    public FrappeNoteApi(Context context, String bearerToken) {
        this.mContext = context;
        this.bearerToken = bearerToken;
    }

    public FrappeNoteApi(Context context) {
        this.mContext = context;
    }

}
