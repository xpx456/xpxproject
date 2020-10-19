package com.accesscontrol.model;

import android.content.Context;

import com.accesscontrol.model.entities.DaoMaster;
import com.accesscontrol.model.entities.DaoSession;


public class DaoSessionManager {

    private final String DB_NAME = "ext.db";
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static DaoSessionManager daoSessionManager = new DaoSessionManager();
    public static DaoSessionManager getInstance() {
        return daoSessionManager;
    }

    public DaoMaster getDaoMaster(Context context) {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        daoMaster = new DaoMaster(mHelper.getWritableDatabase());
        return daoMaster;
    }

    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
