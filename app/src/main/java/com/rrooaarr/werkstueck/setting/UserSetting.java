package com.rrooaarr.werkstueck.setting;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_setting")
public class UserSetting {

    @PrimaryKey
    private int id;

    @NonNull
    @ColumnInfo(name = "server")
    private String mServer;

    @ColumnInfo(name = "port")
    private String mPort;

    @NonNull
    @ColumnInfo(name = "username")
    private String mUsername;

    @NonNull
    @ColumnInfo(name = "password")
    private String mPassword;

    public UserSetting(@NonNull String mServer,@NonNull String mPort, @NonNull String mUsername, @NonNull String mPassword) {
        this.mServer = mServer;
        this.mPort = mPort;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(@NonNull String mPassword) {
        this.mPassword = mPassword;
    }

    @NonNull
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(@NonNull String mUsername) {
        this.mUsername = mUsername;
    }


    @NonNull
    public String getServer() {
        return mServer;
    }

    public void setServer(@NonNull String mServer) {
        this.mServer = mServer;
    }
    @NonNull
    public String getPort() {
        return mPort;
    }

    public void setPort(String mPort) {
        this.mPort = mPort;
    }

}