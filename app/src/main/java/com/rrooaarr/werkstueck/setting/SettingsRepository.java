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
            UserSetting set = params[0];
            mAsyncTaskDao.updateProperties(set.getId(), set.getServer(), set.getPort(), set.getUsername(), set.getPassword());
            return null;
        }
    }

}
