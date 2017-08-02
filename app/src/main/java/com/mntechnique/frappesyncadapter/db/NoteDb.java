package com.mntechnique.frappesyncadapter.db;

import android.content.ContentProviderClient;
import android.content.Context;
import android.os.RemoteException;

import com.mntechnique.frappesyncadapter.domain.Note;

import java.util.List;

import za.co.cporm.model.CPSyncHelper;
import za.co.cporm.model.query.Select;

/**
 * Created by revant on 30/7/17.
 */

public class NoteDb {

    private Context mCtx;
    private ContentProviderClient mProvider;

    public NoteDb(Context ctx, ContentProviderClient provider) {
        this.mProvider = provider;
        this.mCtx = ctx;
    }

    public List<Note> fetchAllNotes(){
        return Select.from(Note.class).queryAsList();
    }

    public Long createNote(Note localDataInstance) {
        try {
            Note n = new Note(localDataInstance);
            CPSyncHelper.insert(mCtx, mProvider, n);
            return n.getId();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long updateNote(Note localDataInstance) throws RemoteException {
        Note retrievedNote = Select.from(Note.class).whereEquals("_id", localDataInstance.getId()).first();
        //TODO Update note

        CPSyncHelper.update(mCtx, mProvider, retrievedNote);
        return retrievedNote.getId();
    }
    public Note fetchNote(Long id){
        Note retrievedNote = Select.from(Note.class).whereEquals("_id", id).first();
        return retrievedNote;
    }
}
