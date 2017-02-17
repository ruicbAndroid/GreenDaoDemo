package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */
@Entity
public class Sticker {
    @Id
    Long id;
    @NotNull
    String name;

    String path;
    String newRow;
    String localPath;
    String pwd;
    @Generated(hash = 1526967944)
    public Sticker(Long id, @NotNull String name, String path, String newRow,
            String localPath, String pwd) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.newRow = newRow;
        this.localPath = localPath;
        this.pwd = pwd;
    }
    @Generated(hash = 1542104920)
    public Sticker() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getNewRow() {
        return this.newRow;
    }
    public void setNewRow(String newRow) {
        this.newRow = newRow;
    }
    public String getLocalPath() {
        return this.localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
