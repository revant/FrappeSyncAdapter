package com.mntechnique.frappesyncadapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import za.co.cporm.model.CPOrmConfiguration;
import za.co.cporm.model.util.ManifestHelper;
import za.co.cporm.util.CPOrmLog;

/**
 * Created by revant on 2/8/17.
 */

public class Util {
    public static CPOrmConfiguration getConfiguration(Context context, String METADATA_CPORM_CONFIG) throws IllegalArgumentException{
        String className = getMetaDataString(context, METADATA_CPORM_CONFIG);
        try{
            Class modelFactory = Class.forName(className);
            if(CPOrmConfiguration.class.isAssignableFrom(modelFactory)){
                return (CPOrmConfiguration)modelFactory.getConstructor().newInstance();
            }
            else throw new IllegalArgumentException("The class provided is not and instance of CPOrmConfiguration: " + className);
        } catch (Exception ex){
            throw new IllegalArgumentException("Failed to create CPOrmConfiguration instance, is the meta data tag added to the application?", ex);
        }
    }
    public static String getMetaDataString(Context context, String name) {
        String value = null;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = ai.metaData.getString(name);
        } catch (Exception e) {
            CPOrmLog.d("Couldn't find config value: " + name);
        }

        return value;
    }
}
