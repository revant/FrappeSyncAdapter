package com.mntechnique.frappesyncadapter.sync;

import android.util.Log;

import com.mntechnique.frappesyncadapter.api.FrappeNoteApi;
import com.mntechnique.frappesyncadapter.domain.Note;
import com.mntechnique.synclib.Datastore;

import java.util.List;

/**
 * Created by revant on 31/7/17.
 */

public class NoteSyncRemoteDatastore implements Datastore<Note> {

    FrappeNoteApi mRemoteApi;
    String TAG = "RemoteStore";
    public NoteSyncRemoteDatastore (FrappeNoteApi api){
        super();
        this.mRemoteApi = api;
    }

    @Override
    public List<Note> get() {
        return mRemoteApi.get();
    }

    @Override
    public Note create() {
        return new Note();
    }

    @Override
    public Note add(Note item) {
        Log.d(TAG, "addRemote:"+item.toString());
        Note result = mRemoteApi.post(item);
        Log.d(TAG, "afterPost:"+result.toString());
        return result;
    }

    @Override
    public Note update(Note item) {
        Log.d(TAG, "updateRemote:"+item.toString());
        Note result = mRemoteApi.put(item);
        return result;
    }

}
