package com.mntechnique.frappesyncadapter.sync;

import android.os.RemoteException;

import com.mntechnique.frappesyncadapter.db.NoteDb;
import com.mntechnique.frappesyncadapter.domain.Note;
import com.mntechnique.synclib.Datastore;

import java.util.List;

/**
 * Created by revant on 31/7/17.
 */

public class NoteSyncLocalDatastore implements Datastore<Note> {
    private NoteDb mDb;

    @Override
    public List<Note> get() {
        return mDb.fetchAllNotes();
    }

    @Override
    public Note create() {
        return new Note();
    }

    @Override
    public Note add(Note localDataInstance) {
        long id = 0;
        id = mDb.createNote(localDataInstance);
        Note result = mDb.fetchNote(id);
        return result;
    }

    @Override
    public Note update(Note localDataInstance) {
        try {
            mDb.updateNote(localDataInstance);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Note result = mDb.fetchNote(localDataInstance.getId());
        return result;
    }

    public NoteSyncLocalDatastore(NoteDb mDb) {
        this.mDb = mDb;
    }
}
