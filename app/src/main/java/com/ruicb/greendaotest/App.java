package com.ruicb.greendaotest;

import android.app.Application;

import com.ruicb.greendaotest.database.DaoMaster;
import com.ruicb.greendaotest.database.DaoSession;
import com.ruicb.greendaotest.database.MyOpenHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ruichengbing on 2017/2/17.
 */

public class App extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        MyOpenHelper helper = new MyOpenHelper(this, "magic-sticker-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
