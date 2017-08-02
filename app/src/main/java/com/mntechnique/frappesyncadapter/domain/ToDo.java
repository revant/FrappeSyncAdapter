package com.mntechnique.frappesyncadapter.domain;

import com.mntechnique.synclib.Syncable;

import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.Table;

@Table
public class ToDo extends CPDefaultRecord<ToDo> implements Syncable {

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
    private String description;

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

    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
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
        ToDo remoteToDo = (ToDo) remote;

        setTitle(remoteToDo.getTitle());
        setDescription(remoteToDo.getDescription());

        // server read only
        setPosition(remoteToDo.getPosition());
        setServerId(remote.getRemoteId());
        setLastUpdatedSequence(remote.getLastUpdatedSequence());
        setDeleted(remoteToDo.isDeleted());
    }

    @Override
    public void mapFromLocal(Syncable local) {
        ToDo localToDo = (ToDo) local;
        setTitle(localToDo.getTitle());
        setDescription(localToDo.getDescription());
        setDeleted(localToDo.isDeleted());
    }

    @Override
    public String toString(){
        return "ToDo [id=" + _id + ", updated=" + updated + ", title=" + title
                + ", serverId=" + serverId + ", position=" + position
                + ", description=" + description + ", deleted="+deleted+"]";
    }

    //constructors
    public ToDo(){
        super();
    }

    public ToDo(ToDo todo){
        this.updated = todo.getUpdated();
        this.title = todo.getTitle();
        this.serverId = todo.getServerId();
        this.position = todo.getPosition();
        this.description = todo.getDescription();
        this.deleted = todo.isDeleted();
    }
}