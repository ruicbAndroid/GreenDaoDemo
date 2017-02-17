package com.ruicb.greendaotest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ruicb.greendaotest.database.helper.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ruichengbing on 2017/2/17.
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        switch (oldVersion){
            case 1:
                //更新字段
                MigrationHelper.getInstance().migrate(db, FaceDao.class);
            case 2:
            case 3:
            case 4:
                //新增表
                StickerDao.createTable(db, false);
            case 5:
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerDao.class);
            case 6:
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerDao.class);
            case 7:
                //新增表
                StickerBackgroundDao.createTable(db, false);
                StickerCategoryDao.createTable(db, false);
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerDao.class, FaceDao.class);
            case 8:
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerBackgroundDao.class);
            case 9:
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerBackgroundDao.class);
                //更新字段
                MigrationHelper.getInstance().migrate(db, StickerCategoryDao.class);
                //新增表
                StickerTextDao.createTable(db, false);
            case 10:
            case 11:
                //新增表
                StickerFontDao.createTable(db, false);
        }
    }
}
