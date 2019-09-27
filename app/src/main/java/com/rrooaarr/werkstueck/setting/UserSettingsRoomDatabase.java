package com.rrooaarr.werkstueck.setting;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {UserSetting.class}, version = 1)
public abstract class UserSettingsRoomDatabase extends RoomDatabase {

    public abstract UserSettingDao settingDao();

    private static volatile UserSettingsRoomDatabase INSTANCE;

    static UserSettingsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UserSettingsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UserSettingsRoomDatabase.class, "user_setting")
                            .addCallback(sRoomDatabaseCallback)
//                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
        new Callback(){
            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }
        };

//    public LiveData<UserSetting> loadDB(){
//        return new LoadFromDbAsync(INSTANCE).execute();
//    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final UserSettingDao mDao;

        PopulateDbAsync(UserSettingsRoomDatabase db) {
            mDao = db.settingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            UserSetting setting = new UserSetting("server", "port", "username", "pass");
            mDao.insert(setting);
            return null;
        }
    }

    public static class InsertDbAsync extends AsyncTask<UserSetting, Void, Void> {

        private final UserSettingDao mSettingDao;

        InsertDbAsync(UserSettingsRoomDatabase db) {
            mSettingDao = db.settingDao();
        }

        @Override
        protected Void doInBackground(UserSetting... userSettings) {
            mSettingDao.insert(userSettings[0]);
            return null;
        }

    }

    private static class LoadFromDbAsync extends AsyncTask<Void, Void, LiveData<UserSetting>> {

        private final UserSettingDao mSettingDao;

        LoadFromDbAsync(UserSettingsRoomDatabase db) {
            mSettingDao = db.settingDao();
        }

        @Override
        protected LiveData<UserSetting> doInBackground(final Void... params) {
            return mSettingDao.getSetting();
        }
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here. Data kept and version increased safely.
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Create the new table
            database.execSQL(
                    "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");
            // Copy the data
            database.execSQL(
                    "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
            // Remove the old table
            database.execSQL("DROP TABLE users");
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE users_new RENAME TO users");
            // Add column
            database.execSQL("ALTER TABLE users_new "
                    + " ADD COLUMN last_update INTEGER");
        }
    };
}