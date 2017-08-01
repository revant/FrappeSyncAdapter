package com.mntechnique.frappesyncadapter.domain;

import com.mntechnique.synclib.Syncable;

import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.Table;

@Table
public class Note extends CPDefaultRecord<Note> implements Syncable {

    // fields
    @Column
    private long updated;

    @Column
    private String title;

    @Column
    private String serverId;

    @Column
    private String position;

    @Column
    private String content;

    @Column
    private boolean deleted;

    // getters

    public long getUpdated() {
        return updated;
    }

    public String getTitle() {
        return title;
    }

    public String getServerId() {
        return serverId;
    }

    public String getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }

    public boolean isDeleted() {
        return deleted;
    }

    // setters
    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String getRemoteId() {
        return this.serverId;
    }

    @Override
    public void setRemoteId(String id) {
        this.serverId = id;
    }

    @Override
    public Long getLastUpdatedSequence() {
        return this.updated;
    }

    @Override
    public void setLastUpdatedSequence(Long value) {
        this.updated = value;
    }

    @Override
    public void mapFromRemote(Syncable remote) {
        Note remoteNote = (Note) remote;

        setTitle(remoteNote.getTitle());
        setContent(remoteNote.getContent());

        // server read only
        setPosition(remoteNote.getPosition());
        setServerId(remote.getRemoteId());
        setLastUpdatedSequence(remote.getLastUpdatedSequence());
        setDeleted(remoteNote.isDeleted());
    }

    @Override
    public void mapFromLocal(Syncable local) {
        Note localNote = (Note) local;
        setTitle(localNote.getTitle());
        setContent(localNote.getContent());
        setDeleted(localNote.isDeleted());
    }

    @Override
    public String toString(){
        return "Note [id=" + _id + ", updated=" + updated + ", title=" + title
                + ", serverId=" + serverId + ", position=" + position
                + ", content=" + content + ", deleted="+deleted+"]";
    }

    //constructors
    public Note(){
        super();
    }

    public Note(Note note){
        this.updated = note.getUpdated();
        this.title = note.getTitle();
        this.serverId = note.getServerId();
        this.position = note.getPosition();
        this.content = note.getContent();
        this.deleted = note.isDeleted();
    }
}