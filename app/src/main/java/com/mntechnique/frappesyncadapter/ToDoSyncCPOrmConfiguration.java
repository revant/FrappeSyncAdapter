package com.mntechnique.frappesyncadapter;

import com.mntechnique.frappesyncadapter.domain.Note;
import com.mntechnique.frappesyncadapter.domain.ToDo;

import java.util.ArrayList;
import java.util.List;

import za.co.cporm.model.CPOrmConfiguration;

/**
 * Created by revant on 31/7/17.
 */

public class ToDoSyncCPOrmConfiguration implements CPOrmConfiguration {
    @Override
    public String getDatabaseName() {
        return "todosync.db";
    }

    @Override
    public int getDatabaseVersion() {
        return 1;
    }

    @Override
    public boolean isQueryLoggingEnabled() {
        return false;
    }

    @Override
    public String upgradeResourceDirectory() {
        return null;
    }

    @Override
    public List<Class<?>> getDataModelObjects() {
        List<Class<?>> domainObjects = new ArrayList<Class<?>>();
        domainObjects.add(ToDo.class);
        return domainObjects;
    }
}
