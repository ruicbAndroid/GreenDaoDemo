package com.ruicb.greendaotest.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STICKER_BACKGROUND".
*/
public class StickerBackgroundDao extends AbstractDao<StickerBackground, Long> {

    public static final String TABLENAME = "STICKER_BACKGROUND";

    /**
     * Properties of entity StickerBackground.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BackgroundPath = new Property(1, String.class, "backgroundPath", false, "BACKGROUND_PATH");
        public final static Property BackgroundColor = new Property(2, String.class, "backgroundColor", false, "BACKGROUND_COLOR");
    }


    public StickerBackgroundDao(DaoConfig config) {
        super(config);
    }
    
    public StickerBackgroundDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STICKER_BACKGROUND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"BACKGROUND_PATH\" TEXT," + // 1: backgroundPath
                "\"BACKGROUND_COLOR\" TEXT);"); // 2: backgroundColor
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STICKER_BACKGROUND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, StickerBackground entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String backgroundPath = entity.getBackgroundPath();
        if (backgroundPath != null) {
            stmt.bindString(2, backgroundPath);
        }
 
        String backgroundColor = entity.getBackgroundColor();
        if (backgroundColor != null) {
            stmt.bindString(3, backgroundColor);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, StickerBackground entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String backgroundPath = entity.getBackgroundPath();
        if (backgroundPath != null) {
            stmt.bindString(2, backgroundPath);
        }
 
        String backgroundColor = entity.getBackgroundColor();
        if (backgroundColor != null) {
            stmt.bindString(3, backgroundColor);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public StickerBackground readEntity(Cursor cursor, int offset) {
        StickerBackground entity = new StickerBackground( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // backgroundPath
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // backgroundColor
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, StickerBackground entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBackgroundPath(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBackgroundColor(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(StickerBackground entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(StickerBackground entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(StickerBackground entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
