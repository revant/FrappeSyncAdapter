package com.mntechnique.frappesyncadapter.sync;

/**
 * Created by revant on 30/7/17.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.mntechnique.frappesyncadapter.api.FrappeNoteApi;
import com.mntechnique.frappesyncadapter.db.NoteDb;
import com.mntechnique.frappesyncadapter.domain.Note;
import com.mntechnique.oauth2authenticator.auth.AccountGeneral;
import com.mntechnique.oauth2authenticator.auth.AuthReqCallback;
import com.mntechnique.oauth2authenticator.auth.RetrieveAuthTokenTask;
import com.mntechnique.synclib.SyncManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class NoteSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "NoteSyncAdapter";

    private Context mContext;

    public NoteSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContext = context;

    }

    public NoteSyncAdapter(Context context, boolean autoInitialize,
                           boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        final ContentProviderClient finalContentProviderClient = provider;
        Log.d(TAG, "onPerformSync");
        AccountManager am = AccountManager.get(mContext);
        String authToken = null;
        try {
            authToken = am.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
            //remote data store
            FrappeNoteApi api = new FrappeNoteApi(mContext, authToken);
            NoteSyncRemoteDatastore remoteDatastore = new NoteSyncRemoteDatastore(api);
            //local data store
            NoteDb db = new NoteDb(mContext, finalContentProviderClient);
            NoteSyncLocalDatastore localDatastore = new NoteSyncLocalDatastore(db);
            //sync!
            SyncManager<Note, Note> syncManager = new SyncManager<Note, Note>(localDatastore, remoteDatastore);
            syncManager.sync();
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }
    }
}
