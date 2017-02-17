package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */
@Entity
public class StickerFont {
    @Id(autoincrement = true)
    Long id;

    @Generated(hash = 1903723285)
    public StickerFont(Long id) {
        this.id = id;
    }

    @Generated(hash = 1104147579)
    public StickerFont() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
