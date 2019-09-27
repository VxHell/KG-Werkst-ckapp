package com.rrooaarr.werkstueck.setting;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class SettingsRepository {

    private UserSettingDao mSettingsDao;
    private LiveData<UserSetting> settingLiveData;

    public SettingsRepository(Application application) {
        UserSettingsRoomDatabase db = UserSettingsRoomDatabase.getDatabase(application);
        mSettingsDao = db.settingDao();
        settingLiveData = mSettingsDao.getSetting();
    }

    LiveData<UserSetting> getSetting() {
        return settingLiveData;
    }

    LiveData<UserSetting> loadSettings() {
        settingLiveData = mSettingsDao.getSetting();
        return settingLiveData;
    }

    public void insert (UserSetting setting) {
        new insertAsyncTask(mSettingsDao).execute(setting);
    }

    public void update (UserSetting setting) {
        new UpdateAsyncTask(mSettingsDao).execute(setting);
    }

    private static class insertAsyncTask extends AsyncTask<UserSetting, Void, Void> {

        private UserSettingDao mAsyncTaskDao;

        insertAsyncTask(UserSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserSetting... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<UserSetting, Void, Void> {

        private UserSettingDao mAsyncTaskDao;

        UpdateAsyncTask(UserSettingDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserSetting... params) {
            mAsyncTaskDao.update(params[0], params[0].getId());
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

}
