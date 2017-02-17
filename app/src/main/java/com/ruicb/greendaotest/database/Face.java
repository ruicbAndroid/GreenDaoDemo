package com.ruicb.greendaotest.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ruichengbing on 2017/2/17.
 */

@Entity
public class Face {
    @Id
    Long id;
    @NotNull
    String localPath;
    String gender;
    String name;
    String pwd;
    @Generated(hash = 647494211)
    public Face(Long id, @NotNull String localPath, String gender, String name,
            String pwd) {
        this.id = id;
        this.localPath = localPath;
        this.gender = gender;
        this.name = name;
        this.pwd = pwd;
    }
    @Generated(hash = 601504354)
    public Face() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLocalPath() {
        return this.localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
