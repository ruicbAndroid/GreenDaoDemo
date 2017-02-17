package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */
@Entity
public class StickerCategory {
    @Id
    Long id;
    String newRow;

    @Generated(hash = 1688055877)
    public StickerCategory(Long id, String newRow) {
        this.id = id;
        this.newRow = newRow;
    }

    @Generated(hash = 2109031487)
    public StickerCategory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewRow() {
        return this.newRow;
    }

    public void setNewRow(String newRow) {
        this.newRow = newRow;
    }
}
