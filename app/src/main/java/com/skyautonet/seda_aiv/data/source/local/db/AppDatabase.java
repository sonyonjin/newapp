package com.skyautonet.seda_aiv.data.source.local.db;

import android.content.Context;

import com.skyautonet.seda_aiv.SAApp;
import com.skyautonet.seda_aiv.data.source.local.db.dao.AppManagedConfigDao;
import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AppManagedConfig.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    private static final String DATABASE_NAME = "app_database";

    public abstract AppManagedConfigDao appManagedConfigDao();

    public static AppDatabase getDatabase() {
        if (INSTANCE == null) {
            Context context = SAApp.instance;
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration() // Rebuild database when migration fails
                    .allowMainThreadQueries() // Allow query in main thread
                    .build();
        }

        return INSTANCE;
    }

}
