package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */
@Entity
public class StickerBackground {
    @Id
    Long id;
    String backgroundPath;
    String backgroundColor;

    @Generated(hash = 1254200350)
    public StickerBackground(Long id, String backgroundPath,
            String backgroundColor) {
        this.id = id;
        this.backgroundPath = backgroundPath;
        this.backgroundColor = backgroundColor;
    }

    @Generated(hash = 141935415)
    public StickerBackground() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackgroundPath() {
        return this.backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
