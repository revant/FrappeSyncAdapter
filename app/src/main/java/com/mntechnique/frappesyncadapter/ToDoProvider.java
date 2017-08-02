package com.mntechnique.frappesyncadapter;

import android.content.Context;
import android.content.pm.ProviderInfo;

import java.lang.reflect.InvocationTargetException;

import za.co.cporm.model.CPOrmConfiguration;
import za.co.cporm.model.CPOrmDatabase;
import za.co.cporm.provider.CPOrmContentProvider;
import za.co.cporm.provider.util.UriMatcherHelper;

/**
 * Created by revant on 2/8/17.
 */

public class ToDoProvider extends CPOrmContentProvider {
    protected CPOrmConfiguration cPOrmConfiguration;
    protected CPOrmDatabase database;
    protected boolean debugEnabled;

    @Override
    public boolean onCreate() {

        try {
            String className = Util.getMetaDataString(getContext(), "TODO_PROVIDER");
            Class modelFactory = Class.forName(className);
            cPOrmConfiguration = (CPOrmConfiguration) modelFactory.getConstructor().newInstance();
            database = new CPOrmDatabase(getContext(), cPOrmConfiguration);
            debugEnabled = cPOrmConfiguration.isQueryLoggingEnabled();
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
