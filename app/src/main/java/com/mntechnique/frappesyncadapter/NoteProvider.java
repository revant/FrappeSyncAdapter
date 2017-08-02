package com.mntechnique.frappesyncadapter;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import za.co.cporm.model.CPOrmConfiguration;
import za.co.cporm.model.CPOrmDatabase;
import za.co.cporm.model.util.ManifestHelper;
import za.co.cporm.provider.CPOrmContentProvider;
import za.co.cporm.provider.util.UriMatcherHelper;

/**
 * Created by revant on 2/8/17.
 */

public class NoteProvider extends CPOrmContentProvider {
    protected CPOrmConfiguration cPOrmConfiguration;
    protected CPOrmDatabase database;
    protected boolean debugEnabled;

    @Override
    public boolean onCreate() {
        cPOrmConfiguration = ManifestHelper.getConfiguration(getContext());
        database = new CPOrmDatabase(getContext(), cPOrmConfiguration);
        debugEnabled = cPOrmConfiguration.isQueryLoggingEnabled();
        return true;
    }
}
