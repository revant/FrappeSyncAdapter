package com.mntechnique.frappesyncadapter.sync;

/**
 * Created by revant on 30/7/17.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NoteSyncAdapterService extends Service {
    private NoteSyncAdapter mSyncAdapter = null;

    private static final Object mSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (mSyncAdapterLock) {
            if(mSyncAdapter == null) {
                mSyncAdapter = new NoteSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }

}
