package com.skyautonet.seda_aiv.data.source.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "application_config_table")
public class AppManagedConfig {
    @PrimaryKey
    public int id;

    public String ssid;
    public String password;
    public String regNo;
    public String rtspLink1;
    public String rtspLink2;
    public String rtspLink3;
    public String ipAddress;
}
