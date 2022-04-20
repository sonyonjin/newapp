package com.skyautonet.seda_aiv.data.source.local.db.dao;

import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AppManagedConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAppManagedConfig(AppManagedConfig config);

    @Update
    public void updateAppManagedConfig(AppManagedConfig config);

    @Delete
    public void deleteAppManagedConfig(AppManagedConfig config);

    @Query("SELECT * FROM application_config_table")
    public LiveData<List<AppManagedConfig>> getAppManagedConfigLiveData();

    // Use this if you want to get the data immediately (LiveData does not contain data until the update notification)
    @Query("SELECT * FROM application_config_table")
    public List<AppManagedConfig> getAppManagedConfigData();

}
