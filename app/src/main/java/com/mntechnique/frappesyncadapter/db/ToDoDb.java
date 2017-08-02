package com.mntechnique.frappesyncadapter.db;

import android.content.ContentProviderClient;
import android.content.Context;
import android.os.RemoteException;

import com.mntechnique.frappesyncadapter.domain.ToDo;

import java.util.List;

import za.co.cporm.model.CPSyncHelper;
import za.co.cporm.model.query.Select;

/**
 * Created by revant on 30/7/17.
 */

public class ToDoDb {

    private Context mCtx;
    private ContentProviderClient mProvider;

    public ToDoDb(Context ctx, ContentProviderClient provider) {
        this.mProvider = provider;
        this.mCtx = ctx;
    }

    public List<ToDo> fetchAllToDos(){
        return Select.from(ToDo.class).queryAsList();
    }

    public Long createToDo(ToDo localDataInstance) throws RemoteException {
        ToDo n = new ToDo(localDataInstance);
        CPSyncHelper.insert(mCtx, mProvider, n);
        return n.getId();
    }

    public Long updateToDo(ToDo localDataInstance) throws RemoteException {
        ToDo retrievedToDo = Select.from(ToDo.class).whereEquals("_id", localDataInstance.getId()).first();
        //TODO Update note

        CPSyncHelper.update(mCtx, mProvider, retrievedToDo);
        return retrievedToDo.getId();
    }
    public ToDo fetchToDo(Long id){
        ToDo retrievedToDo = Select.from(ToDo.class).whereEquals("_id", id).first();
        return retrievedToDo;
    }
}
