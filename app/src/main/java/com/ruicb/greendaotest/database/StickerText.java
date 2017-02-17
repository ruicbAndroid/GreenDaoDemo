package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */
@Entity
public class StickerText {
    @Id
    Long id;

    @Generated(hash = 1795711366)
    public StickerText(Long id) {
        this.id = id;
    }

    @Generated(hash = 2145362652)
    public StickerText() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
