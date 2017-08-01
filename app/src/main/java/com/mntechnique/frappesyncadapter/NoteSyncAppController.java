package com.mntechnique.frappesyncadapter;

import android.app.Application;
import za.co.cporm.model.CPOrm;

/**
 * Created by revant on 30/7/17.
 */

public class NoteSyncAppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CPOrm.initialize(this);
    }
}
